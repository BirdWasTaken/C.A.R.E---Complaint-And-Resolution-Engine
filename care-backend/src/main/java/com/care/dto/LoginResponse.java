package com.care.dto;

import com.care.entity.User.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

// Data returned to the frontend after successful login
@Data
@AllArgsConstructor
public class LoginResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String studentId;
    private Role role;
    private String message;

}
