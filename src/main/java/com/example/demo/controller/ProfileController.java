package com.example.demo.controller;

import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        try {
            
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            profileService.updateProfile(email, request);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
