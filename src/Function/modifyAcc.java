package Function;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.UUID;

public class modifyAcc {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";
    private static final String AVATAR_DIR = "src/Resources/avatars/";

    public boolean modifyAcc(int id, String username, String oldPassword, String newPassword, String newUsername, String avatarPath) {
        boolean success = false;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            if (isValidUser(connection, id, username, oldPassword)) {
                if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals(oldPassword)) {
                    updatePassword(connection, id, username, newPassword);
                    success = true;
                }
                if (newUsername != null && !newUsername.isEmpty() && !newUsername.equals(username)) {
                    updateUsername(connection, id, username, newUsername);
                    success = true;
                }
                if (avatarPath != null && !avatarPath.isEmpty()) {
                    updateAvatar(connection, id, username, avatarPath);
                    success = true;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid user credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (success) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to modify account: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return success;
    }

    private boolean isValidUser(Connection connection, int id, String username, String oldPassword) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ? AND password = ? AND id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, oldPassword);
            preparedStatement.setInt(3, id);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to validate user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void updatePassword(Connection connection, int id, String username, String newPassword) throws SQLException {
        String updateQuery = "UPDATE user SET password = ? WHERE username = ? AND id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to update password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUsername(Connection connection, int id, String oldUsername, String newUsername) throws SQLException {
        String updateQuery = "UPDATE user SET username = ? WHERE username = ? AND id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, oldUsername);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to update username: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAvatar(Connection connection, int id, String username, String avatarPath) throws SQLException, IOException {
        String oldAvatarPath = getAvatarPathId(id);
        String newAvatarPath = AVATAR_DIR + UUID.randomUUID().toString() + getFileExtension(avatarPath);

        // Copy new avatar to the avatars directory
        Files.copy(new File(avatarPath).toPath(), new File(newAvatarPath).toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Update the database with the new avatar path
        String updateQuery = "UPDATE user SET avatar = ? WHERE username = ? AND id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newAvatarPath);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to update avatar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Delete the old avatar if it exists and is not the default one
        if (oldAvatarPath != null && !oldAvatarPath.equals("src/Resources/user.png") && !oldAvatarPath.isEmpty()) {
            File oldAvatarFile = new File(oldAvatarPath);
            if (oldAvatarFile.exists()) {
                oldAvatarFile.delete();
            }
        }
    }
    public static String getAvatarPathId(int id) {
        String query = "SELECT avatar FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String avatarPath = resultSet.getString("avatar");
                    if (avatarPath != null && !avatarPath.isEmpty()) {
                        return avatarPath;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to get avatar path: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return "src/Resources/user.png"; // Default avatar path
    }


    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return ""; // No extension found
    }
}