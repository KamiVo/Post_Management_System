package Function;

import Main.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class viewUser extends JPanel {
    public viewUser() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setOpaque(false);

        List<User> users = getUsers();

        for (User user : users) {
            JPanel userPanel = createUserPanel(user);
            userPanel.setBackground(Color.BLUE);
            userPanel.setPreferredSize(new Dimension(300, 200));
            userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            userPanel.setLayout(new GridLayout(4, 1));

            mainPanel.add(userPanel);
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        scrollPane.setOpaque(false);

        add(scrollPane);
    }

    private JPanel createUserPanel(User user) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setLayout(new GridLayout(4, 1));

        if (!"admin".equals(user.getRole())) {
            panel.add(createLabel("<html><span style='color:yellow;'>ID:</span> " + user.getId() + "</html>"));
        }
        panel.add(createLabel("<html><span style='color:yellow;'>Username:</span> " + user.getName() + "</html>"));
        panel.add(createLabel("<html><span style='color:yellow;'>Age:</span> " + user.getAge() + "</html>"));
        panel.add(createLabel("<html><span style='color:yellow;'>Hometown:</span> " + user.getHometown() + "</html>"));

        return panel;
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "K@miVo_02825");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id, username, age, hometown FROM user_details")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                int age = resultSet.getInt("age");
                String hometown = resultSet.getString("hometown");
                users.add(new User(id, username, hometown, age, null));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to get users: " + e.getMessage());
        }
        return users;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI.ManageUsersGUI("admin").setVisible(true));
    }
}