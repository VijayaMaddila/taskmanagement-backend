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
    private SlackService slackService;

    public Project createProject(ProjectDto dto) {
        Project project = new Project();
        mapDtoToProject(dto, project);
        Project saved = projectRepository.save(project);
        slackService.notifyProjectCreated(saved);

        
        if (dto.getCreatedById() != null) {
            User creator = userRepository.findById(dto.getCreatedById()).orElse(null);
            if (creator != null) {
                Team creatorTeam = new Team();
                creatorTeam.setProject(saved);
                creatorTeam.setUser(creator);
                creatorTeam.setRole(Team.TeamRole.ADMIN);
                teamRepository.save(creatorTeam);
            }
        }

        // invite members
        if (dto.getMembers() != null) {
            for (ProjectDto.MemberInvite invite : dto.getMembers()) {
                Team.TeamRole role = invite.getRole() != null ? invite.getRole() : Team.TeamRole.DEVELOPER;

            
                User user = null;
                if (invite.getUserId() != null) {
                    user = userRepository.findById(invite.getUserId()).orElse(null);
                } else if (invite.getEmail() != null) {
                    user = userRepository.findByEmail(invite.getEmail());
                }

                if (user != null) {
                    Team team = new Team();
                    team.setProject(saved);
                    team.setUser(user);
                    team.setRole(role);
                    teamRepository.save(team);

                } else {
                    String email = invite.getEmail();
                    if (email != null) {
                        ProjectInvite pending = new ProjectInvite();
                        pending.setProject(saved);
                        pending.setEmail(email);
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
        return projectRepository.save(project);
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
    }
}
