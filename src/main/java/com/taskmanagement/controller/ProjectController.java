package com.taskmanagement.controller;

import com.taskmanagement.dto.ProjectDto;
import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.TeamMember;
import com.taskmanagement.service.ProjectService;
import com.taskmanagement.service.SlackService;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SlackService slackService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectDto dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }

    @GetMapping
    public ResponseEntity<Page<Project>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.getAllProjects(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody ProjectDto dto) {
        return ResponseEntity.ok(projectService.updateProject(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Page<Task>> getTasksByProject(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getTasksByProject(id, page, size));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<Page<TeamMember>> getMembersByProject(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.getMembersByProject(id, page, size));
    }

    @GetMapping("/{id}/slack-members")
    public ResponseEntity<List<Map<String, String>>> getSlackMembers(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(slackService.getWorkspaceMembers(project.getSlackBotToken()));
    }
}
