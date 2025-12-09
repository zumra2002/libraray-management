package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.enums.BookStatus;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    // Find all books
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    // Find book by ID
    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    // Find book by ISBN
    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));
    }

    // Create book (without image)
    public Book createBook(Book book) {
        // Check if ISBN already exists
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        // Set default status if not provided
        if (book.getStatus() == null) {
            book.setStatus(BookStatus.AVAILABLE);
        }

        return bookRepository.save(book);
    }

    // Create book with image upload
    public Book createBookWithImage(Book book, MultipartFile imageFile) throws IOException {
        // Check ISBN
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        // Upload image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            if (!imageUploadService.validateImageSize(imageFile)) {
                throw new RuntimeException("Image size must be less than 5MB");
            }
            String imagePath = imageUploadService.uploadImage(imageFile);
            book.setImageUrl(imagePath);
        }

        // Set default status
        if (book.getStatus() == null) {
            book.setStatus(BookStatus.AVAILABLE);
        }

        return bookRepository.save(book);
    }

    // Update book
    public Book updateBook(Long id, Book bookDetails) {
        Book existingBook = findBookById(id);

        // Update fields
        if (bookDetails.getTitle() != null) {
            existingBook.setTitle(bookDetails.getTitle());
        }
        if (bookDetails.getAuthor() != null) {
            existingBook.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getIsbn() != null && !bookDetails.getIsbn().equals(existingBook.getIsbn())) {
            // Check if new ISBN already exists
            if (bookRepository.existsByIsbn(bookDetails.getIsbn())) {
                throw new RuntimeException("Book with ISBN " + bookDetails.getIsbn() + " already exists");
            }
            existingBook.setIsbn(bookDetails.getIsbn());
        }
        if (bookDetails.getLanguage() != null) {
            existingBook.setLanguage(bookDetails.getLanguage());
        }
        if (bookDetails.getCategory() != null) {
            existingBook.setCategory(bookDetails.getCategory());
        }
        if (bookDetails.getGenre() != null) {
            existingBook.setGenre(bookDetails.getGenre());
        }
        if (bookDetails.getImageUrl() != null) {
            existingBook.setImageUrl(bookDetails.getImageUrl());
        }

        // Note: Status is NOT updated here - use updateBookStatus() instead

        return bookRepository.save(existingBook);
    }

    // Update book with image
    public Book updateBookWithImage(Long id, Book bookDetails, MultipartFile imageFile) throws IOException {
        Book existingBook = updateBook(id, bookDetails);

        // Update image if new one provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image
            if (existingBook.getImageUrl() != null) {
                imageUploadService.deleteImage(existingBook.getImageUrl());
            }
            // Upload new image
            String imagePath = imageUploadService.uploadImage(imageFile);
            existingBook.setImageUrl(imagePath);
            existingBook = bookRepository.save(existingBook);
        }

        return existingBook;
    }

    // Update book status ONLY (PATCH operation)
    public Book updateBookStatus(Long id, BookStatus newStatus) {
        Book book = findBookById(id);
        book.setStatus(newStatus);
        return bookRepository.save(book);
    }

    // Delete book
    public void deleteBook(Long id) {
        Book book = findBookById(id);

        // Delete image if exists
        if (book.getImageUrl() != null) {
            imageUploadService.deleteImage(book.getImageUrl());
        }

        bookRepository.deleteById(id);
    }

    // Find books by category
    public List<Book> findBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    // Find books by status
    public List<Book> findBooksByStatus(BookStatus status) {
        return bookRepository.findByStatus(status);
    }

    // Find available books
    public List<Book> findAvailableBooks() {
        return bookRepository.findByStatus(BookStatus.AVAILABLE);
    }

    // Search books by title
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // Search books by author
    public List<Book> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    // Search books by genre
    public List<Book> searchBooksByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }

    // Helper method to set category by ID
    public void setCategoryById(Book book, Long categoryId) {
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
            book.setCategory(category);
        }
    }
}