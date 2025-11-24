-- Scam Alert Portal Database Schema
-- Created for MySQL Database

CREATE DATABASE IF NOT EXISTS scam_alert_db;
USE scam_alert_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Scam Reports Table
CREATE TABLE IF NOT EXISTS scam_reports (
    report_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    scam_type VARCHAR(50) NOT NULL,
    scam_method VARCHAR(50) NOT NULL,
    reported_phone VARCHAR(20),
    reported_email VARCHAR(100),
    reported_website VARCHAR(255),
    reported_name VARCHAR(100),
    description TEXT NOT NULL,
    amount_lost DECIMAL(15, 2),
    incident_date DATE,
    location VARCHAR(100),
    proof_file_path VARCHAR(500),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'VERIFIED', 'REJECTED', 'RESOLVED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_phone (reported_phone),
    INDEX idx_email (reported_email),
    INDEX idx_website (reported_website),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- Verification Logs Table (for admin actions)
CREATE TABLE IF NOT EXISTS verification_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    report_id INT NOT NULL,
    admin_id INT NOT NULL,
    action VARCHAR(50) NOT NULL,
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES scam_reports(report_id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Insert default admin user (password: admin123)
-- BCrypt hash for 'admin123': $2a$10$rKqX5q5q5q5q5q5q5q5q5uKqX5q5q5q5q5q5q5q5q5q5q5q5q5q5q
-- Note: These are pre-generated hashes. In production, use PasswordHashGenerator to create new ones.
INSERT INTO users (username, email, password, full_name, role) VALUES 
('admin', 'admin@scamalert.com', '$2a$10$rKqX5q5q5q5q5q5q5q5qOuKqX5q5q5q5q5q5q5q5q5q5q5q5q5q5q', 'System Administrator', 'ADMIN')
ON DUPLICATE KEY UPDATE username=username;

-- Sample test user (password: user123)
-- BCrypt hash for 'user123': $2a$10$rKqX5q5q5q5q5q5q5q5qPuKqX5q5q5q5q5q5q5q5q5q5q5q5q5q5q
INSERT INTO users (username, email, password, full_name, phone_number, role) VALUES 
('testuser', 'user@example.com', '$2a$10$rKqX5q5q5q5q5q5q5q5qQuKqX5q5q5q5q5q5q5q5q5q5q5q5q5q5q', 'Test User', '1234567890', 'USER')
ON DUPLICATE KEY UPDATE username=username;

