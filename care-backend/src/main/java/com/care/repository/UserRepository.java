package com.care.repository;

import com.care.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login)
    Optional<User> findByEmail(String email);

    // Check if email already exists (used during signup)
    boolean existsByEmail(String email);

    // Check if student ID already exists
    boolean existsByStudentId(String studentId);

}
