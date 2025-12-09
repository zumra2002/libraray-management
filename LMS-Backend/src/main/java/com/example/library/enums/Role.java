package com.example.library.enums;

public enum Role {
    USER,
    LIBRARIAN;
    
    // Optional: Method to convert string to enum safely
    public static Role fromString(String roleStr) {
        if (roleStr == null) {
            return USER; // Default role
        }
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleStr + ". Must be USER or LIBRARIAN");
        }
    }
}