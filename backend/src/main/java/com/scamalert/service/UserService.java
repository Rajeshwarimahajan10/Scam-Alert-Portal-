package com.scamalert.service;

import com.scamalert.dao.UserDAO;
import com.scamalert.model.User;
import com.scamalert.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Map<String, Object> login(String username, String password) {
        // Trim username to handle whitespace
        username = username != null ? username.trim() : null;
        
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        
        if (password == null || password.isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        
        User user = userDAO.findByUsername(username);
        
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // Check if account is active
        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated. Please contact administrator.");
        }

        // Verify password
        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        
        if (!passwordMatches) {
            // Log for debugging (remove in production)
            System.out.println("Login attempt failed for username: " + username);
            System.out.println("Stored password hash: " + user.getPassword());
            throw new RuntimeException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("role", user.getRole());
        response.put("userId", user.getUserId());
        
        return response;
    }

    public User register(User user) {
        // Validate input
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Full name cannot be empty");
        }

        // Check if username already exists
        if (userDAO.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userDAO.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        // Save user to database
        try {
            return userDAO.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
        }
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}

