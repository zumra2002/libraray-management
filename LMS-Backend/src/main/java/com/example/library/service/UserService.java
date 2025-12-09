package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.enums.Role;
import com.example.library.DTO.RegisterRequest;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new user
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        
        
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        
        user.setIsBlacklisted(false);
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return userRepository.save(user);
    }

    // Update user - ROLE CANNOT BE CHANGED
    public User updateUser(Long id, RegisterRequest updateRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update only allowed fields
        if (updateRequest.getFirstName() != null) {
            existingUser.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            existingUser.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getAddress() != null) {
            existingUser.setAddress(updateRequest.getAddress());
        }

        // Update password only if provided
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        // Role is NOT updated
        return userRepository.save(existingUser);
    }

    // Find user by ID
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Delete user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Update user role - LIBRARIAN only (accepts Role enum)
    public User updateUserRole(Long id, Role newRole) {
        User user = findById(id);
        user.setRole(newRole);
        return userRepository.save(user);
    }

    // Blacklist/unblacklist user
    public User toggleBlacklist(Long id) {
        User user = findById(id);
        user.setIsBlacklisted(!user.getIsBlacklisted());
        return userRepository.save(user);
    }
}