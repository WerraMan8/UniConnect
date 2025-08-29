/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uniconnect.chat;

import com.uniconnect.dao.MessageDAO;
import com.uniconnect.models.Message;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author Werner
 */

@ServerEndpoint("/chat")
public class ChatEndpoint {
    
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("New connection: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String sender = "User" + session.getId();

        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(null);
        msg.setContent(message);
        msg.setSentAt(new Timestamp(System.currentTimeMillis()));

        MessageDAO messageDao = new MessageDAO();
        try {
            messageDao.saveMessage(msg);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        synchronized (sessions) {
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendText(sender + ": " + message);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }
}
