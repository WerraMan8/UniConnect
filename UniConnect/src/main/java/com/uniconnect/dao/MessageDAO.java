/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uniconnect.dao;

import com.uniconnect.models.Message;
import com.uniconnect.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Werner
 */


public class MessageDAO {
    public void saveMessage(Message msg) throws SQLException {
        String sql = "INSERT INTO messages (sender, receiver, content, sent_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, msg.getSender());
            ps.setString(2, msg.getReceiver());
            ps.setString(3, msg.getContent());
            ps.setTimestamp(4, msg.getSentAt());
            ps.executeUpdate();
        }
    }

    public List<Message> getMessagesForConversation(String userA, String userB, int limit) throws SQLException {
        String sql = "SELECT id, sender, receiver, content, sent_at FROM messages " +
                     "WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) " +
                     "ORDER BY sent_at ASC LIMIT ?";
        List<Message> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userA);
            ps.setString(2, userB);
            ps.setString(3, userB);
            ps.setString(4, userA);
            ps.setInt(5, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message m = new Message();
                    m.setId(rs.getLong("id"));
                    m.setSender(rs.getString("sender"));
                    m.setReceiver(rs.getString("receiver"));
                    m.setContent(rs.getString("content"));
                    m.setSentAt(rs.getTimestamp("sent_at"));
                    list.add(m);
                }
            }
        }
        return list;
    }
}
