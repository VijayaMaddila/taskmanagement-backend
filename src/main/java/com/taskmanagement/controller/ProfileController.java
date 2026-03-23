package com.taskmanagement.controller;

import com.taskmanagement.dto.ChangePasswordDto;
import com.taskmanagement.dto.ProfileDto;
import com.taskmanagement.model.User;
import com.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    //get current user's profile
    @GetMapping
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(userService.getProfile(email));
    }

    //update username and email
    @PutMapping
    public ResponseEntity<User> updateProfile(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody ProfileDto dto) {
        return ResponseEntity.ok(userService.updateProfile(email, dto));
    }

    // change-password 
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(email, dto);
        return ResponseEntity.ok("Password changed successfully");
    }
}
