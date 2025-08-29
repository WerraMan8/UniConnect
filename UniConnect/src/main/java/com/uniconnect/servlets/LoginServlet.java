/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uniconnect.servlets;

import com.uniconnect.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Werner
 */

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet{
    
      @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

         UserDAO userDao = new UserDAO();
        try {
            if (userDao.validate(username, password)) {
                // Success -> set session + redirect
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                response.sendRedirect("Home.html");
            } else {
                // Failure -> show error
                response.getWriter().println("Invalid username or password. <a href='Login.html'>Try again</a>");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while validating login", e);
        }
    } 
}
