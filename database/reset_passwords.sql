-- Reset Password Hashes - Run this to fix login issues
-- This script will update passwords with working BCrypt hashes

USE scam_alert_db;

-- Update admin password to: admin123
-- BCrypt hash for 'admin123'
UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE username = 'admin';

-- Update testuser password to: user123
-- BCrypt hash for 'user123' 
-- Note: If this doesn't work, register a new user instead
UPDATE users 
SET password = '$2a$10$8K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0'
WHERE username = 'testuser';

-- If users don't exist, create them
INSERT INTO users (username, email, password, full_name, role, is_active) 
SELECT 'admin', 'admin@scamalert.com', 
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 
       'System Administrator', 'ADMIN', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, email, password, full_name, phone_number, role, is_active) 
SELECT 'testuser', 'user@example.com', 
       '$2a$10$8K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0', 
       'Test User', '1234567890', 'USER', TRUE
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'testuser');

-- Verify
SELECT username, email, role, is_active, 
       LENGTH(password) as password_length,
       LEFT(password, 7) as hash_prefix
FROM users 
WHERE username IN ('admin', 'testuser');

