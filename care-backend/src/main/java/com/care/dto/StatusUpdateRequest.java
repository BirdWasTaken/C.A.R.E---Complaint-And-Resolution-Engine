package com.care.dto;

import lombok.Data;

// Data sent by admin when updating a complaint status
@Data
public class StatusUpdateRequest {

    // "OPEN", "IN_PROGRESS", or "RESOLVED"
    private String status;

    // Optional admin notes / response message
    private String adminResponse;

}
