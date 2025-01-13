package Function;

import javax.swing.*;
import java.sql.*;

public class deletePost {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";

    public boolean deletePost(int id, int authorId, boolean deleteTitle, boolean deleteContent, boolean deleteAll) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            if (!isPostOwnedByUser(connection, id, authorId)) {
                JOptionPane.showMessageDialog(null, "You do not have permission to delete this post.");
                return false;
            }

            if (deleteAll) {
                String deletePostQuery = "DELETE FROM posts WHERE id = ?";
                preparedStatement = connection.prepareStatement(deletePostQuery);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                preparedStatement.close();

                success = true;
            } else {
                StringBuilder queryBuilder = new StringBuilder("UPDATE posts SET ");
                boolean first = true;
                if (deleteTitle) {
                    queryBuilder.append("title = NULL");
                    first = false;
                }
                if (deleteContent) {
                    if (!first) {
                        queryBuilder.append(", ");
                    }
                    queryBuilder.append("content = NULL");
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
        SwingUtilities.invokeLater(() -> new GUI.ManagePostsGUI("user"));
    }
}