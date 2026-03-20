package com.taskmanagement.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanagement.dto.LoginDto;
import com.taskmanagement.dto.RegisterDto;
import com.taskmanagement.model.User;
import com.taskmanagement.service.TeamService;
import com.taskmanagement.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterDto register,
            @RequestParam(required = false) String token) {
        User user = userService.register(register);
        if (token != null) {
            teamService.acceptInvite(token, user.getId());
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        Map<String, Object> response = userService.loginAndGetToken(login);
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/invite-info")
    public ResponseEntity<?> getInviteInfo(@RequestParam String token) {
        return ResponseEntity.ok(teamService.getInviteInfo(token));
    }
}
