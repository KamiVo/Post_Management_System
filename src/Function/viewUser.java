package Function;

import Main.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class viewUser extends JPanel {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";
    public viewUser() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#2C2C2C"));

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
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT u.id, u.username, ud.hometown, ud.age, r.name AS role " +
                             "FROM user u " +
                             "JOIN user_details ud ON u.id = ud.id " +
                             "JOIN roles r ON u.role_id = r.id")) {


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String hometown = resultSet.getString("hometown");
                int age = resultSet.getInt("age");
                String role = resultSet.getString("role");
                users.add(new User(id, username, hometown, age, role));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to get users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
            setBackground(new Color(0x0079FF));
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
            if(!"admin".equals(user.getRole())){
                idLabel.setText("<html><span style='color:yellow;'>ID:</span> " + user.getId() + "</html>");
            }else{
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