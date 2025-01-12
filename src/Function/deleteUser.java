package Function;

import javax.swing.*;
import java.sql.*;

public class deleteUser {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";

    public boolean deleteUser(int id, boolean deleteAge, boolean deleteHometown, boolean deleteAll) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            if (deleteAll) {
                // Delete related posts first
                String deletePostsQuery = "DELETE FROM posts WHERE author_id = ?";
                preparedStatement = connection.prepareStatement(deletePostsQuery);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                preparedStatement.close();

                // Delete user details
                String deleteUserDetailsQuery = "DELETE FROM user_details WHERE id = ?";
                preparedStatement = connection.prepareStatement(deleteUserDetailsQuery);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                preparedStatement.close();

                // Delete user
                String deleteUserQuery = "DELETE FROM user WHERE id = ?";
                preparedStatement = connection.prepareStatement(deleteUserQuery);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                preparedStatement.close();

                success = true;
            } else {
                StringBuilder queryBuilder = new StringBuilder("UPDATE user_details SET ");
                boolean first = true;
                if (deleteAge) {
                    queryBuilder.append("age = NULL");
                    first = false;
                }
                if (deleteHometown) {
                    if (!first) {
                        queryBuilder.append(", ");
                    }
                    queryBuilder.append("hometown = NULL");
                }
                queryBuilder.append(" WHERE id = ?");
                preparedStatement = connection.prepareStatement(queryBuilder.toString());
                preparedStatement.setInt(1, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                success = rowsUpdated > 0;
                preparedStatement.close();
            }

            if (success) {
                connection.commit();
            } else {
                connection.rollback();
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