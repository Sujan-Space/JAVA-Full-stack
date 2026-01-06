package com.bank.servlet;

import com.bank.db.DBConnection;
import com.bank.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT user_id, password, username FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("password");
                
                if (PasswordUtil.verifyPassword(password, storedHash)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", rs.getInt("user_id"));
                    session.setAttribute("username", rs.getString("username"));
                    session.setMaxInactiveInterval(30 * 60); 
                    
                    response.sendRedirect("home");
                } else {
                    response.sendRedirect("login.html?error=invalid_credentials");
                }
            } else {
                response.sendRedirect("login.html?error=invalid_credentials");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=system_error");
        }
    }
}