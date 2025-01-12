package Function;

import GUI.ManagePostsGUI;

import javax.swing.*;
import java.sql.*;

public class addPost {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";

    public addPost(String title, String content, int authorId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (isPostExists(connection, title)) {
                JOptionPane.showMessageDialog(null, "Post with the same title already exists!");
                return;
            }
            addPostToPostTable(connection, title, content, authorId);
            JOptionPane.showMessageDialog(null, "Post added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to add post: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isPostExists(Connection connection, String title) throws SQLException {
        String checkQuery = "SELECT * FROM posts WHERE title = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, title);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void addPostToPostTable(Connection connection, String title, String content, int authorId) throws SQLException {
        String insertQuery = "INSERT INTO posts (title, content, author_id) VALUES (?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, title);
            insertStmt.setString(2, content);
            insertStmt.setInt(3, authorId);
            insertStmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagePostsGUI("admin").setVisible(true));
    }
}