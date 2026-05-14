package com.care.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Stats shown on the Admin Dashboard (total, open, in progress, resolved)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {

    private long totalComplaints;
    private long openComplaints;
    private long inProgressComplaints;
    private long resolvedComplaints;

}
