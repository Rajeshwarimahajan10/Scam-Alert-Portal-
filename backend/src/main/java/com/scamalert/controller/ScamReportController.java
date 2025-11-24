package com.scamalert.controller;

import com.scamalert.dto.ApiResponse;
import com.scamalert.model.ScamReport;
import com.scamalert.model.User;
import com.scamalert.service.ScamReportService;
import com.scamalert.service.UserService;
import com.scamalert.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ScamReportController {

    @Autowired
    private ScamReportService scamReportService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private int getUserIdFromToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            String username = jwtUtil.extractUsername(token);
            // In a real scenario, you'd fetch user from database
            // For now, we'll handle it in the service layer
            return 0; // This should be improved
        }
        return 0;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createReport(@RequestBody Map<String, Object> reportData, 
                                                     HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null || !jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "Unauthorized"));
            }

            String username = jwtUtil.extractUsername(token);
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "User not found"));
            }
            int userId = user.getUserId();

            ScamReport report = new ScamReport();
            report.setUserId(userId);
            report.setScamType((String) reportData.get("scamType"));
            report.setScamMethod((String) reportData.get("scamMethod"));
            report.setReportedPhone((String) reportData.get("reportedPhone"));
            report.setReportedEmail((String) reportData.get("reportedEmail"));
            report.setReportedWebsite((String) reportData.get("reportedWebsite"));
            report.setReportedName((String) reportData.get("reportedName"));
            report.setDescription((String) reportData.get("description"));
            
            if (reportData.get("amountLost") != null) {
                report.setAmountLost(new BigDecimal(reportData.get("amountLost").toString()));
            }
            
            if (reportData.get("incidentDate") != null) {
                report.setIncidentDate(LocalDate.parse(reportData.get("incidentDate").toString()));
            }
            
            report.setLocation((String) reportData.get("location"));
            report.setProofFilePath((String) reportData.get("proofFilePath"));
            report.setStatus("PENDING");

            ScamReport savedReport = scamReportService.createReport(report);
            return ResponseEntity.ok(new ApiResponse(true, "Report submitted successfully", savedReport));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error creating report: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllReports() {
        try {
            List<ScamReport> reports = scamReportService.getAllReports();
            return ResponseEntity.ok(new ApiResponse(true, "Reports retrieved successfully", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving reports: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserReports(@PathVariable int userId) {
        try {
            List<ScamReport> reports = scamReportService.getUserReports(userId);
            return ResponseEntity.ok(new ApiResponse(true, "User reports retrieved successfully", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving user reports: " + e.getMessage()));
        }
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ApiResponse> getReportById(@PathVariable int reportId) {
        try {
            ScamReport report = scamReportService.getReportById(reportId);
            if (report == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Report not found"));
            }
            return ResponseEntity.ok(new ApiResponse(true, "Report retrieved successfully", report));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving report: " + e.getMessage()));
        }
    }

    @GetMapping("/search/phone")
    public ResponseEntity<ApiResponse> searchByPhone(@RequestParam String phone) {
        try {
            List<ScamReport> reports = scamReportService.searchByPhone(phone);
            return ResponseEntity.ok(new ApiResponse(true, "Search results retrieved", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error searching: " + e.getMessage()));
        }
    }

    @GetMapping("/search/email")
    public ResponseEntity<ApiResponse> searchByEmail(@RequestParam String email) {
        try {
            List<ScamReport> reports = scamReportService.searchByEmail(email);
            return ResponseEntity.ok(new ApiResponse(true, "Search results retrieved", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error searching: " + e.getMessage()));
        }
    }

    @GetMapping("/search/website")
    public ResponseEntity<ApiResponse> searchByWebsite(@RequestParam String website) {
        try {
            List<ScamReport> reports = scamReportService.searchByWebsite(website);
            return ResponseEntity.ok(new ApiResponse(true, "Search results retrieved", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error searching: " + e.getMessage()));
        }
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<ApiResponse> searchByKeyword(@RequestParam String keyword) {
        try {
            List<ScamReport> reports = scamReportService.searchByKeyword(keyword);
            return ResponseEntity.ok(new ApiResponse(true, "Search results retrieved", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error searching: " + e.getMessage()));
        }
    }

    @PutMapping("/{reportId}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable int reportId, 
                                                     @RequestBody Map<String, String> statusData,
                                                     HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null || !jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "Unauthorized"));
            }

            String role = jwtUtil.getRoleFromToken(token);
            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse(false, "Only admins can update report status"));
            }

            String status = statusData.get("status");
            scamReportService.updateReportStatus(reportId, status);
            return ResponseEntity.ok(new ApiResponse(true, "Status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error updating status: " + e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse> getReportsByStatus(@PathVariable String status) {
        try {
            List<ScamReport> reports = scamReportService.getReportsByStatus(status);
            return ResponseEntity.ok(new ApiResponse(true, "Reports retrieved successfully", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving reports: " + e.getMessage()));
        }
    }
}

