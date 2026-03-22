package com.taskmanagement.controller;

import com.taskmanagement.dto.ActivityLogDto;
import com.taskmanagement.model.ActivityLog;
import com.taskmanagement.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/activity")
@CrossOrigin
public class ActivityLogController {

    @Autowired
    private ActivityLogService activityLogService;

    //get all logs
    @GetMapping
    public ResponseEntity<Page<ActivityLog>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(activityLogService.getAllLogs(page, size));
    }

    //get all logs by userid
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ActivityLog>> getLogsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(activityLogService.getLogsByUser(userId, page, size));
    }

    
    @GetMapping("/entity")
    public ResponseEntity<Page<ActivityLog>> getLogsByEntity(
            @RequestParam String entityType,
            @RequestParam Long entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(activityLogService.getLogsByEntity(entityType, entityId, page, size));
    }


    @GetMapping("/task/{taskId}/status-history")
    public ResponseEntity<Page<ActivityLog>> getStatusHistoryByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(activityLogService.getStatusHistoryByTask(taskId, page, size));
    }

    
    @PostMapping
    public ResponseEntity<ActivityLog> createLog(@RequestBody ActivityLogDto dto) {
        return ResponseEntity.ok(activityLogService.createLog(dto));
    }
}
