package com.scamalert.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScamReport {
    private int reportId;
    private int userId;
    private String scamType;
    private String scamMethod;
    private String reportedPhone;
    private String reportedEmail;
    private String reportedWebsite;
    private String reportedName;
    private String description;
    private BigDecimal amountLost;
    private LocalDate incidentDate;
    private String location;
    private String proofFilePath;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username; // For display purposes

    public ScamReport() {
    }

    public ScamReport(int reportId, int userId, String scamType, String scamMethod, 
                     String reportedPhone, String reportedEmail, String reportedWebsite,
                     String reportedName, String description, BigDecimal amountLost,
                     LocalDate incidentDate, String location, String proofFilePath,
                     String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reportId = reportId;
        this.userId = userId;
        this.scamType = scamType;
        this.scamMethod = scamMethod;
        this.reportedPhone = reportedPhone;
        this.reportedEmail = reportedEmail;
        this.reportedWebsite = reportedWebsite;
        this.reportedName = reportedName;
        this.description = description;
        this.amountLost = amountLost;
        this.incidentDate = incidentDate;
        this.location = location;
        this.proofFilePath = proofFilePath;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getScamType() {
        return scamType;
    }

    public void setScamType(String scamType) {
        this.scamType = scamType;
    }

    public String getScamMethod() {
        return scamMethod;
    }

    public void setScamMethod(String scamMethod) {
        this.scamMethod = scamMethod;
    }

    public String getReportedPhone() {
        return reportedPhone;
    }

    public void setReportedPhone(String reportedPhone) {
        this.reportedPhone = reportedPhone;
    }

    public String getReportedEmail() {
        return reportedEmail;
    }

    public void setReportedEmail(String reportedEmail) {
        this.reportedEmail = reportedEmail;
    }

    public String getReportedWebsite() {
        return reportedWebsite;
    }

    public void setReportedWebsite(String reportedWebsite) {
        this.reportedWebsite = reportedWebsite;
    }

    public String getReportedName() {
        return reportedName;
    }

    public void setReportedName(String reportedName) {
        this.reportedName = reportedName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountLost() {
        return amountLost;
    }

    public void setAmountLost(BigDecimal amountLost) {
        this.amountLost = amountLost;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProofFilePath() {
        return proofFilePath;
    }

    public void setProofFilePath(String proofFilePath) {
        this.proofFilePath = proofFilePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

