package Function;

import GUI.ManageUsersGUI;
import Main.LoginRegisterGUI;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class editUser {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = "K@miVo_02825"; // Replace with your database password

    public editUser(String username, String hometown, int age) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageUsersGUI("Admin").setVisible(true));
    }
}