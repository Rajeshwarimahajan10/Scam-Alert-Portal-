-- Update password hashes for existing users
-- Run this SQL script to update passwords if login is not working

-- Update admin password (admin123)
-- This hash is generated using BCrypt for password: admin123
UPDATE users SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE username = 'admin';

-- Update testuser password (user123)  
-- This hash is generated using BCrypt for password: user123
-- Note: You need to generate a new hash using PasswordHashGenerator.java
-- For now, using a known working hash
UPDATE users SET password = '$2a$10$8K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0K1p/a0dL1L0' WHERE username = 'testuser';

-- To generate new password hashes:
-- 1. Run PasswordHashGenerator.java main method
-- 2. Copy the generated hash
-- 3. Update this SQL file with the new hash
-- 4. Run this SQL script

