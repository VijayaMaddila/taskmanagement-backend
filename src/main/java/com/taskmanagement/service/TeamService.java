package com.taskmanagement.service;

import com.taskmanagement.dto.InviteDto;
import com.taskmanagement.dto.TeamDto;
import com.taskmanagement.dto.TeamMemberDto;
import com.taskmanagement.model.*;
import com.taskmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectInviteRepository projectInviteRepository;

    @Autowired
    private EmailService emailService;

    // Team CRUD

    public Team createTeam(TeamDto dto) {
        Team team = new Team();
        team.setName(dto.getName());
        return teamRepository.save(team);
    }

    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found: " + id));
    }

    // Team Members

    public TeamMember addMember(TeamMemberDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found: " + dto.getTeamId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setUser(user);
        member.setRole(dto.getRole());
        return teamMemberRepository.save(member);
    }

    public Page<TeamMember> getMembersByTeam(Long teamId, int page, int size) {
        return teamMemberRepository.findByTeamId(teamId,
                PageRequest.of(page, size, Sort.by("joinedAt").descending()));
    }

    public Page<TeamMember> getMembersByProject(Long projectId, int page, int size) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
        if (project.getTeam() == null) throw new RuntimeException("No team assigned to project: " + projectId);
        return getMembersByTeam(project.getTeam().getId(), page, size);
    }

    public void removeMember(Long memberId) {
        teamMemberRepository.deleteById(memberId);
    }

    // Invites

    public ProjectInvite sendInvite(InviteDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found: " + dto.getProjectId()));

        List<ProjectInvite> existing = projectInviteRepository.findByEmail(dto.getEmail());
        ProjectInvite pendingInvite = existing.stream()
                .filter(i -> i.getProject().getId().equals(dto.getProjectId()) &&
                             i.getStatus() == ProjectInvite.InviteStatus.PENDING)
                .findFirst()
                .orElse(null);
        if (pendingInvite != null) {
            try {
                emailService.sendProjectInvite(pendingInvite.getEmail(), project.getName(), pendingInvite.getToken());
            } catch (Exception e) { }
            return pendingInvite;
        }

        ProjectInvite invite = new ProjectInvite();
        invite.setProject(project);
        invite.setEmail(dto.getEmail());
        invite.setRole(dto.getRole());
        invite.setToken(UUID.randomUUID().toString());
        invite.setStatus(ProjectInvite.InviteStatus.PENDING);
        projectInviteRepository.save(invite);

        try {
            emailService.sendProjectInvite(invite.getEmail(), project.getName(), invite.getToken());
        } catch (Exception e) { }

        return invite;
    }

    public void acceptInvite(String token, Long userId) {
        ProjectInvite invite = projectInviteRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid invite token"));

        if (invite.getStatus() != ProjectInvite.InviteStatus.PENDING) {
            throw new RuntimeException("Invite already " + invite.getStatus().name().toLowerCase());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Project project = invite.getProject();
        if (project.getTeam() != null) {
            TeamMember member = new TeamMember();
            member.setTeam(project.getTeam());
            member.setUser(user);
            member.setRole(invite.getRole());
            teamMemberRepository.save(member);
        }

        invite.setStatus(ProjectInvite.InviteStatus.ACCEPTED);
        projectInviteRepository.save(invite);
    }

    public void declineInvite(String token) {
        ProjectInvite invite = projectInviteRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid invite token"));

        if (invite.getStatus() != ProjectInvite.InviteStatus.PENDING) {
            throw new RuntimeException("Invite already " + invite.getStatus().name().toLowerCase());
        }

        invite.setStatus(ProjectInvite.InviteStatus.DECLINED);
        projectInviteRepository.save(invite);
    }

    public Map<String, String> getInviteInfo(String token) {
        ProjectInvite invite = projectInviteRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid invite token"));
        return Map.of(
            "email", invite.getEmail(),
            "projectName", invite.getProject().getName(),
            "role", invite.getRole().name(),
            "status", invite.getStatus().name()
        );
    }

    public Page<ProjectInvite> getPendingInvites(Long projectId, int page, int size) {
        return projectInviteRepository.findByProjectId(projectId,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    public Page<ProjectInvite> getInvitesByEmail(String email, int page, int size) {
        return projectInviteRepository.findByEmail(email,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
}
