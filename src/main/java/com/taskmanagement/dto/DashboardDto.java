package com.taskmanagement.dto;

public class DashboardDto {
    private long totalProjects;
    private long openIssues;
    private long inProgress;
    private long completed;

    public long getTotalProjects()
     { 
        return totalProjects; 
    }
    public void setTotalProjects(long totalProjects)
     { 
        this.totalProjects = totalProjects; 
    }

    public long getOpenIssues()
    { 
        return openIssues; 
    }
    public void setOpenIssues(long openIssues) 
    { 
        this.openIssues = openIssues; 
    }
    public long getInProgress() 
    { 
        return inProgress; 
    }
    public void setInProgress(long inProgress)
    { 
        this.inProgress = inProgress;
    }

    public long getCompleted()
    { 
        return completed; 
    }
    public void setCompleted(long completed)
    { 
        this.completed = completed; 
    }
}
