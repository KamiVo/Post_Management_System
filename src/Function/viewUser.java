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
        setLayout(new BorderLayout());

        List<User> users = getUsers();
        DefaultListModel<User> listModel = new DefaultListModel<>();
        for (User user : users) {
            listModel.addElement(user);
        }

        JList<User> userList = new JList<>(listModel);
        userList.setCellRenderer(new UserCellRenderer());
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1000, 600));

        add(scrollPane, BorderLayout.CENTER);
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

    private static class UserCellRenderer extends JPanel implements ListCellRenderer<User> {
        private final JLabel idLabel;
        private final JLabel usernameLabel;
        private final JLabel ageLabel;
        private final JLabel hometownLabel;

        public UserCellRenderer() {
            setLayout(new GridLayout(4, 1));
            setBackground(Color.BLUE);
            setPreferredSize(new Dimension(250, 200));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            idLabel = createLabel();
            usernameLabel = createLabel();
            ageLabel = createLabel();
            hometownLabel = createLabel();

            add(idLabel);
            add(usernameLabel);
            add(ageLabel);
            add(hometownLabel);
        }

        private JLabel createLabel() {
            JLabel label = new JLabel();
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setForeground(Color.WHITE);
            return label;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends User> list, User user, int index, boolean isSelected, boolean cellHasFocus) {
            if (!"admin".equals(user.getRole())) {
                idLabel.setText("<html><span style='color:yellow;'>ID:</span> " + user.getId() + "</html>");
            } else {
                idLabel.setText("");
            }
            usernameLabel.setText("<html><span style='color:yellow;'>Username:</span> " + user.getName() + "</html>");
            ageLabel.setText("<html><span style='color:yellow;'>Age:</span> " + user.getAge() + "</html>");
            hometownLabel.setText("<html><span style='color:yellow;'>Hometown:</span> " + user.getHometown() + "</html>");

            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI.ManageUsersGUI("admin").setVisible(true));
    }
}