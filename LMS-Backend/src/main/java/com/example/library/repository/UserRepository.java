package com.example.library.repository;

import org.springframework.stereotype.Repository;
import com.example.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // Check for existence by Email
    boolean existsByEmail(String email);
}
