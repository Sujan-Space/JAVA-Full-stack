package com.bank.servlet;

import com.bank.db.DBConnection;
import com.bank.model.KYCInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        int userId = (Integer) session.getAttribute("user_id");
        String username = (String) session.getAttribute("username");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM kyc_info WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Home - Internet Banking</title>");
            out.println("<style>");
            out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
            out.println(".navbar { background-color: #2c3e50; padding: 15px 50px; color: white; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println(".navbar h2 { font-size: 24px; }");
            out.println(".navbar a { color: white; text-decoration: none; margin-left: 20px; padding: 8px 15px; border-radius: 5px; transition: background 0.3s; }");
            out.println(".navbar a:hover { background-color: #34495e; }");
            out.println(".container { max-width: 900px; margin: 50px auto; background: white; border-radius: 15px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); overflow: hidden; }");
            out.println(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }");
            out.println(".content { padding: 40px; }");
            out.println(".kyc-card { background: #f8f9fa; padding: 25px; border-radius: 10px; margin-bottom: 20px; border-left: 5px solid #667eea; }");
            out.println(".kyc-row { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #e0e0e0; }");
            out.println(".kyc-row:last-child { border-bottom: none; }");
            out.println(".kyc-label { font-weight: 600; color: #555; }");
            out.println(".kyc-value { color: #333; }");
            out.println(".btn-container { text-align: center; margin-top: 30px; }");
            out.println(".btn { display: inline-block; padding: 12px 30px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; text-decoration: none; border-radius: 25px; transition: transform 0.3s, box-shadow 0.3s; font-weight: 600; }");
            out.println(".btn:hover { transform: translateY(-2px); box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4); }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            // Navbar
            out.println("<div class='navbar'>");
            out.println("<h2>🏦 Retail Bank Portal</h2>");
            out.println("<div>");
            out.println("<span>Welcome, " + username + "</span>");
            out.println("<a href='logout'>Logout</a>");
            out.println("</div>");
            out.println("</div>");
            
            out.println("<div class='container'>");
            out.println("<div class='header'>");
            out.println("<h1>Your KYC Information</h1>");
            out.println("</div>");
            
            out.println("<div class='content'>");
            
            if (rs.next()) {
                out.println("<div class='kyc-card'>");
                out.println("<div class='kyc-row'><span class='kyc-label'>Full Name:</span><span class='kyc-value'>" + rs.getString("full_name") + "</span></div>");
                out.println("<div class='kyc-row'><span class='kyc-label'>Address:</span><span class='kyc-value'>" + rs.getString("address") + "</span></div>");
                out.println("<div class='kyc-row'><span class='kyc-label'>Mobile Number:</span><span class='kyc-value'>" + rs.getString("mobile_number") + "</span></div>");
                out.println("<div class='kyc-row'><span class='kyc-label'>Email:</span><span class='kyc-value'>" + rs.getString("email") + "</span></div>");
                out.println("<div class='kyc-row'><span class='kyc-label'>Date of Birth:</span><span class='kyc-value'>" + rs.getString("date_of_birth") + "</span></div>");
                out.println("<div class='kyc-row'><span class='kyc-label'>Last Updated:</span><span class='kyc-value'>" + rs.getString("updated_at") + "</span></div>");
                out.println("</div>");
            } else {
                out.println("<p style='text-align: center; color: #e74c3c;'>⚠️ No KYC information found. Please contact support.</p>");
            }
            
            out.println("<div class='btn-container'>");
            out.println("<a href='updateKYC' class='btn'>Update KYC Information</a>");
            out.println("</div>");
            
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=system_error");
        }
    }
}