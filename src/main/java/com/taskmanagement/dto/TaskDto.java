package com.taskmanagement.dto;

import com.taskmanagement.model.Task;
import java.time.LocalDateTime;

public class TaskDto {
    private String title;
    private String description;
    private Task.Status status;
    private Task.Priority priority;
    private Long projectId;
    private Long createdById;
    private Long assignedToId;
    private Long parentTaskId;
    private LocalDateTime dueDate;

    public TaskDto() {}

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
    public Task.Status getStatus() 
    { 
        return status; 
    }
    public void setStatus(Task.Status status)
    { 
        this.status = status; 
    }
    public Task.Priority getPriority() 
    { 
        return priority; 
    }
    public void setPriority(Task.Priority priority)
    { 
        this.priority = priority; 
    }
    public Long getProjectId() 
    { 
        return projectId; 
    }
    public void setProjectId(Long projectId) 
    { 
        this.projectId = projectId; 
    }
    public Long getCreatedById() 
    { 
        return createdById; 
    }
    public void setCreatedById(Long createdById) 
    { 
        this.createdById = createdById; 
    }
    public Long getAssignedToId() 
    { 
        return assignedToId; 
    }
    public void setAssignedToId(Long assignedToId) 
    { 
        this.assignedToId = assignedToId; 
    }
    public Long getParentTaskId() 
    { 
        return parentTaskId; 
    }
    public void setParentTaskId(Long parentTaskId) 
    { 
        this.parentTaskId = parentTaskId; 
    }
    public LocalDateTime getDueDate() 
    { 
        return dueDate; 
    }
    public void setDueDate(LocalDateTime dueDate) 
    { 
        this.dueDate = dueDate; 
    }
}
