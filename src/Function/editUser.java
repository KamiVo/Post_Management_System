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

            if (newHometown != null || newAge != null) {
                StringBuilder sqlDetailsBuilder = new StringBuilder("UPDATE user_details SET ");
                boolean firstUpdate = true;
                if (newHometown != null) {
                    sqlDetailsBuilder.append("hometown = ?");
                    firstUpdate = false;
                }
                if (newAge != null) {
                    if (!firstUpdate) {
                        sqlDetailsBuilder.append(", ");
                    }
                    sqlDetailsBuilder.append("age = ?");
                }

                sqlDetailsBuilder.append(" WHERE id = ?");
                String sqlDetails = sqlDetailsBuilder.toString();

                preparedStatement = connection.prepareStatement(sqlDetails);
                int paramIndex = 1;
                if (newHometown != null) {
                    preparedStatement.setString(paramIndex++, newHometown);
                }
                if (newAge != null) {
                    preparedStatement.setInt(paramIndex++, newAge);
                }
                preparedStatement.setInt(paramIndex, id);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI.ManageUsersGUI("admin").setVisible(true));
    }
}