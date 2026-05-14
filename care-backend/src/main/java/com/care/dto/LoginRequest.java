package com.care.dto;

import lombok.Data;

// Data sent from the Login form to the backend
@Data
public class LoginRequest {

    private String email;
    private String password;

}
