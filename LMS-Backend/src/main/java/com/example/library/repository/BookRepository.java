package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);
    
    // Check if book exists by ISBN
    boolean existsByIsbn(String isbn);
    
    // Find books by category
    List<Book> findByCategoryId(Long categoryId);
    
    // Find books by status
    List<Book> findByStatus(BookStatus status);
    
    // Find books by author
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    // Find books by title (partial match)
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    // Find books by genre
    List<Book> findByGenreContainingIgnoreCase(String genre);
    
    // Find available books
    List<Book> findByStatusAndCategoryId(BookStatus status, Long categoryId);
}