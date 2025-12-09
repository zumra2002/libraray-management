package com.example.library.controller;

import com.example.library.entity.Category;
import com.example.library.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) { 
        this.service = service; 
    }

    // View All Categories - Only LIBRARIAN can access
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<Category>> getAllCategories() { 
        List<Category> categories = service.findAll();
        return ResponseEntity.ok(categories);
    }

    // Create Category - Only LIBRARIAN can access
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        try {
            Category savedCategory = service.save(category);
            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update Category - Only LIBRARIAN can access
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id, 
            @RequestBody Category category) {
        
        return service.findById(id)
                .map(existingCategory -> {
                    category.setId(id);
                    Category updatedCategory = service.save(category);
                    return ResponseEntity.ok(updatedCategory);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Category - Only LIBRARIAN can access
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return service.findById(id)
                .map(category -> {
                    service.deleteById(id);
                    return ResponseEntity.ok("Category deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}