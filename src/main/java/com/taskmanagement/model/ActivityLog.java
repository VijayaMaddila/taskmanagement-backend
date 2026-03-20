package com.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "activity_log",
    indexes = {
        @Index(name = "idx_actlog_user_created", columnList = "user_id, created_at"),
        @Index(name = "idx_actlog_task_created", columnList = "task_id, created_at"),
        @Index(name = "idx_actlog_entity", columnList = "entity_type, entity_id, created_at")
    }
)
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"projects", "password", "is_deleted"})
    private User user;

    @Column(nullable = false)
    private String action;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonIgnoreProperties({"project", "createdBy", "assignedTo", "parentTask"})
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private Task.Status oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private Task.Status newStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
