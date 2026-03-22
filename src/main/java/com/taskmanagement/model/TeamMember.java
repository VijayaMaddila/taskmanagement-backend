package com.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "team_members",
    indexes = {
        @Index(name = "idx_team_member_team", columnList = "team_id"),
        @Index(name = "idx_team_member_user", columnList = "user_id")
    }
)
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonBackReference
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"projects", "password"})
    private User user;

    @Enumerated(EnumType.STRING)
    private Team.TeamRole role;

    private LocalDateTime joinedAt;

    public TeamMember() {
        this.joinedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team.TeamRole getRole() {
        return role;
    }

    public void setRole(Team.TeamRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
