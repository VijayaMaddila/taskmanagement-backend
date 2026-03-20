package com.taskmanagement.dto;

import com.taskmanagement.model.Team;

public class TeamDto {
    private Long projectId;
    private Long userId;
    private Team.TeamRole role;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Team.TeamRole getRole() {
        return role;
    }

    public void setRole(Team.TeamRole role) {
        this.role = role;
    }
}
