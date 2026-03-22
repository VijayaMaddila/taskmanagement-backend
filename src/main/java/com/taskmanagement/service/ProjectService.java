package com.taskmanagement.service;

import com.taskmanagement.dto.ProjectDto;
import com.taskmanagement.model.*;
import com.taskmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectInviteRepository projectInviteRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private SlackService slackService;

    public Project createProject(ProjectDto dto) {
        Project project = new Project();
        mapDtoToProject(dto, project);
        Project saved = projectRepository.save(project);
        slackService.notifyProjectCreated(saved);

        Team team = saved.getTeam();

        // Add creator as ADMIN member of the team
        if (dto.getCreatedById() != null && team != null) {
            User creator = userRepository.findById(dto.getCreatedById()).orElse(null);
            if (creator != null) {
                TeamMember creatorMember = new TeamMember();
                creatorMember.setTeam(team);
                creatorMember.setUser(creator);
                creatorMember.setRole(Team.TeamRole.ADMIN);
                teamMemberRepository.save(creatorMember);
            }
        }

        // Invite members
        if (dto.getMembers() != null && team != null) {
            for (ProjectDto.MemberInvite invite : dto.getMembers()) {
                Team.TeamRole role = invite.getRole() != null ? invite.getRole() : Team.TeamRole.DEVELOPER;

                User user = null;
                if (invite.getUserId() != null) {
                    user = userRepository.findById(invite.getUserId()).orElse(null);
                } else if (invite.getEmail() != null) {
                    user = userRepository.findByEmail(invite.getEmail());
                }

                if (user != null) {
                    TeamMember member = new TeamMember();
                    member.setTeam(team);
                    member.setUser(user);
                    member.setRole(role);
                    teamMemberRepository.save(member);
                } else {
                    String email = invite.getEmail();
                    if (email != null) {
                        ProjectInvite pending = new ProjectInvite();
                        pending.setProject(saved);
                        pending.setEmail(email);
                        pending.setName(invite.getName());
                        pending.setRole(role);
                        pending.setToken(UUID.randomUUID().toString());
                        projectInviteRepository.save(pending);
                        emailService.sendProjectInvite(email, saved.getName(), pending.getToken());
                    }
                }
            }
        }

        return saved;
    }

    public Page<Project> getAllProjects(int page, int size) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found");
        return projectRepository.findAllByUserId(user.getId(), PageRequest.of(page, size));
    }

    public Project getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found: " + id));
        if (project.isIs_deleted()) throw new RuntimeException("Project not found: " + id);
        return project;
    }

    public Project updateProject(Long id, ProjectDto dto) {
        Project project = getProjectById(id);
        mapDtoToProject(dto, project);
        Project updated = projectRepository.save(project);
        slackService.notifyProjectUpdated(updated);
        return updated;
    }

    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        project.setIs_deleted(true);
        projectRepository.save(project);
    }

    private void mapDtoToProject(ProjectDto dto, Project project) {
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setSlackWebhookUrl(dto.getSlackWebhookUrl());
        project.setSlackBotToken(dto.getSlackBotToken());

        if (dto.getCreatedById() != null) {
            User creator = userRepository.findById(dto.getCreatedById())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getCreatedById()));
            project.setCreatedBy(creator);
        }

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found: " + dto.getTeamId()));
            project.setTeam(team);
        }
    }
}
