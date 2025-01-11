package Function;

import javax.swing.*;
import java.sql.*;

public class addUser {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";

    public addUser(String username, String hometown, int age) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (isUsernameExists(connection, username)) {
                JOptionPane.showMessageDialog(null, "User with the same username already exists!");
                return;
            }
            int newUserId = addUserToUserTable(connection, username);
            addUserToUserDetailsTable(connection,newUserId, hometown, age);
            JOptionPane.showMessageDialog(null, "User added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to add user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isUsernameExists(Connection connection, String username) throws SQLException {
        String checkQuery = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next();
            }
        }
    }


    private int addUserToUserTable(Connection connection, String username) throws SQLException {
        String insertQuery = "INSERT INTO user (username, password, role_id) VALUES (?, ?, (SELECT id FROM roles WHERE name = 'user'))";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, username);
            insertStmt.setString(2, "1"); //Default password
            insertStmt.executeUpdate();
            try(ResultSet generatedKeys = insertStmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve new user id.");
                }
            }
        }
    }

    private void addUserToUserDetailsTable(Connection connection, int id, String hometown, int age) throws SQLException {
        String insertQuery = "INSERT INTO user_details (id, hometown, age) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, id);
            insertStmt.setString(2, hometown);
            insertStmt.setInt(3, age);
            insertStmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI.ManageUsersGUI("admin").setVisible(true));
    }
}