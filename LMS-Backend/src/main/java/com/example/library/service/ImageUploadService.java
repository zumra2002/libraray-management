package com.example.library.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Interface for image uploading, validation, and deletion services.
 */
public interface ImageUploadService {

    /**
     * Uploads an image file and returns its storage path/URL.
     * @param imageFile The image file to upload.
     * @return The path or URL where the image is stored.
     * @throws IOException If an error occurs during file transfer.
     */
    String uploadImage(MultipartFile imageFile) throws IOException;

    /**
     * Deletes an image based on its storage path/URL.
     * @param imagePath The path or URL of the image to delete.
     */
    void deleteImage(String imagePath);

    /**
     * Validates if the image size is acceptable (e.g., less than 5MB).
     * @param imageFile The image file to validate.
     * @return True if the size is valid, false otherwise.
     */
    boolean validateImageSize(MultipartFile imageFile);
}