/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uniconnect.dao;

import com.uniconnect.models.User;
import com.uniconnect.utils.DBConnection;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Werner
 */


public class UserDAO {
     public boolean register(String username, String plainPassword) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean validate(String username, String plainPassword) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password_hash");
                    return BCrypt.checkpw(plainPassword, hash);
                } else {
                    return false;
                }
            }
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password_hash FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    return u;
                } else return null;
            }
        }
    }
}
