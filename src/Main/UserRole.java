package Main;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRole {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = "K@miVo_02825"; // Replace with your database password

    public UserRole(String username, String hometown, int age) {
        try (Connection _ = DriverManager.getConnection(URL, USER, PASSWORD)) {
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect: " + e.getMessage());
        }
    }

    public static String getUserRole(String username) {
        String role = null;
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT r.name FROM users u JOIN roles r ON u.role_id = r.id WHERE u.username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("name");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to get role: " + e.getMessage());
        }
        return role;
    }
}