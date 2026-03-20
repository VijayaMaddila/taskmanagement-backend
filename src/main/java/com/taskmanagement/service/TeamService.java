package com.taskmanagement.service;

import com.taskmanagement.dto.InviteDto;
import com.taskmanagement.dto.TeamDto;
import com.taskmanagement.model.Project;
import com.taskmanagement.model.ProjectInvite;
import com.taskmanagement.model.Team;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.ProjectInviteRepository;
import com.taskmanagement.repository.ProjectRepository;
import com.taskmanagement.repository.TeamRepository;
import com.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectInviteRepository projectInviteRepository;

    @Autowired
    private EmailService emailService;

    //Team members

    public Team addMember(TeamDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found: " + dto.getProjectId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        Team team = new Team();
        team.setProject(project);
        team.setUser(user);
        team.setRole(dto.getRole());
        return teamRepository.save(team);
    }

    public List<Team> getMembersByProject(Long projectId) {
        return teamRepository.findByProjectId(projectId);
    }

    public void removeMember(Long teamId) {
        teamRepository.deleteById(teamId);
    }

    //Invites

    //Create a ProjectInvite and send an email with the invite token. 
    public ProjectInvite sendInvite(InviteDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found: " + dto.getProjectId()));

        // If a pending invite already exists, resend the email with the existing token
        List<ProjectInvite> existing = projectInviteRepository.findByEmail(dto.getEmail());
        ProjectInvite pendingInvite = existing.stream()
                .filter(i -> i.getProject().getId().equals(dto.getProjectId()) &&
                             i.getStatus() == ProjectInvite.InviteStatus.PENDING)
                .findFirst()
                .orElse(null);
        if (pendingInvite != null) {
            try {
                emailService.sendProjectInvite(pendingInvite.getEmail(), project.getName(), pendingInvite.getToken());
            } catch (Exception e) {
                
            }
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
        } catch (Exception e) {
        
        }

        return invite;
    }

    // Accept a pending invite and add the user to the team. 
    public void acceptInvite(String token, Long userId) {
        ProjectInvite invite = projectInviteRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid invite token"));

        if (invite.getStatus() != ProjectInvite.InviteStatus.PENDING) {
            throw new RuntimeException("Invite already " + invite.getStatus().name().toLowerCase());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Team team = new Team();
        team.setProject(invite.getProject());
        team.setUser(user);
        team.setRole(invite.getRole());
        teamRepository.save(team);

        invite.setStatus(ProjectInvite.InviteStatus.ACCEPTED);
        projectInviteRepository.save(invite);
    }

    // Decline a pending invite.
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

    public List<ProjectInvite> getPendingInvites(Long projectId) {
        return projectInviteRepository.findByProjectId(projectId);
    }
    public List<ProjectInvite> getInvitesByEmail(String email) {
        return projectInviteRepository.findByEmail(email);
    }
}
