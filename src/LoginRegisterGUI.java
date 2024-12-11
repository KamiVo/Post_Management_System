import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Custom exception for formula errors
class FormulaException extends Exception {
    public FormulaException(String message) {
        super(message);
    }
}

public class LoginRegisterGUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private Connection connection;

    public LoginRegisterGUI() {
        setTitle("User and Post Management System");
        setSize(1600, 900);
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

        initializeDBConnection();
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "K@miVo_02825");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Username or Email:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField userText = new JTextField(18);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JPasswordField passText = new JPasswordField(18);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        Dimension buttonSize = new Dimension(150, 30);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Username or Email:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField userText = new JTextField(20);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JPasswordField passText = new JPasswordField(20);

        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JPasswordField confirmPassText = new JPasswordField(20);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        Dimension buttonSize = new Dimension(150, 30);
        registerButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (new String(passText.getPassword()).equals(new String(confirmPassText.getPassword()))) {
                        if (registerUser(userText.getText(), new String(passText.getPassword()))) {
                            cardLayout.show(mainPanel, "Login");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Passwords do not match");
                    }
                } catch (FormulaException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Login");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel.add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel.add(passText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel.add(confirmPassLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel.add(confirmPassText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        jPanel.add(registerButton, gbc);

        gbc.gridy = 4;
        jPanel.add(backButton, gbc);

        return jPanel;
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
            JOptionPane.showMessageDialog(this, "Authentication failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean registerUser(String identifier, String password) throws FormulaException {
        if (identifier == null || identifier.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new FormulaException("Username/Email and Password cannot be blank");
        }

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

            if (checkResultSet.next()) {
                JOptionPane.showMessageDialog(this, "Username or Email already exists");
                return false; // User already exists
            }

            String insertQuery;
            if (identifier.contains("@")) {
                insertQuery = "INSERT INTO loginregister(email, username, password) VALUES(?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, identifier);
                insertStatement.setString(2, ""); // Set username to empty string
                insertStatement.setString(3, password);
                insertStatement.executeUpdate();
            } else {
                insertQuery = "INSERT INTO loginregister(username, email, password) VALUES(?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, identifier);
                insertStatement.setString(2, ""); // Set email to empty string
                insertStatement.setString(3, password);
                insertStatement.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginRegisterGUI().setVisible(true));
    }
}