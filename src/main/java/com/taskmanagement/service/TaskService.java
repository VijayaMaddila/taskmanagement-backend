package com.taskmanagement.service;

import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.ProjectRepository;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlackService slackService;

    public Task createTask(TaskDto dto) {
        Task task = new Task();
        mapDtoToTask(dto, task);
        Task saved = taskRepository.save(task);
        slackService.notifyTaskCreated(saved);
        return saved;
    }

    public Page<Task> getAllTasks(int page, int size) {
        return taskRepository.findAll(PageRequest.of(page, size));
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found: " + id));
    }

    public Page<Task> getTasksByProject(Long projectId, int page, int size) {
        return taskRepository.findByProjectId(projectId, PageRequest.of(page, size));
    }

    public Page<Task> getTasksByAssignee(Long userId, int page, int size) {
        return taskRepository.findByAssignedToId(userId, PageRequest.of(page, size));
    }

    public Task updateTask(Long id, TaskDto dto) {
        Task task = getTaskById(id);
        String oldStatus = task.getStatus() != null ? task.getStatus().name() : null;
        Long oldAssigneeId = task.getAssignedTo() != null ? task.getAssignedTo().getId() : null;
        mapDtoToTask(dto, task);
        Task saved = taskRepository.save(task);
        String newStatus = saved.getStatus() != null ? saved.getStatus().name() : null;
        if (oldStatus != null && !oldStatus.equals(newStatus)) {
            slackService.notifyTaskStatusChanged(saved, oldStatus);
        }
        Long newAssigneeId = saved.getAssignedTo() != null ? saved.getAssignedTo().getId() : null;
        if (newAssigneeId != null && !newAssigneeId.equals(oldAssigneeId)) {
            slackService.notifyTaskAssigned(saved);
        }
        return saved;
    }

    public void deleteTask(Long id) {
        taskRepository.delete(getTaskById(id));
    }

    private void mapDtoToTask(TaskDto dto, Task task) {
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        if (dto.getStatus() != null) 
            {
                task.setStatus(dto.getStatus());
            }
        if (dto.getPriority() != null) 
            {
                task.setPriority(dto.getPriority());
            }
        task.setDueDate(dto.getDueDate());

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found: " + dto.getProjectId()));
            task.setProject(project);
        }
        if (dto.getCreatedById() != null) {
            User creator = userRepository.findById(dto.getCreatedById())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getCreatedById()));
            task.setCreatedBy(creator);
        }
        if (dto.getAssignedToId() != null) {
            User assignee = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getAssignedToId()));
            task.setAssignedTo(assignee);
        }
        if (dto.getParentTaskId() != null) {
            Task parent = taskRepository.findById(dto.getParentTaskId())
                    .orElseThrow(() -> new RuntimeException("Parent task not found: " + dto.getParentTaskId()));
            task.setParentTask(parent);
        }
    }
}
