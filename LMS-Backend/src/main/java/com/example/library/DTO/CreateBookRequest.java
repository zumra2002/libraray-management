package com.example.library.DTO;

import com.example.library.enums.BookStatus;

// ============================================
// 1. CREATE BOOK REQUEST DTO
// ============================================
public class CreateBookRequest {
    private String title;
    private String author;
    private String isbn;
    private String language;
    private Long categoryId;  // Category ID instead of full object
    private String genre;
    private String imageUrl;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

// ============================================
// 2. UPDATE BOOK REQUEST DTO
// ============================================
class UpdateBookRequest {
    private String title;
    private String author;
    private String isbn;
    private String language;
    private Long categoryId;
    private String genre;
    private String imageUrl;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

// ============================================
// 3. UPDATE BOOK STATUS DTO (For PATCH)
// ============================================
class UpdateBookStatusRequest {
    private String status;  // Accepts "AVAILABLE" or "RESERVED"

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}