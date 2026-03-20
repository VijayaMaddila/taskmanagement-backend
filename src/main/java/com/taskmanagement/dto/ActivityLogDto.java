package com.taskmanagement.dto;

import com.taskmanagement.model.Task;

public class ActivityLogDto {

    private Long userId;      
    private String action;    
    private String entityType; 
    private Long entityId;     
    private Long taskId;
    private Task.Status oldStatus;
    private Task.Status newStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Task.Status getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Task.Status oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Task.Status getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Task.Status newStatus) {
        this.newStatus = newStatus;
    }
}
