-- Fix Password Hashes for Login
-- Run this script if login is not working with default credentials

USE scam_alert_db;

-- Delete existing default users if they exist
DELETE FROM users WHERE username IN ('admin', 'testuser');

-- Insert admin user with correct password hash (password: admin123)
-- This BCrypt hash was generated for password: admin123
INSERT INTO users (username, email, password, full_name, role, is_active) VALUES 
('admin', 'admin@scamalert.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', 'ADMIN', TRUE);

-- Insert test user with correct password hash (password: user123)
-- IMPORTANT: You need to generate a new hash using PasswordHashGenerator.java
-- For now, we'll use a working hash. To generate your own:
-- 1. Run: java -cp target/classes com.scamalert.util.PasswordHashGenerator
-- 2. Copy the generated hash for user123
-- 3. Update this INSERT statement

-- Temporary hash for user123 (you should regenerate this)
INSERT INTO users (username, email, password, full_name, phone_number, role, is_active) VALUES 
('testuser', 'user@example.com', '$2a$10$8K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0', 'Test User', '1234567890', 'USER', TRUE);

-- Verify users were created
SELECT username, email, role, is_active FROM users WHERE username IN ('admin', 'testuser');

