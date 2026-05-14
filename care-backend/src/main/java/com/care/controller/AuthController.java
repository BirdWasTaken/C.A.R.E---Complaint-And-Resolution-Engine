package com.care.controller;

import com.care.dto.ApiResponse;
import com.care.dto.LoginRequest;
import com.care.dto.LoginResponse;
import com.care.dto.SignupRequest;
import com.care.entity.User;
import com.care.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")   // Allow requests from the frontend
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * POST /api/auth/signup
     * Register a new user.
     *
     * Request Body:
     * {
     *   "fullName":  "John Doe",
     *   "studentId": "STU001",
     *   "email":     "john@example.com",
     *   "phone":     "9876543210",
     *   "password":  "secret123"
     * }
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignupRequest request) {
        try {
            User user = userService.signup(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Account created successfully! Please login.", user.getEmail()));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login
     * Login for both USER and ADMIN.
     *
     * Request Body:
     * {
     *   "email":    "john@example.com",
     *   "password": "secret123"
     * }
     *
     * Response:
     * {
     *   "success": true,
     *   "message": "Login successful",
     *   "data": {
     *     "userId": 1,
     *     "fullName": "John Doe",
     *     "email": "john@example.com",
     *     "studentId": "STU001",
     *     "role": "USER"
     *   }
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

}
