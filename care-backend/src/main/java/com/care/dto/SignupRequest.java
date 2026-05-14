package com.care.dto;

import lombok.Data;

// Data sent from the Register form to the backend
@Data
public class SignupRequest {

    private String fullName;
    private String studentId;
    private String email;
    private String phone;
    private String password;

}
