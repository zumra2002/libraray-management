package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.enums.BookStatus;
import com.example.library.DTO.CreateBookRequest;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // ============================================
    // PUBLIC/AUTHENTICATED ENDPOINTS (Both USER and LIBRARIAN)
    // ============================================

    // Get all books - Both USER and LIBRARIAN can view
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    // Get book by ID - Both USER and LIBRARIAN can view
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.findBookById(id);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get books by category - Both can view
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Long categoryId) {
        List<Book> books = bookService.findBooksByCategory(categoryId);
        return ResponseEntity.ok(books);
    }

    // Get available books - Both can view
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookService.findAvailableBooks();
        return ResponseEntity.ok(books);
    }

    // Search books by title - Both can search
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    // Search books by author - Both can search
    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchByAuthor(@RequestParam String author) {
        List<Book> books = bookService.searchBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    // Search books by genre - Both can search
    @GetMapping("/search/genre")
    public ResponseEntity<List<Book>> searchByGenre(@RequestParam String genre) {
        List<Book> books = bookService.searchBooksByGenre(genre);
        return ResponseEntity.ok(books);
    }

    // ============================================
    // LIBRARIAN ONLY ENDPOINTS
    // ============================================

    // Create book without image - LIBRARIAN ONLY
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> createBook(@RequestBody CreateBookRequest request) {
        try {
            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setIsbn(request.getIsbn());
            book.setLanguage(request.getLanguage());
            book.setGenre(request.getGenre());
            book.setImageUrl(request.getImageUrl());

            // Set category if provided
            if (request.getCategoryId() != null) {
                bookService.setCategoryById(book, request.getCategoryId());
            }

            Book savedBook = bookService.createBook(book);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Create book with image upload - LIBRARIAN ONLY
    @PostMapping(value = "/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> createBookWithImage(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("isbn") String isbn,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setLanguage(language);
            book.setGenre(genre);

            // Set category
            if (categoryId != null) {
                bookService.setCategoryById(book, categoryId);
            }

            Book savedBook = bookService.createBookWithImage(book, image);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update book - LIBRARIAN ONLY
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateBook(
            @PathVariable Long id,
            @RequestBody CreateBookRequest request) {
        
        try {
            Book bookDetails = new Book();
            bookDetails.setTitle(request.getTitle());
            bookDetails.setAuthor(request.getAuthor());
            bookDetails.setIsbn(request.getIsbn());
            bookDetails.setLanguage(request.getLanguage());
            bookDetails.setGenre(request.getGenre());
            bookDetails.setImageUrl(request.getImageUrl());

            // Set category
            if (request.getCategoryId() != null) {
                bookService.setCategoryById(bookDetails, request.getCategoryId());
            }

            Book updatedBook = bookService.updateBook(id, bookDetails);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update book with image - LIBRARIAN ONLY
    @PutMapping(value = "/{id}/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateBookWithImage(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("isbn") String isbn,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            Book bookDetails = new Book();
            bookDetails.setTitle(title);
            bookDetails.setAuthor(author);
            bookDetails.setIsbn(isbn);
            bookDetails.setLanguage(language);
            bookDetails.setGenre(genre);

            if (categoryId != null) {
                bookService.setCategoryById(bookDetails, categoryId);
            }

            Book updatedBook = bookService.updateBookWithImage(id, bookDetails, image);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Update book status ONLY (PATCH) - LIBRARIAN ONLY
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateBookStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {
        
        try {
            BookStatus newStatus = BookStatus.fromString(request.getStatus());
            Book updatedBook = bookService.updateBookStatus(id, newStatus);
            return ResponseEntity.ok(updatedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete book - LIBRARIAN ONLY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ============================================
    // INNER CLASSES FOR REQUEST BODIES
    // ============================================

    public static class UpdateStatusRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}