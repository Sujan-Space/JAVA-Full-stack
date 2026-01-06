package com.bank.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.bank.db.DBConnection;

public class AuditLogger {
    
    public static void logKYCUpdate(int userId, String action, String oldValues, 
                                    String newValues, String ipAddress) {
        String query = "INSERT INTO kyc_audit_log (user_id, action, old_values, new_values, ip_address) " +
                       "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, oldValues);
            stmt.setString(4, newValues);
            stmt.setString(5, ipAddress);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}