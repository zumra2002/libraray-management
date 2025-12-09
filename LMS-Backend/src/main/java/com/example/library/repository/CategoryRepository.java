package com.example.library.repository;

import com.example.library.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Check if category with this name already exists
    boolean existsByName(String name);
    
    // Optional: Find category by name
    Category findByName(String name);
}