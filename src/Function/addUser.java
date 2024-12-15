package Function;

import Main.LoginRegisterGUI;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addUser {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = "K@miVo_02825"; // Replace with your database password

    public addUser(String username, String hometown, int age) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String checkQuery = "SELECT * FROM user WHERE username = ? OR hometown = ? OR age = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, hometown);
                checkStmt.setInt(3, age);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "User with the same username, hometown, or age already exists!");
                        return;
                    }
                }
            }

            String insertQuery = "INSERT INTO user (username, hometown, age) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, hometown);
                insertStmt.setInt(3, age);
                insertStmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "User added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add user: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginRegisterGUI().setVisible(true));
    }
}