package Main;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginRegisterGUI extends JFrame {
    public static Connection connection;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public LoginRegisterGUI() {
        setTitle("User and Post Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new BorderLayout());
        add(gradientPanel, BorderLayout.CENTER);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false); // Make main panel transparent

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");

        gradientPanel.add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "Login");

        initializeDBConnection();
    }

    public void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "K@miVo_02825");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createLoginPanel() {
        JPanel logPanel = new JPanel(new GridBagLayout());
        logPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userLabel = new JLabel("Username or Email:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField userText = new JTextField(18);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JPasswordField passText = new JPasswordField(18);

        JButton loginButton = createButton("Login", 150, 30);
        JButton registerButton = createButton("Register", 150, 30);

        loginButton.addActionListener(e -> {
            if (authenticateUser(userText.getText(), new String(passText.getPassword()))) {
                new MainDashboardGUI(userText.getText()).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        });

        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        passText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        addComponentsToPanel(logPanel, gbc, userLabel, userText, passLabel, passText, loginButton, registerButton);
        return logPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel regPanel = new JPanel(new GridBagLayout());
        regPanel.setOpaque(false);
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

        JButton registerButton = createButton("Register", 150, 30);
        JButton backButton = createButton("Back", 150, 30);

        registerButton.addActionListener(e -> {
            try {
                if (new String(passText.getPassword()).equals(new String(confirmPassText.getPassword()))) {
                    if (registerUser(userText.getText(), new String(passText.getPassword()))) {
                        switch (JOptionPane.showConfirmDialog(null, "Registration successful. Do you want to login?", "Success", JOptionPane.YES_NO_OPTION)) {
                            case JOptionPane.YES_OPTION:
                                cardLayout.show(mainPanel, "Login");
                                break;
                            case JOptionPane.NO_OPTION:
                                break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match");
                }
            } catch (Main.FormulaException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        addComponentsToPanel(regPanel, gbc, userLabel, userText, passLabel, passText, confirmPassLabel, confirmPassText, registerButton, backButton);
        return regPanel;
    }

    private void addComponentsToPanel(JPanel panel, GridBagConstraints gbc, JComponent... components) {
        for (int i = 0; i < components.length; i++) {
            gbc.gridx = i % 2 == 0 ? 0 : 1;
            gbc.gridy = i / 2;
            gbc.anchor = i % 2 == 0 ? GridBagConstraints.EAST : GridBagConstraints.WEST;
            panel.add(components[i], gbc);
        }
    }

    private JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusPainted(false);
        return button;
    }

    private boolean authenticateUser(String identifier, String password) {
        try {
            String query = identifier.contains("@") ? "SELECT * FROM loginregister WHERE email = ? AND password = ?" : "SELECT * FROM loginregister WHERE username = ? AND password = ?";
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

    private boolean registerUser(String identifier, String password) throws Main.FormulaException {
        if (identifier == null || identifier.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new Main.FormulaException("Username/Email and Password cannot be blank");
        }

        try {
            String checkQuery = identifier.contains("@") ? "SELECT * FROM loginregister WHERE email = ?" : "SELECT * FROM loginregister WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, identifier);
            ResultSet checkResultSet = checkStatement.executeQuery();

            if (checkResultSet.next()) {
                JOptionPane.showMessageDialog(this, "Username or Email already exists");
                return false;
            }

            String insertQuery = identifier.contains("@") ? "INSERT INTO loginregister(email, username, password) VALUES(?, '', ?)" : "INSERT INTO loginregister(username, email, password) VALUES(?, '', ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, identifier);
            insertStatement.setString(2, password);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            Color color1 = new Color(0x33ccff);
            Color color2 = new Color(0x0066cc);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginRegisterGUI().setVisible(true));
    }
}