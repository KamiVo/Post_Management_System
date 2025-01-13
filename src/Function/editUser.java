package Function;

import javax.swing.*;
import java.sql.*;

public class editUser {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";

    public boolean editUser(int id, String newName, String newHometown, Integer newAge, boolean editAll) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            if (newName != null) {
                String sqlUpdateUserUsername = "UPDATE user SET username = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlUpdateUserUsername);
                preparedStatement.setString(1, newName);
                preparedStatement.setInt(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected <= 0) {
                    connection.rollback();
                    return false;
                }
                success = true;
            }
            // Handle all combinations of user_details updates
            if (newHometown != null && newAge != null) {
                String sqlDetails = "UPDATE user_details SET hometown = ?, age = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlDetails);
                preparedStatement.setString(1, newHometown);
                preparedStatement.setInt(2, newAge);
                preparedStatement.setInt(3, id);
            } else if (newHometown != null) {
                String sqlDetails = "UPDATE user_details SET hometown = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlDetails);
                preparedStatement.setString(1, newHometown);
                preparedStatement.setInt(2, id);
            } else if (newAge != null) {
                String sqlDetails = "UPDATE user_details SET age = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlDetails);
                preparedStatement.setInt(1, newAge);
                preparedStatement.setInt(2, id);
            }

            if (newHometown != null || newAge != null) {
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    success = true;
                } else {
                    connection.rollback();
                    return false;
                }
            }

            if (success) {
                connection.commit();
            }

            return success;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isUserAdmin(int userId) {
        String sql = "SELECT role_id FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int roleId = resultSet.getInt("role_id");
                    return roleId == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUserExist(int userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI.ManageUsersGUI("admin").setVisible(true));
    }
}