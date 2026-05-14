package com.care.service;

import com.care.dto.ComplaintRequest;
import com.care.dto.ComplaintResponse;
import com.care.dto.DashboardStats;
import com.care.dto.StatusUpdateRequest;
import com.care.entity.Complaint;
import com.care.entity.Complaint.Category;
import com.care.entity.Complaint.Priority;
import com.care.entity.Complaint.Status;
import com.care.entity.User;
import com.care.repository.ComplaintRepository;
import com.care.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    // ---- USER: Submit a new complaint ----
    public ComplaintResponse createComplaint(ComplaintRequest request) {

        // Find the logged-in user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        Complaint complaint = new Complaint();
        complaint.setTitle(request.getTitle());
        complaint.setLocation(request.getLocation());
        complaint.setDescription(request.getDescription());
        complaint.setUser(user);

        // Convert String to Enum (case-insensitive)
        complaint.setCategory(Category.valueOf(request.getCategory().toUpperCase()));
        complaint.setPriority(Priority.valueOf(request.getPriority().toUpperCase()));

        // Status is OPEN by default
        complaint.setStatus(Status.OPEN);

        Complaint saved = complaintRepository.save(complaint);
        return ComplaintResponse.fromEntity(saved);
    }

    // ---- USER: Get all complaints for a specific user ----
    public List<ComplaintResponse> getMyComplaints(Long userId) {
        return complaintRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ComplaintResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // ---- USER/ADMIN: Get a single complaint by ID ----
    public ComplaintResponse getComplaintById(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found."));
        return ComplaintResponse.fromEntity(complaint);
    }

    // ---- ADMIN: Get all complaints ----
    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ComplaintResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // ---- ADMIN: Update complaint status and add response ----
    public ComplaintResponse updateComplaintStatus(Long complaintId, StatusUpdateRequest request) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found."));

        // Update status
        complaint.setStatus(Status.valueOf(request.getStatus().toUpperCase()));

        // Update admin response if provided
        if (request.getAdminResponse() != null && !request.getAdminResponse().isBlank()) {
            complaint.setAdminResponse(request.getAdminResponse());
        }

        Complaint updated = complaintRepository.save(complaint);
        return ComplaintResponse.fromEntity(updated);
    }

    // ---- ADMIN: Get dashboard statistics ----
    public DashboardStats getDashboardStats() {
        long total      = complaintRepository.count();
        long open       = complaintRepository.countByStatus(Status.OPEN);
        long inProgress = complaintRepository.countByStatus(Status.IN_PROGRESS);
        long resolved   = complaintRepository.countByStatus(Status.RESOLVED);

        return new DashboardStats(total, open, inProgress, resolved);
    }

}
