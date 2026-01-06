package com.bank.servlet;

import com.bank.db.DBConnection;
import com.bank.util.AuditLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/updateKYC")
public class UpdateKYCServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        int userId = (Integer) session.getAttribute("user_id");
        
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
            out.println("<title>Update KYC</title>");
            out.println("<style>");
            out.println("* { margin: 0; padding: 0; box-sizing: border-box; }");
            out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
            out.println(".navbar { background-color: #2c3e50; padding: 15px 50px; color: white; display: flex; justify-content: space-between; align-items: center; }");
            out.println(".navbar h2 { font-size: 24px; }");
            out.println(".navbar a { color: white; text-decoration: none; margin-left: 20px; padding: 8px 15px; border-radius: 5px; transition: background 0.3s; }");
            out.println(".navbar a:hover { background-color: #34495e; }");
            out.println(".container { max-width: 600px; margin: 50px auto; background: white; border-radius: 15px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); overflow: hidden; }");
            out.println(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }");
            out.println(".form-container { padding: 40px; }");
            out.println(".form-group { margin-bottom: 20px; }");
            out.println(".form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #555; }");
            out.println(".form-group input, .form-group textarea { width: 100%; padding: 12px; border: 2px solid #e0e0e0; border-radius: 8px; font-size: 14px; transition: border 0.3s; }");
            out.println(".form-group input:focus, .form-group textarea:focus { outline: none; border-color: #667eea; }");
            out.println(".form-group textarea { resize: vertical; min-height: 80px; }");
            out.println(".btn-submit { width: 100%; padding: 14px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; border-radius: 25px; font-size: 16px; font-weight: 600; cursor: pointer; transition: transform 0.3s; }");
            out.println(".btn-submit:hover { transform: translateY(-2px); }");
            out.println(".readonly { background-color: #f5f5f5; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='navbar'>");
            out.println("<h2>🏦 Retail Bank Portal</h2>");
            out.println("<div><a href='home'>← Back to Home</a></div>");
            out.println("</div>");
            
            out.println("<div class='container'>");
            out.println("<div class='header'><h1>Update KYC Information</h1></div>");
            out.println("<div class='form-container'>");
            
            if (rs.next()) {
                out.println("<form action='updateKYC' method='POST'>");
                
                out.println("<div class='form-group'>");
                out.println("<label>Full Name:</label>");
                out.println("<input type='text' name='full_name' value='" + rs.getString("full_name") + "' readonly class='readonly'>");
                out.println("</div>");
                
                out.println("<div class='form-group'>");
                out.println("<label>Address: *</label>");
                out.println("<textarea name='address' required>" + rs.getString("address") + "</textarea>");
                out.println("</div>");
                
                out.println("<div class='form-group'>");
                out.println("<label>Mobile Number: *</label>");
                out.println("<input type='tel' name='mobile_number' value='" + rs.getString("mobile_number") + "' pattern='[0-9]{10}' required>");
                out.println("</div>");
                
                out.println("<div class='form-group'>");
                out.println("<label>Email: *</label>");
                out.println("<input type='email' name='email' value='" + rs.getString("email") + "' required>");
                out.println("</div>");
                
                out.println("<div class='form-group'>");
                out.println("<label>Date of Birth:</label>");
                out.println("<input type='date' name='date_of_birth' value='" + rs.getString("date_of_birth") + "' readonly class='readonly'>");
                out.println("</div>");
                
                out.println("<button type='submit' class='btn-submit'>Update KYC Information</button>");
                out.println("</form>");
            }
            
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("home?error=system_error");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        int userId = (Integer) session.getAttribute("user_id");
        String address = request.getParameter("address");
        String mobileNumber = request.getParameter("mobile_number");
        String email = request.getParameter("email");
        String ipAddress = request.getRemoteAddr();
        
        try (Connection conn = DBConnection.getConnection()) {
            
            // Get old values for audit
            String selectQuery = "SELECT address, mobile_number, email FROM kyc_info WHERE user_id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setInt(1, userId);
            ResultSet rs = selectStmt.executeQuery();
            
            String oldValues = "";
            if (rs.next()) {
                oldValues = "Address: " + rs.getString("address") + 
                           ", Mobile: " + rs.getString("mobile_number") + 
                           ", Email: " + rs.getString("email");
            }
            
            // Update KYC info
            String updateQuery = "UPDATE kyc_info SET address = ?, mobile_number = ?, email = ? WHERE user_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, address);
            updateStmt.setString(2, mobileNumber);
            updateStmt.setString(3, email);
            updateStmt.setInt(4, userId);
            
            int rowsUpdated = updateStmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                String newValues = "Address: " + address + ", Mobile: " + mobileNumber + ", Email: " + email;
                AuditLogger.logKYCUpdate(userId, "KYC_UPDATE", oldValues, newValues, ipAddress);
                response.sendRedirect("home?success=kyc_updated");
            } else {
                response.sendRedirect("updateKYC?error=update_failed");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("updateKYC?error=system_error");
        }
    }
}