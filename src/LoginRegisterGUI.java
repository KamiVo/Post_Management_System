import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginRegisterGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Connection connection;

    public LoginRegisterGUI() {
        setTitle("User and Post Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");

        // Initialize database connection
        initializeDBConnection();
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "K@miVo_02825");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel userLabel = new JLabel("Username or Email:");
        JTextField userText = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (authenticateUser(userText.getText(), new String(passText.getPassword()))) {
                    new MainDashboardGUI(userText.getText()).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Register");
            }
        });

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passLabel);
        panel.add(passText);
        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel userLabel = new JLabel("Username or Email:");
        JTextField userText = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passText = new JPasswordField();
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPassText = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new String(passText.getPassword()).equals(new String(confirmPassText.getPassword()))) {
                    if (registerUser(userText.getText(), new String(passText.getPassword()))) {
                        cardLayout.show(mainPanel, "Login");
                    } else {
                        JOptionPane.showMessageDialog(null, "Username or Email already exists");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Login");
            }
        });

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passLabel);
        panel.add(passText);
        panel.add(confirmPassLabel);
        panel.add(confirmPassText);
        panel.add(registerButton);
        panel.add(backButton);

        return panel;
    }

    private boolean authenticateUser(String identifier, String password) {
        try {
            String query;
            if (identifier.contains("@")) {
                query = "SELECT * FROM loginregister WHERE email = ? AND password = ?";
            } else {
                query = "SELECT * FROM loginregister WHERE username = ? AND password = ?";
            }
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, identifier);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String identifier, String password) {
        try {
            String checkQuery;
            if (identifier.contains("@")) {
                checkQuery = "SELECT * FROM loginregister WHERE email = ?";
            } else {
                checkQuery = "SELECT * FROM loginregister WHERE username = ?";
            }
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, identifier);
            ResultSet checkResultSet = checkStatement.executeQuery();

            if(checkResultSet.next()) {
                return false;
            } else {
                String insertQuery;
                if (identifier.contains("@")) {
                    insertQuery = "INSERT INTO loginregister (email, password) VALUES (?, ?)";
                } else {
                    insertQuery = "INSERT INTO loginregister (username, password) VALUES (?, ?)";
                }
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, identifier);
                insertStatement.setString(2, password);
                insertStatement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginRegisterGUI().setVisible(true);
            }
        });
    }
}