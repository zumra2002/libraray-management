package com.example.library.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

// NOTE: You must have an 'ImageUploadService' interface for this to compile.

@Service
public class FileSystemImageUploadService implements ImageUploadService {

    // Define the maximum allowed file size (5MB in bytes)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // This field will hold the root Path object for the upload directory
    private final Path rootLocation; 

    /**
     * Constructor injection: Spring will provide the value for 'uploadDir'
     * before this constructor is executed, ensuring it is not null.
     * * @param uploadDir The path string injected from application properties.
     * @throws IOException If the necessary directories cannot be created.
     */
    public FileSystemImageUploadService(
        @Value("${image.upload.dir:./uploads/images}") String uploadDir) throws IOException {
        
        // 1. Initialize the Path object with the injected value
        this.rootLocation = Paths.get(uploadDir); 

        // 2. Initialize the upload directory
        if (Files.notExists(this.rootLocation)) {
            try {
                // Creates all non-existent directories in the path
                Files.createDirectories(this.rootLocation);
            } catch (IOException e) {
                // Throwing an exception here stops the application startup, 
                // which is correct if the upload directory is critical.
                throw new IOException("Could not initialize storage directory: " + uploadDir, e);
            }
        }
    }

    @Override
    public String uploadImage(MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }
        
        // Validation check (optional, but good practice)
        if (!validateImageSize(imageFile)) {
             throw new IllegalArgumentException("File size exceeds the allowed limit of 5MB.");
        }

        // Generate a unique filename
        String originalFilename = imageFile.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String uniqueFileName = UUID.randomUUID().toString() + extension;
        
        // Resolve the full file path relative to the root location
        Path filePath = this.rootLocation.resolve(uniqueFileName);

        // Save the file to the target location
        // Using StandardCopyOption.REPLACE_EXISTING is often safer, but basic copy works too.
        Files.copy(imageFile.getInputStream(), filePath);

        // Return the unique file name or the full path string (depending on your need)
        // Returning only the unique file name is often preferred for database storage:
        // return uniqueFileName; 
        
        // Returning the full path string as in your original logic:
        return filePath.toString(); 
    }

    @Override
    public void deleteImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }
        try {
            // NOTE: If you store only the file name (uniqueFileName) in the DB, 
            // you must resolve the path against rootLocation here:
            // Path fileToDelete = this.rootLocation.resolve(imagePath);
            
            // If you store the full path string:
            Path fileToDelete = Paths.get(imagePath);
            
            if (Files.exists(fileToDelete)) {
                Files.delete(fileToDelete);
            }
        } catch (Exception e) {
            System.err.println("Error deleting image file: " + imagePath + ". Error: " + e.getMessage());
            // Log the error for production, but don't re-throw.
        }
    }

    @Override
    public boolean validateImageSize(MultipartFile imageFile) {
        return imageFile.getSize() <= MAX_FILE_SIZE;
    }
}