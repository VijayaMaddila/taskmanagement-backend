package com.taskmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "project_invites",
    indexes = {
        @Index(name = "idx_invite_token", columnList = "token"),
        @Index(name = "idx_invite_project_status", columnList = "project_id, status"),
        @Index(name = "idx_invite_email_status", columnList = "email, status")
    }
)
public class ProjectInvite {

    public enum InviteStatus { PENDING, ACCEPTED, DECLINED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Team.TeamRole role;

    @Enumerated(EnumType.STRING)
    private InviteStatus status = InviteStatus.PENDING;

    private String token;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() 
    { 
        return id; 
    }
    public Project getProject()
    { 
        return project; 
    }
    public void setProject(Project project) 
    { 
        this.project = project; 
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Team.TeamRole getRole() 
    { 
        return role; 
    }
    public void setRole(Team.TeamRole role) 
    { 
        this.role = role; 
    }
    public InviteStatus getStatus() 
    { 
        return status; 
    }
    public void setStatus(InviteStatus status) 
    { 
        this.status = status; 
    }
    public String getToken() 
    { 
        return token; 
    }
    public void setToken(String token) 
    { 
        this.token = token; 
    }
    public LocalDateTime getCreatedAt() 
    { 
        return createdAt; 
    }
}
