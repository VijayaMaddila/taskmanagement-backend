package com.taskmanagement.service;

import com.taskmanagement.dto.ActivityLogDto;
import com.taskmanagement.model.ActivityLog;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.ActivityLogRepository;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Page<ActivityLog> getAllLogs(int page, int size) {
        return activityLogRepository.findRecentLogs(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Page<ActivityLog> getLogsByUser(Long userId, int page, int size) {
        return activityLogRepository.findLogsByUserId(
                userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Page<ActivityLog> getLogsByEntity(String entityType, Long entityId, int page, int size) {
        return activityLogRepository.findLogsByEntity(
                entityType, entityId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public Page<ActivityLog> getStatusHistoryByTask(Long taskId, int page, int size) {
        return activityLogRepository.findStatusHistoryByTaskId(
                taskId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public ActivityLog createLog(ActivityLogDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setAction(dto.getAction());
        log.setEntityType(dto.getEntityType());
        log.setEntityId(dto.getEntityId());

        if (dto.getTaskId() != null) {
            Task task = taskRepository.findById(dto.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found with id: " + dto.getTaskId()));
            log.setTask(task);
            log.setOldStatus(dto.getOldStatus());
            log.setNewStatus(dto.getNewStatus());
        }

        return activityLogRepository.save(log);
    }
}
