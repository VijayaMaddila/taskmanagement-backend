package com.taskmanagement.service;

import com.taskmanagement.model.Project;
import com.taskmanagement.model.Task;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SlackService {

    private final RestTemplate restTemplate = new RestTemplate();

    //Workspace Members

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map<String, String>> getWorkspaceMembers(String botToken) {
        List<Map<String, String>> result = new ArrayList<>();
        if (botToken == null || botToken.isBlank()) return result;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + botToken);
            ResponseEntity<Map> response = restTemplate.exchange(
                "https://slack.com/api/users.list",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
            );
            Map body = response.getBody();
            if (body == null || !Boolean.TRUE.equals(body.get("ok"))) return result;

            List<Map<String, Object>> members = (List<Map<String, Object>>) body.get("members");
            if (members == null) return result;

            for (Map<String, Object> member : members) {
                if (Boolean.TRUE.equals(member.get("is_bot"))) continue;
                if (Boolean.TRUE.equals(member.get("deleted"))) continue;

                Map<String, Object> profile = (Map<String, Object>) member.get("profile");
                String displayName = profile != null && profile.get("display_name") != null
                        ? profile.get("display_name").toString() : "";
                String realName = member.getOrDefault("real_name", "").toString();
                String email    = profile != null && profile.get("email") != null
                        ? profile.get("email").toString() : "";

                Map<String, String> user = new LinkedHashMap<>();
                user.put("id",       member.getOrDefault("id", "").toString());
                user.put("name",     displayName.isBlank() ? realName : displayName);
                user.put("realName", realName);
                user.put("email",    email);
                result.add(user);
            }
        } catch (Exception ignored) {}
        return result;
    }

    //Send

    private void send(String payload, String webhookUrl) {
        if (webhookUrl == null || webhookUrl.isBlank()) return;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.postForEntity(webhookUrl, new HttpEntity<>(payload, headers), String.class);
        } catch (Exception ignored) {
    
        }
    }

    //Task Created 

    public void notifyTaskCreated(Task task) {
        if (task.getProject() == null || task.getProject().getSlackWebhookUrl() == null) return;

        String priority = task.getPriority() != null ? task.getPriority().name() : "N/A";
        String status   = task.getStatus()   != null ? task.getStatus().name()   : "N/A";
        String assignee = task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : "Unassigned";
        String creator  = task.getCreatedBy() != null  ? task.getCreatedBy().getUsername()  : "Unknown";
        String dueDate  = task.getDueDate()   != null  ? task.getDueDate().toString()        : "Not set";

        String payload = attachment(
            "#2eb886",
            "New Task Created",
            "A new task has been added to *" + e(task.getProject().getName()) + "*",
            e(task.getTitle()),
            fields(
                field("Project",    e(task.getProject().getName()), true),
                field("Priority",   priorityLabel(priority),         true),
                field("Status",     e(status),                       true),
                field("Assignee",   e(assignee),                     true),
                field("Created By", e(creator),                      true),
                field("Due Date",   e(dueDate),                      true)
            )
        );

        send(payload, task.getProject().getSlackWebhookUrl());
    }

    //Task Status Changed

    public void notifyTaskStatusChanged(Task task, String oldStatus) {
        if (task.getProject() == null || task.getProject().getSlackWebhookUrl() == null) return;

        String newStatus = task.getStatus()   != null ? task.getStatus().name()              : "N/A";
        String assignee  = task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : "Unassigned";
        String priority  = task.getPriority()   != null ? task.getPriority().name()          : "N/A";

        String payload = attachment(
            "#f0a500",
            "Task Status Updated",
            "Status changed on *" + e(task.getProject().getName()) + "*",
            e(task.getTitle()),
            fields(
                field("Project",         e(task.getProject().getName()), true),
                field("Previous Status", e(oldStatus),                   true),
                field("New Status",      e(newStatus),                   true),
                field("Priority",        priorityLabel(priority),        true),
                field("Assignee",        e(assignee),                    true)
            )
        );

        send(payload, task.getProject().getSlackWebhookUrl());
    }

    //Task Assigned

    public void notifyTaskAssigned(Task task) {
        if (task.getProject() == null || task.getProject().getSlackWebhookUrl() == null) return;

        String assignee = task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : "Unassigned";
        String priority = task.getPriority()   != null ? task.getPriority().name()          : "N/A";
        String dueDate  = task.getDueDate()    != null ? task.getDueDate().toString()        : "Not set";
        String status   = task.getStatus()     != null ? task.getStatus().name()             : "N/A";

        String payload = attachment(
            "#0052cc",
            "Task Assigned",
            "A task has been assigned in *" + e(task.getProject().getName()) + "*",
            e(task.getTitle()),
            fields(
                field("Project",     e(task.getProject().getName()), true),
                field("Assigned To", e(assignee),                    true),
                field("Priority",    priorityLabel(priority),        true),
                field("Status",      e(status),                      true),
                field("Due Date",    e(dueDate),                     true)
            )
        );

        send(payload, task.getProject().getSlackWebhookUrl());
    }

    //Project Created

    public void notifyProjectCreated(Project project) {
        if (project.getSlackWebhookUrl() == null || project.getSlackWebhookUrl().isBlank()) return;

        String creator   = project.getCreatedBy()   != null ? project.getCreatedBy().getUsername()  : "Unknown";
        String desc      = project.getDescription() != null && !project.getDescription().isBlank()
                           ? project.getDescription() : "No description";
        String startDate = project.getStartDate() != null ? project.getStartDate().toString() : "N/A";
        String endDate   = project.getEndDate()   != null ? project.getEndDate().toString()   : "N/A";

        String payload = attachment(
            "#7c3aed",
            "New Project Created",
            e(desc),
            e(project.getName()),
            fields(
                field("Created By",  e(creator),   true),
                field("Start Date",  e(startDate), true),
                field("End Date",    e(endDate),   true)
            )
        );

        send(payload, project.getSlackWebhookUrl());
    }

    //Attachment Builder

    private String attachment(String color, String pretext, String text, String title, String fields) {
        long ts = System.currentTimeMillis() / 1000;
        return "{"
            + "\"attachments\": [{"
            + "\"color\": \"" + color + "\","
            + "\"pretext\": \"" + pretext + "\","
            + "\"title\": \"" + title + "\","
            + "\"text\": \"" + text + "\","
            + "\"fields\": [" + fields + "],"
            + "\"footer\": \"TaskManagement App\","
            + "\"ts\": " + ts + ","
            + "\"mrkdwn_in\": [\"text\", \"pretext\", \"fields\"]"
            + "}]}";
    }

    private String fields(String... fieldItems) {
        return String.join(",", fieldItems);
    }

    private String field(String title, String value, boolean isShort) {
        return "{\"title\": \"" + title + "\", \"value\": \"" + value + "\", \"short\": " + isShort + "}";
    }

    //Helpers
    private String e(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", " ")
                    .replace("\r", "");
    }

    private String priorityLabel(String priority) {
        if (priority == null) return "N/A";
        return switch (priority.toUpperCase()) {
            case "CRITICAL" -> "🔴 Critical";
            case "HIGH"     -> "🟠 High";
            case "MEDIUM"   -> "🟡 Medium";
            case "LOW"      -> "🟢 Low";
            default         -> priority;
        };
    }
}
