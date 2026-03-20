package com.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "tasks",
    indexes = {
        @Index(name = "idx_task_project", columnList = "project_id"),
        @Index(name = "idx_task_status", columnList = "status"),
        @Index(name = "idx_task_priority", columnList = "priority"),
        @Index(name = "idx_task_assigned_to", columnList = "assigned_to"),
        @Index(name = "idx_task_created_by", columnList = "created_by"),
        @Index(name = "idx_task_due_date", columnList = "due_date"),
        @Index(name = "idx_task_project_status", columnList = "project_id,status")
    }
)
public class Task {

    public enum Status { TODO, IN_PROGRESS, IN_REVIEW, DONE }
    public enum Priority { LOW, MEDIUM, HIGH, CRITICAL }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties({"projects", "password"})
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    @JsonIgnoreProperties({"projects", "password"})
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    @JsonIgnoreProperties({"project", "createdBy", "assignedTo", "parentTask"})
    private Task parentTask;

    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() 
    {
         return id; 
    }
    public String getTitle() 
    { 
        return title;
    }
    public void setTitle(String title) 
    { 
        this.title = title; 
    }
    public String getDescription() 
    { 
        return description; 
    }
    public void setDescription(String description) 
    { 
        this.description = description; 
    }
    public Status getStatus() 
    { 
        return status; 
    }
    public void setStatus(Status status) 
    
    { 
        this.status = status; 
    }
    public Priority getPriority() 
    { 
        return priority; 
    }
    public void setPriority(Priority priority) 
    { 
        this.priority = priority; 
    }
    public Project getProject() 
    { 
        return project; 
    }
    public void setProject(Project project)
    { 
        this.project = project; 
    }
    public User getCreatedBy() 
    { 
        return createdBy; 
    }
    public void setCreatedBy(User createdBy) 
    { 
        this.createdBy = createdBy; 
    }
    public User getAssignedTo() 
    { 
        return assignedTo; 
    }
    public void setAssignedTo(User assignedTo)
    { 
        this.assignedTo = assignedTo; 
    }
    public Task getParentTask() 
    { 
        return parentTask; 
    }
    public void setParentTask(Task parentTask) 
    { 
        this.parentTask = parentTask; 
    }
    public LocalDateTime getDueDate() 
    { 
        return dueDate; 
    }
    public void setDueDate(LocalDateTime dueDate) 
    { 
        this.dueDate = dueDate; 
    }
    public LocalDateTime getCreatedAt() 
    { 
        return createdAt; 
    }
    public LocalDateTime getUpdatedAt() 
    { 
        return updatedAt; 
    }
}
