package com.scamalert.dao;

import com.scamalert.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setRole(rs.getString("role"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        user.setActive(rs.getBoolean("is_active"));
        return user;
    };

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, username);
        } catch (Exception e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND is_active = TRUE";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email);
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND is_active = TRUE";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, userId);
        } catch (Exception e) {
            return null;
        }
    }

    public User save(User user) {
        String sql = "INSERT INTO users (username, email, password, full_name, phone_number, role, created_at, updated_at, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.setString(4, user.getFullName());
                if (user.getPhoneNumber() != null && !user.getPhoneNumber().trim().isEmpty()) {
                    ps.setString(5, user.getPhoneNumber());
                } else {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                }
                ps.setString(6, user.getRole() != null ? user.getRole() : "USER");
                ps.setTimestamp(7, Timestamp.valueOf(now));
                ps.setTimestamp(8, Timestamp.valueOf(now));
                ps.setBoolean(9, true);
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                int userId = keyHolder.getKey().intValue();
                return findById(userId);
            } else {
                throw new RuntimeException("Failed to generate user ID");
            }
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("username")) {
                throw new RuntimeException("Username already exists");
            } else if (e.getMessage().contains("email")) {
                throw new RuntimeException("Email already exists");
            }
            throw new RuntimeException("Database error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}

