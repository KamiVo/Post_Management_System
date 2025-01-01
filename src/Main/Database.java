package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/user_management";
        String user = "root";
        String password = "K@miVo_02825";
        return DriverManager.getConnection(url, user, password);
    }
}