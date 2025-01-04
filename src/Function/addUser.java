package Function;

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
            if (isUserExists(connection, username, hometown, age)) {
                JOptionPane.showMessageDialog(null, "User with the same username, hometown, or age already exists!");
                return;
            }
            addUserToDatabase(connection, username, hometown, age);
            JOptionPane.showMessageDialog(null, "User added successfully!");
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Failed to add user: " + e.getMessage());
        }
    }

    private boolean isUserExists(Connection connection, String username, String hometown, int age) throws SQLException {
        String checkQuery = "SELECT * FROM user_details WHERE username = ? OR hometown = ? OR age = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            checkStmt.setString(2, hometown);
            checkStmt.setInt(3, age);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void addUserToDatabase(Connection connection, String username, String hometown, int age) throws SQLException {
        String insertQuery = "INSERT INTO user_details (username, hometown, age) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, hometown);
            insertStmt.setInt(3, age);
            insertStmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI.ManageUsersGUI("admin").setVisible(true));
    }
}