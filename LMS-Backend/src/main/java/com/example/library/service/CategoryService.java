package com.example.library.service;

import com.example.library.entity.Category;
import com.example.library.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) { 
        this.repo = repo; 
    }

    // Find all categories
    public List<Category> findAll() { 
        return repo.findAll(); 
    }

    // Find category by ID
    public Optional<Category> findById(Long id) { 
        return repo.findById(id); 
    }

    // Save/Update category
    public Category save(Category category) { 
        return repo.save(category); 
    }

    // Delete category by ID - NEW METHOD
    public void deleteById(Long id) { 
        repo.deleteById(id); 
    }

    // Optional: Check if category exists
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }

    // Optional: Check if category name already exists (to prevent duplicates)
    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }
}