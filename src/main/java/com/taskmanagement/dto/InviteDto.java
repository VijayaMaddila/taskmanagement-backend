package com.taskmanagement.dto;

import com.taskmanagement.model.Team;

public class InviteDto {
    private Long projectId;
    private String email;
    private Team.TeamRole role;

    public Long getProjectId() 
    { 
        return projectId; 
    }
    public void setProjectId(Long projectId)
    { 
        this.projectId = projectId; 
    }

    public String getEmail() 
    { 
        return email; 
    }
    public void setEmail(String email)
    { 
        this.email = email; 
    }

    public Team.TeamRole getRole() 
    { 
        return role; 
    }
    public void setRole(Team.TeamRole role) 
    { 
        this.role = role; 
    }

}
