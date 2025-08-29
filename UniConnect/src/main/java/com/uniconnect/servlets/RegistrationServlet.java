/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uniconnect.servlets;

import com.uniconnect.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Werner
 */

@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet{
      private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDao = new UserDAO();

        try {
            boolean userCreated = userDao.register(username, password); // <-- call register instead of validate
            if (userCreated) {
                response.sendRedirect("Login.html"); // redirect to login page after successful registration
            } else {
                response.getWriter().println("Registration failed. User may already exist.");
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
