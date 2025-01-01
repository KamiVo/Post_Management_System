package Function;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class viewUser extends JPanel {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = "K@miVo_02825"; // Replace with your database password

    public viewUser() {
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadData(tableModel);
    }

    private void loadData(DefaultTableModel tableModel) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT username, hometown, age FROM users WHERE role_id = 2";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Add column names to the table model
                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(metaData.getColumnName(i));
                }

                // Add rows to the table model
                while (resultSet.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = resultSet.getObject(i);
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("View Users");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new viewUser());
            frame.setVisible(true);
        });
    }
}