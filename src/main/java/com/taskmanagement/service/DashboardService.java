package com.taskmanagement.service;

import com.taskmanagement.dto.DashboardDto;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.ProjectRepository;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardDto getDashboardStats() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found");
        Long userId = user.getId();

        DashboardDto dashboard = new DashboardDto();
        dashboard.setTotalProjects(projectRepository.countByUserId(userId));
        dashboard.setOpenIssues(taskRepository.countByStatusAndUserId(Task.Status.TODO, userId));
        dashboard.setInProgress(taskRepository.countByStatusAndUserId(Task.Status.IN_PROGRESS, userId));
        dashboard.setCompleted(taskRepository.countByStatusAndUserId(Task.Status.DONE, userId));
        return dashboard;
    }
}
