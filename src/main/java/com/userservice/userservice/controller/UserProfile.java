package com.userservice.userservice.controller;

import com.userservice.userservice.entity.User;
import com.userservice.userservice.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Profile", description = "Endpoints for managing user profile")
@RestController
@RequestMapping("/userprofile")
public class UserProfile {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/mydetails")
    @Operation(
            summary = "Get User Details",
            description = "Returns details of the authenticated user"
    )
    @SecurityRequirement(name = "Bearer Authentication") // Indicates that this endpoint requires a JWT token
    public ResponseEntity<?> getUserDetails() {
        // Get the currently authenticated user's username
        String username = getCurrentUsername();

        // Fetch the user details from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return the user details
        return ResponseEntity.ok(user);
    }

    private String getCurrentUsername() {
        // Retrieve the currently authenticated user's details
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}