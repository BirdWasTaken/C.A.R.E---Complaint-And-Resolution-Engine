-- ============================================================
--  C.A.R.E – Database Schema (MySQL)
--  File: schema.sql
--
--  NOTE: You do NOT need to run this manually.
--  Hibernate with "ddl-auto=update" will auto-create these tables.
--  This file is provided for reference and documentation.
-- ============================================================

CREATE DATABASE IF NOT EXISTS care_db;
USE care_db;

-- ============================================================
--  TABLE: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    full_name   VARCHAR(100)    NOT NULL,
    student_id  VARCHAR(50)     NOT NULL UNIQUE,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    phone       VARCHAR(20)     NOT NULL,
    password    VARCHAR(255)    NOT NULL,     -- BCrypt hashed
    role        ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at  DATETIME        NOT NULL,
    PRIMARY KEY (id)
);

-- ============================================================
--  TABLE: complaints
-- ============================================================
CREATE TABLE IF NOT EXISTS complaints (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    title           VARCHAR(255)    NOT NULL,
    category        ENUM('INFRASTRUCTURE','FACILITIES','ACADEMIC','ADMINISTRATION','OTHER') NOT NULL,
    location        VARCHAR(255)    NOT NULL,
    priority        ENUM('LOW','MEDIUM','HIGH','URGENT') NOT NULL,
    description     TEXT            NOT NULL,
    status          ENUM('OPEN','IN_PROGRESS','RESOLVED') NOT NULL DEFAULT 'OPEN',
    admin_response  TEXT,                             -- NULL until admin responds
    created_at      DATETIME        NOT NULL,
    updated_at      DATETIME,
    user_id         BIGINT          NOT NULL,          -- Foreign key → users.id
    PRIMARY KEY (id),
    CONSTRAINT fk_complaint_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ============================================================
--  SEED DATA: Default Admin Account
--  Email:    admin@care.com
--  Password: admin123   (BCrypt hash stored below)
-- ============================================================
INSERT INTO users (full_name, student_id, email, phone, password, role, created_at)
VALUES (
    'Administrator',
    'ADMIN001',
    'admin@care.com',
    '0000000000',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',  -- admin123
    'ADMIN',
    NOW()
)
ON DUPLICATE KEY UPDATE id = id;  -- Skip insert if already exists

-- ============================================================
--  USEFUL QUERIES FOR TESTING
-- ============================================================

-- View all users (passwords are hashed, safe to display)
-- SELECT id, full_name, student_id, email, role, created_at FROM users;

-- View all complaints with student names
-- SELECT c.id, c.title, u.full_name, c.category, c.priority, c.status, c.created_at
-- FROM complaints c JOIN users u ON c.user_id = u.id
-- ORDER BY c.created_at DESC;

-- Count complaints by status
-- SELECT status, COUNT(*) as total FROM complaints GROUP BY status;
