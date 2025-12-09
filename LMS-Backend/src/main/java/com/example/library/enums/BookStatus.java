package com.example.library.enums;

public enum BookStatus {
    AVAILABLE,
    RESERVED;
    
    // Convert string to enum safely
    public static BookStatus fromString(String statusStr) {
        if (statusStr == null) {
            return AVAILABLE; // Default status
        }
        try {
            return BookStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusStr + ". Must be AVAILABLE or RESERVED");
        }
    }
}