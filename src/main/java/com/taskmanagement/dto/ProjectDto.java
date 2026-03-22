package com.taskmanagement.dto;

import com.taskmanagement.model.Team;

import java.time.LocalDate;
import java.util.List;

public class ProjectDto {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long createdById;
    private Long teamId;
    private List<MemberInvite> members;
    private String slackWebhookUrl;
    private String slackBotToken;

    public static class MemberInvite {
        private Long userId;
        private String email;
        private String name;
        private Team.TeamRole role;

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Team.TeamRole getRole() {
            return role;
        }
        public void setRole(Team.TeamRole role) {
            this.role = role;
        }
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
    public Long getCreatedById()
    {
        return createdById;
    }
    public void setCreatedById(Long createdById)
    {
        this.createdById = createdById;
    }
    public Long getTeamId()
    {
        return teamId;
    }
    public void setTeamId(Long teamId)
    {
        this.teamId = teamId;
    }
    public List<MemberInvite> getMembers()
    {
        return members;
    }
    public void setMembers(List<MemberInvite> members)
    {
        this.members = members;
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
}
