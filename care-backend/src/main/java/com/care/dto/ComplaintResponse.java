package com.care.dto;

import com.care.entity.Complaint;
import lombok.Data;

import java.time.LocalDateTime;

// Data returned to the frontend when displaying a complaint
@Data
public class ComplaintResponse {

    private Long id;
    private String title;
    private String category;
    private String location;
    private String priority;
    private String description;
    private String status;
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Basic user info (no password!)
    private Long userId;
    private String userName;
    private String studentId;

    // Static factory method to convert entity to DTO
    public static ComplaintResponse fromEntity(Complaint complaint) {
        ComplaintResponse dto = new ComplaintResponse();
        dto.setId(complaint.getId());
        dto.setTitle(complaint.getTitle());
        dto.setCategory(complaint.getCategory().name());
        dto.setLocation(complaint.getLocation());
        dto.setPriority(complaint.getPriority().name());
        dto.setDescription(complaint.getDescription());
        dto.setStatus(complaint.getStatus().name());
        dto.setAdminResponse(complaint.getAdminResponse());
        dto.setCreatedAt(complaint.getCreatedAt());
        dto.setUpdatedAt(complaint.getUpdatedAt());
        dto.setUserId(complaint.getUser().getId());
        dto.setUserName(complaint.getUser().getFullName());
        dto.setStudentId(complaint.getUser().getStudentId());
        return dto;
    }

}
