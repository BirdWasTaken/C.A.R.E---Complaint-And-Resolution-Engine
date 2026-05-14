package com.care.repository;

import com.care.entity.Complaint;
import com.care.entity.Complaint.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Get all complaints for a specific user (My Complaints page)
    List<Complaint> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Get complaints by status (Admin filter)
    List<Complaint> findByStatusOrderByCreatedAtDesc(Status status);

    // Get all complaints sorted by newest first (Admin dashboard)
    List<Complaint> findAllByOrderByCreatedAtDesc();

    // Count complaints by status (Admin stats)
    long countByStatus(Status status);

    // Future: search by title or description (can be used later)
    @Query("SELECT c FROM Complaint c WHERE " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Complaint> searchByKeyword(String keyword);

}
