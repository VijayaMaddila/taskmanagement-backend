package com.taskmanagement.dto;

import com.taskmanagement.model.Team;

public class TeamMemberDto {
    private Long teamId;
    private Long userId;
    private Team.TeamRole role;

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
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
