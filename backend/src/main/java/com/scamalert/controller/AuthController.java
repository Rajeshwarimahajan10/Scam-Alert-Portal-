package com.scamalert.controller;

import com.scamalert.dto.ApiResponse;
import com.scamalert.dto.LoginRequest;
import com.scamalert.dto.RegisterRequest;
import com.scamalert.model.User;
import com.scamalert.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            // Validate required fields
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Username is required"));
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Email is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Password is required"));
            }
            if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Full name is required"));
            }

            User user = new User();
            user.setUsername(request.getUsername().trim());
            user.setEmail(request.getEmail().trim().toLowerCase());
            user.setPassword(request.getPassword());
            user.setFullName(request.getFullName().trim());
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
                user.setPhoneNumber(request.getPhoneNumber().trim());
            }
            user.setRole("USER");

            User savedUser = userService.register(user);
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", savedUser.getUserId());
            userData.put("username", savedUser.getUsername());
            userData.put("email", savedUser.getEmail());
            userData.put("fullName", savedUser.getFullName());

            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", userData));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> loginData = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", loginData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}

