package com.care.dto;

import lombok.Data;

// Data sent when a user submits a new complaint
@Data
public class ComplaintRequest {

    // The logged-in user's ID (stored in browser after login)
    private Long userId;

    private String title;
    private String category;   // "infrastructure", "facilities", etc.
    private String location;
    private String priority;   // "low", "medium", "high", "urgent"
    private String description;

}
