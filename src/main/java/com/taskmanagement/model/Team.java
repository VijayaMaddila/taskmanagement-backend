package com.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "teams",
    indexes = {
        @Index(name = "idx_team_project", columnList = "project_id"),
        @Index(name = "idx_team_user", columnList = "user_id"),
        @Index(name = "idx_team_role", columnList = "role"),
        @Index(name = "idx_team_project_user", columnList = "project_id, user_id")
    }
)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"projects", "password"})
    private User user;

    @Enumerated(EnumType.STRING)
    private TeamRole role;

    private LocalDateTime joinedAt;

    public Team() {
        this.joinedAt = LocalDateTime.now();
    }

    public enum TeamRole {
        ADMIN, DEVELOPER
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TeamRole getRole() {
        return role;
    }

    public void setRole(TeamRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
