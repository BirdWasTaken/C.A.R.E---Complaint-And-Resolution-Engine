package com.care.service;

import com.care.dto.LoginRequest;
import com.care.dto.LoginResponse;
import com.care.dto.SignupRequest;
import com.care.entity.User;
import com.care.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // BCrypt encoder for hashing passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ---- SIGNUP ----
    public User signup(SignupRequest request) {

        // Check if email is already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }

        // Check if student ID is already used
        if (userRepository.existsByStudentId(request.getStudentId())) {
            throw new RuntimeException("Student ID is already registered.");
        }

        // Create new user
        User user = new User();
        user.setFullName(request.getFullName());
        user.setStudentId(request.getStudentId());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Hash the password before saving – NEVER store plain text passwords
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Default role is USER
        user.setRole(User.Role.USER);

        return userRepository.save(user);
    }

    // ---- LOGIN ----
    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        // Compare entered password with stored hashed password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        // Return user info (no password!) to the frontend
        return new LoginResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getStudentId(),
                user.getRole(),
                "Login successful"
        );
    }

}
