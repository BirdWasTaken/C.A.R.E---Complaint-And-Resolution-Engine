package com.care.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Full name of the user
    @Column(nullable = false)
    private String fullName;

    // Student ID (unique per student)
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    // Email – used for login
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    // BCrypt hashed password – never stored as plain text
    @Column(nullable = false)
    private String password;

    // Role: USER or ADMIN
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // One user can have many complaints
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Complaint> complaints;

    // Automatically set createdAt before first save
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ---- Role Enum ----
    public enum Role {
        USER,
        ADMIN
    }

}
