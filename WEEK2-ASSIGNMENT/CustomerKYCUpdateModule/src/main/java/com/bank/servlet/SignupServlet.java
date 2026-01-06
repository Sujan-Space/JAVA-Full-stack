package com.bank.servlet;

import com.bank.db.DBConnection;
import com.bank.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String fullName = request.getParameter("full_name");
        String address = request.getParameter("address");
        String mobileNumber = request.getParameter("mobile_number");
        String dateOfBirth = request.getParameter("date_of_birth");
        
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert user
            String userQuery = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, username);
            userStmt.setString(2, PasswordUtil.hashPassword(password));
            userStmt.setString(3, email);
            userStmt.executeUpdate();
            
            // Get generated user_id
            ResultSet rs = userStmt.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }
            
            // Insert KYC info
            String kycQuery = "INSERT INTO kyc_info (user_id, full_name, address, mobile_number, email, date_of_birth) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement kycStmt = conn.prepareStatement(kycQuery);
            kycStmt.setInt(1, userId);
            kycStmt.setString(2, fullName);
            kycStmt.setString(3, address);
            kycStmt.setString(4, mobileNumber);
            kycStmt.setString(5, email);
            kycStmt.setString(6, dateOfBirth);
            kycStmt.executeUpdate();
            
            conn.commit();
            response.sendRedirect("login.html?success=registered");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            response.sendRedirect("signup.html?error=registration_failed");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}