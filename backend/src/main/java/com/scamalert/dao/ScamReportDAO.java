package com.scamalert.dao;

import com.scamalert.model.ScamReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ScamReportDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<ScamReport> reportRowMapper = (rs, rowNum) -> {
        ScamReport report = new ScamReport();
        report.setReportId(rs.getInt("report_id"));
        report.setUserId(rs.getInt("user_id"));
        report.setScamType(rs.getString("scam_type"));
        report.setScamMethod(rs.getString("scam_method"));
        report.setReportedPhone(rs.getString("reported_phone"));
        report.setReportedEmail(rs.getString("reported_email"));
        report.setReportedWebsite(rs.getString("reported_website"));
        report.setReportedName(rs.getString("reported_name"));
        report.setDescription(rs.getString("description"));
        
        BigDecimal amount = rs.getBigDecimal("amount_lost");
        report.setAmountLost(amount);
        
        Date incidentDate = rs.getDate("incident_date");
        if (incidentDate != null) {
            report.setIncidentDate(incidentDate.toLocalDate());
        }
        
        report.setLocation(rs.getString("location"));
        report.setProofFilePath(rs.getString("proof_file_path"));
        report.setStatus(rs.getString("status"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            report.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            report.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        try {
            report.setUsername(rs.getString("username"));
        } catch (Exception e) {
            // Username might not be in result set
        }
        
        return report;
    };

    public ScamReport save(ScamReport report) {
        String sql = "INSERT INTO scam_reports (user_id, scam_type, scam_method, reported_phone, reported_email, " +
                     "reported_website, reported_name, description, amount_lost, incident_date, location, " +
                     "proof_file_path, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, report.getUserId());
            ps.setString(2, report.getScamType());
            ps.setString(3, report.getScamMethod());
            ps.setString(4, report.getReportedPhone());
            ps.setString(5, report.getReportedEmail());
            ps.setString(6, report.getReportedWebsite());
            ps.setString(7, report.getReportedName());
            ps.setString(8, report.getDescription());
            ps.setBigDecimal(9, report.getAmountLost());
            if (report.getIncidentDate() != null) {
                ps.setDate(10, Date.valueOf(report.getIncidentDate()));
            } else {
                ps.setDate(10, null);
            }
            ps.setString(11, report.getLocation());
            ps.setString(12, report.getProofFilePath());
            ps.setString(13, report.getStatus() != null ? report.getStatus() : "PENDING");
            ps.setTimestamp(14, Timestamp.valueOf(now));
            ps.setTimestamp(15, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);

        int reportId = keyHolder.getKey().intValue();
        return findById(reportId);
    }

    public ScamReport findById(int reportId) {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "WHERE sr.report_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, reportRowMapper, reportId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ScamReport> findAll() {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "ORDER BY sr.created_at DESC";
        return jdbcTemplate.query(sql, reportRowMapper);
    }

    public List<ScamReport> findByUserId(int userId) {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "WHERE sr.user_id = ? ORDER BY sr.created_at DESC";
        return jdbcTemplate.query(sql, reportRowMapper, userId);
    }

    public List<ScamReport> searchByPhone(String phone) {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "WHERE sr.reported_phone LIKE ? ORDER BY sr.created_at DESC";
        return jdbcTemplate.query(sql, reportRowMapper, "%" + phone + "%");
    }

    public List<ScamReport> searchByEmail(String email) {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "WHERE sr.reported_email LIKE ? ORDER BY sr.created_at DESC";
        return jdbcTemplate.query(sql, reportRowMapper, "%" + email + "%");
    }

    public List<ScamReport> searchByWebsite(String website) {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "WHERE sr.reported_website LIKE ? ORDER BY sr.created_at DESC";
        return jdbcTemplate.query(sql, reportRowMapper, "%" + website + "%");
    }

    public List<ScamReport> searchByKeyword(String keyword) {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "WHERE sr.description LIKE ? OR sr.reported_name LIKE ? OR sr.scam_type LIKE ? " +
                     "ORDER BY sr.created_at DESC";
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, reportRowMapper, searchPattern, searchPattern, searchPattern);
    }

    public void updateStatus(int reportId, String status) {
        String sql = "UPDATE scam_reports SET status = ?, updated_at = ? WHERE report_id = ?";
        jdbcTemplate.update(sql, status, Timestamp.valueOf(LocalDateTime.now()), reportId);
    }

    public List<ScamReport> findByStatus(String status) {
        String sql = "SELECT sr.*, u.username FROM scam_reports sr " +
                     "LEFT JOIN users u ON sr.user_id = u.user_id " +
                     "WHERE sr.status = ? ORDER BY sr.created_at DESC";
        return jdbcTemplate.query(sql, reportRowMapper, status);
    }
}

