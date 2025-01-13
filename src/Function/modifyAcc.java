package Function;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class modifyAcc {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";

    public modifyAcc(String username, String oldPassword, String newPassword, String newUsername) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (isValidUser(connection, username, oldPassword)) {
                if (newPassword != null && !newPassword.isEmpty()) {
                    updatePassword(connection, username, newPassword);
                }
                if (newUsername != null && !newUsername.isEmpty()) {
                    updateUsername(connection, username, newUsername);
                }
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidUser(Connection connection, String username, String oldPassword) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, oldPassword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void updatePassword(Connection connection, String username, String newPassword) throws SQLException {
        String updateQuery = "UPDATE user SET password = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password.");
            }
        }
    }

    private void updateUsername(Connection connection, String oldUsername, String newUsername) throws SQLException {
        String updateQuery = "UPDATE user SET username = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, oldUsername);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Username updated successfully.");
            } else {
                System.out.println("Failed to update username.");
            }
        }
    }
}