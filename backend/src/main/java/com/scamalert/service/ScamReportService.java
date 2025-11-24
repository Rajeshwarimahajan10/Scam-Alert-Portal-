package com.scamalert.service;

import com.scamalert.dao.ScamReportDAO;
import com.scamalert.model.ScamReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScamReportService {

    @Autowired
    private ScamReportDAO scamReportDAO;

    public ScamReport createReport(ScamReport report) {
        if (report.getStatus() == null || report.getStatus().isEmpty()) {
            report.setStatus("PENDING");
        }
        return scamReportDAO.save(report);
    }

    public List<ScamReport> getAllReports() {
        return scamReportDAO.findAll();
    }

    public List<ScamReport> getUserReports(int userId) {
        return scamReportDAO.findByUserId(userId);
    }

    public ScamReport getReportById(int reportId) {
        return scamReportDAO.findById(reportId);
    }

    public List<ScamReport> searchByPhone(String phone) {
        return scamReportDAO.searchByPhone(phone);
    }

    public List<ScamReport> searchByEmail(String email) {
        return scamReportDAO.searchByEmail(email);
    }

    public List<ScamReport> searchByWebsite(String website) {
        return scamReportDAO.searchByWebsite(website);
    }

    public List<ScamReport> searchByKeyword(String keyword) {
        return scamReportDAO.searchByKeyword(keyword);
    }

    public void updateReportStatus(int reportId, String status) {
        scamReportDAO.updateStatus(reportId, status);
    }

    public List<ScamReport> getReportsByStatus(String status) {
        return scamReportDAO.findByStatus(status);
    }
}

