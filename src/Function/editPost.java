package Function;

import javax.swing.*;
import java.sql.*;

public class editPost {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";

    public boolean editPost(int id, int authorId, String newTitle, String newContent) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            if (!isPostOwnedByUser(connection, id, authorId)) {
                JOptionPane.showMessageDialog(null, "You do not have permission to edit this post.");
                return false;
            }

            if (newTitle != null && newContent != null) {
                String sqlUpdatePost = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlUpdatePost);
                preparedStatement.setString(1, newTitle);
                preparedStatement.setString(2, newContent);
                preparedStatement.setInt(3, id);
            } else if (newTitle != null) {
                String sqlUpdatePost = "UPDATE posts SET title = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlUpdatePost);
                preparedStatement.setString(1, newTitle);
                preparedStatement.setInt(2, id);
            } else if (newContent != null) {
                String sqlUpdatePost = "UPDATE posts SET content = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlUpdatePost);
                preparedStatement.setString(1, newContent);
                preparedStatement.setInt(2, id);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            } else {
                connection.rollback();
                return false;
            }

            if (success) {
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isPostOwnedByUser(Connection connection, int postId, int authorId) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int postAuthorId = resultSet.getInt("author_id");
                    if (authorId == -1 || authorId == postAuthorId) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isPostExists(int postId) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI.ManagePostsGUI("admin"));
    }
}