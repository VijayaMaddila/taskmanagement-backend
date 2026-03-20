package com.taskmanagement.controller;

import com.taskmanagement.dto.InviteDto;
import com.taskmanagement.dto.TeamDto;
import com.taskmanagement.model.ProjectInvite;
import com.taskmanagement.model.Team;
import com.taskmanagement.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    
    @PostMapping
    public ResponseEntity<Team> addMember(@RequestBody TeamDto dto) {
        return ResponseEntity.ok(teamService.addMember(dto));
    }


    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Team>> getMembersByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.getMembersByProject(projectId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeMember(@PathVariable Long id) {
        teamService.removeMember(id);
        return ResponseEntity.ok("Member removed successfully");
    }

    
    @PostMapping("/invite/send")
    public ResponseEntity<ProjectInvite> sendInvite(@RequestBody InviteDto dto) {
        return ResponseEntity.ok(teamService.sendInvite(dto));
    }

    @GetMapping("/invite/info")
    public ResponseEntity<Map<String, String>> getInviteInfo(@RequestParam String token) {
        return ResponseEntity.ok(teamService.getInviteInfo(token));
    }

    
    @PostMapping("/invite/accept")
    public ResponseEntity<String> acceptInvite(@RequestParam String token, @RequestParam Long userId) {
        teamService.acceptInvite(token, userId);
        return ResponseEntity.ok("Invite accepted");
    }

    
    @PostMapping("/invite/decline")
    public ResponseEntity<String> declineInvite(@RequestParam String token) {
        teamService.declineInvite(token);
        return ResponseEntity.ok("Invite declined");
    }

    
    @GetMapping("/invites/project/{projectId}")
    public ResponseEntity<List<ProjectInvite>> getPendingInvites(@PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.getPendingInvites(projectId));
    }

    
    @GetMapping("/invites/user")
    public ResponseEntity<List<ProjectInvite>> getInvitesByEmail(@RequestParam String email) {
        return ResponseEntity.ok(teamService.getInvitesByEmail(email));
    }
}
