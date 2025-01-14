package Function;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class modifyAcc {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";
    private static final String AVATAR_DIR = "src/Resources/avatars/";

    public boolean modifyAcc(int id, String username, String newUsername, String newHometown, Integer newAge,String oldPassword, String newPassword, String avatarPath) {
        boolean success = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            if (newUsername != null && !newUsername.isEmpty() && !newUsername.equals(username)) {
                // 1. Check for Username Existence (Excluding the Current User)
                String sqlCheckUsername = "SELECT * FROM user WHERE username = ? AND id != ?";
                preparedStatement = connection.prepareStatement(sqlCheckUsername);
                preparedStatement.setString(1, newUsername);
                preparedStatement.setInt(2, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        connection.rollback();
                        return false; // Exit if username exists
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    connection.rollback();
                    return false; // Exit on query error
                }

                // 2. Update the username
                String sqlUpdateUserUsername = "UPDATE user SET username = ? WHERE id = ?";
                try {
                    preparedStatement = connection.prepareStatement(sqlUpdateUserUsername);
                    preparedStatement.setString(1, newUsername);
                    preparedStatement.setInt(2, id);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected <= 0) {
                        connection.rollback();
                        return false;
                    }
                    success = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    connection.rollback();
                    return false; // Exit on query error
                }
            }

            // Check if the new Pass is equal to the old Pass
            if(newPassword != null && !newPassword.equals(oldPassword)) {

                // Update the password
                String sqlUpdateUserPassword = "UPDATE user SET password = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlUpdateUserPassword);
                preparedStatement.setString(1, newPassword);
                preparedStatement.setInt(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected <= 0) {
                    connection.rollback();
                    return false;
                }
                success = true;
            }

            // Update the hometown
            if (newHometown != null && !newHometown.isEmpty()) {
                String sqlUpdateUserHometown = "UPDATE user_details SET hometown = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlUpdateUserHometown);
                preparedStatement.setString(1, newHometown);
                preparedStatement.setInt(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected <= 0) {
                    connection.rollback();
                    return false;
                }
                success = true;
            }

            // Update the age
            if (newAge != null) {
                String sqlUpdateUserAge = "UPDATE user_details SET age = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlUpdateUserAge);
                preparedStatement.setInt(1, newAge);
                preparedStatement.setInt(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected <= 0) {
                    connection.rollback();
                    return false;
                }
                success = true;
            }

            // Update the avatar
            if (avatarPath != null) {
                String oldAvatarPath = getAvatarPathId(id);
                String newAvatarPath = AVATAR_DIR + id + getFileExtension(avatarPath);
                File newAvatarFile = new File(newAvatarPath);
                File oldAvatarFile = null;

                if(oldAvatarPath != null && !oldAvatarPath.equals("src/Resources/avatars/user.png") && !oldAvatarPath.isEmpty())
                    oldAvatarFile = new File(oldAvatarPath);

                // Copy new avatar to the avatars directory
                Files.copy(new File(avatarPath).toPath(), newAvatarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);


                // Update the database with the new avatar path
                String updateQuery = "UPDATE user SET avatar = ? WHERE id = ?";
                try {
                    preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, newAvatarPath);
                    preparedStatement.setInt(2, id);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Failed to update avatar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                success = true;
            }

            // Commit the transaction if all updates were successful
            if (success) {
                connection.commit();
            }

            return success;
        } catch (SQLException | IOException e) {
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

    public static String getAvatarPathId(int id) {
        String query = "SELECT avatar FROM user WHERE id = ?";
        String defaultPath = "src/Resources/avatars/user.png"; // Store the default path
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String path = resultSet.getString("avatar");
                    return (path != null && !path.isEmpty()) ? path : defaultPath;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to get avatar path: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return query;
    }


    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return ""; // No extension found
    }
}