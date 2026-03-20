package com.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "projects",
    indexes = {
        @Index(name = "idx_project_user", columnList = "created_by"),
        @Index(name = "idx_project_deleted", columnList = "is_deleted"),
        @Index(name = "idx_project_name", columnList = "name")
    }
)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean is_deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties({"projects", "password"})
    private User createdBy;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties({"project", "createdBy", "assignedTo"})
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties({"project"})
    private List<Team> members = new ArrayList<>();

    private String slackWebhookUrl;
    private String slackBotToken;

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
    public String getName() 
    { 
        return name; 
    }
    public void setName(String name) 
    { 
        this.name = name; 
    }
    public String getDescription() 
    { 
        return description; 
    }
    public void setDescription(String description) 
    { 
        this.description = description; 
    }
    public LocalDate getStartDate() 
    { 
        return startDate; 
    }
    public void setStartDate(LocalDate startDate) 
    { 
        this.startDate = startDate; 
    }
    public LocalDate getEndDate() 
    { 
        return endDate; 
    }
    public void setEndDate(LocalDate endDate) 
    { 
        this.endDate = endDate; 
    }
    public boolean isIs_deleted() 
    { 
        return is_deleted; 
    }
    public void setIs_deleted(boolean is_deleted) 
    { 
        this.is_deleted = is_deleted; 
    }
    public User getCreatedBy() 
    { 
        return createdBy; 
    }
    public void setCreatedBy(User createdBy) 
    { 
        this.createdBy = createdBy; 
    }
    public List<Task> getTasks() 
    { 
        return tasks; 
    }
    public List<Team> getMembers() 
    { 
        return members; 
    }
    public String getSlackWebhookUrl()
    {
        return slackWebhookUrl;
    }
    public void setSlackWebhookUrl(String slackWebhookUrl)
    {
        this.slackWebhookUrl = slackWebhookUrl;
    }
    public String getSlackBotToken()
    {
        return slackBotToken;
    }
    public void setSlackBotToken(String slackBotToken)
    {
        this.slackBotToken = slackBotToken;
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
