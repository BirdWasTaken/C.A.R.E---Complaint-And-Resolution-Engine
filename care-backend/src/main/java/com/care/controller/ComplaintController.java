package com.care.controller;

import com.care.dto.ApiResponse;
import com.care.dto.ComplaintRequest;
import com.care.dto.ComplaintResponse;
import com.care.dto.DashboardStats;
import com.care.dto.StatusUpdateRequest;
import com.care.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")   // Allow requests from the frontend
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    // =============================================
    //  USER ENDPOINTS
    // =============================================

    /**
     * POST /api/complaints
     * Submit a new complaint.
     *
     * Request Body:
     * {
     *   "userId":      1,
     *   "title":       "AC Not Working",
     *   "category":    "infrastructure",
     *   "location":    "Main Library",
     *   "priority":    "high",
     *   "description": "The AC has been broken for a week."
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ComplaintResponse>> createComplaint(
            @RequestBody ComplaintRequest request) {
        try {
            ComplaintResponse response = complaintService.createComplaint(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Complaint submitted successfully.", response));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/complaints/user/{userId}
     * Get all complaints submitted by a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getMyComplaints(
            @PathVariable Long userId) {
        try {
            List<ComplaintResponse> complaints = complaintService.getMyComplaints(userId);
            return ResponseEntity.ok(ApiResponse.success("Complaints fetched successfully.", complaints));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/complaints/{id}
     * Get details of a single complaint by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplaintResponse>> getComplaintById(
            @PathVariable Long id) {
        try {
            ComplaintResponse complaint = complaintService.getComplaintById(id);
            return ResponseEntity.ok(ApiResponse.success("Complaint found.", complaint));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // =============================================
    //  ADMIN ENDPOINTS
    // =============================================

    /**
     * GET /api/complaints/admin/all
     * Get ALL complaints (Admin only).
     */
    @GetMapping("/admin/all")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getAllComplaints() {
        List<ComplaintResponse> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(ApiResponse.success("All complaints fetched.", complaints));
    }

    /**
     * PUT /api/complaints/admin/{id}/status
     * Update a complaint's status and add an admin response.
     *
     * Request Body:
     * {
     *   "status":        "IN_PROGRESS",
     *   "adminResponse": "We are looking into this issue."
     * }
     */
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<ApiResponse<ComplaintResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        try {
            ComplaintResponse updated = complaintService.updateComplaintStatus(id, request);
            return ResponseEntity.ok(ApiResponse.success("Complaint status updated.", updated));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/complaints/admin/stats
     * Get dashboard statistics for the admin.
     *
     * Response:
     * {
     *   "totalComplaints": 45,
     *   "openComplaints": 12,
     *   "inProgressComplaints": 18,
     *   "resolvedComplaints": 15
     * }
     */
    @GetMapping("/admin/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        DashboardStats stats = complaintService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats fetched.", stats));
    }

}
