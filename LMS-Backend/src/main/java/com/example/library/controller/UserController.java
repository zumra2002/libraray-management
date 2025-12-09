package com.example.library.controller;

import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.DTO.RegisterRequest;
import com.example.library.DTO.UpdateUserRequest;
import com.example.library.service.UserService;
import com.example.library.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.library.DTO.LoginRequest;
import com.example.library.DTO.JwtAuthResponse;
import com.example.library.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/auth") 
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User savedUser = userService.registerUser(registerRequest);
            
            // Send welcome email
            try {
                emailService.sendWelcomeEmail(
                    savedUser.getEmail(), 
                    savedUser.getFirstName()
                );
            } catch (MessagingException e) {
                System.err.println("Failed to send welcome email: " + e.getMessage());
            }
            
            savedUser.setPassword(null); 
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
            
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), 
                        loginDto.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    // Get User Profile
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
    
    // Update User Profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserDetails(
            @PathVariable Long id, 
            @RequestBody UpdateUserRequest updateRequest) {
        try {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName(updateRequest.getFirstName());
            registerRequest.setLastName(updateRequest.getLastName());
            registerRequest.setPhoneNumber(updateRequest.getPhoneNumber());
            registerRequest.setAddress(updateRequest.getAddress());
            registerRequest.setPassword(updateRequest.getPassword());
            
            User updatedUser = userService.updateUser(id, registerRequest);
            updatedUser.setPassword(null);
            
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Update User Role - LIBRARIAN only
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest roleRequest) {
        try {
            // CHANGED: Convert string to enum with validation
            Role newRole = Role.fromString(roleRequest.getRole());
            User updatedUser = userService.updateUserRole(id, newRole);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Toggle Blacklist - LIBRARIAN only
    @PutMapping("/{id}/blacklist")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> toggleBlacklist(@PathVariable Long id) {
        try {
            User user = userService.toggleBlacklist(id);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Inner class for role update
    public static class UpdateRoleRequest {
        private String role;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}