package com.taskmanagement.controller;

import com.taskmanagement.dto.InviteDto;
import com.taskmanagement.dto.TeamDto;
import com.taskmanagement.dto.TeamMemberDto;
import com.taskmanagement.model.ProjectInvite;
import com.taskmanagement.model.Team;
import com.taskmanagement.model.TeamMember;
import com.taskmanagement.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody TeamDto dto) {
        return ResponseEntity.ok(teamService.createTeam(dto));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamMember> addMember(@PathVariable Long teamId, @RequestBody TeamMemberDto dto) {
        dto.setTeamId(teamId);
        return ResponseEntity.ok(teamService.addMember(dto));
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<Page<TeamMember>> getMembersByTeam(
            @PathVariable Long teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.getMembersByTeam(teamId, page, size));
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<String> removeMember(@PathVariable Long memberId) {
        teamService.removeMember(memberId);
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
    public ResponseEntity<Page<ProjectInvite>> getPendingInvites(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.getPendingInvites(projectId, page, size));
    }

    @GetMapping("/invites/user")
    public ResponseEntity<Page<ProjectInvite>> getInvitesByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(teamService.getInvitesByEmail(email, page, size));
    }
}
