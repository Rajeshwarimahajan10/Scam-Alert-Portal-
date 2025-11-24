package com.scamalert.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes
 * Run this main method to generate password hashes for database
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hash for admin123
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Password: " + adminPassword);
        System.out.println("Hash: " + adminHash);
        System.out.println();
        
        // Generate hash for user123
        String userPassword = "user123";
        String userHash = encoder.encode(userPassword);
        System.out.println("Password: " + userPassword);
        System.out.println("Hash: " + userHash);
        System.out.println();
        
        // Verify the hashes
        System.out.println("Verification:");
        System.out.println("admin123 matches: " + encoder.matches(adminPassword, adminHash));
        System.out.println("user123 matches: " + encoder.matches(userPassword, userHash));
    }
}

