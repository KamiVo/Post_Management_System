package Main;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginRegisterGUI extends JFrame {
    public static Connection connection;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    int width = 400;
    int height = 650;

    public LoginRegisterGUI() {
        setTitle("User and Post Management System");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setExtendedState(JFrame.NORMAL);

        setLayout(new BorderLayout());
        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new BorderLayout());
        add(gradientPanel, BorderLayout.CENTER);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBounds(width / 3, 0, width - (width / 3), height);
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
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Title Label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(243, 33, 148));

        // Username Label
        JLabel userLabel = new JLabel("Username or Email:");
        userLabel.setFont(new Font("Roboto", Font.PLAIN, 18));

        // Username Field
        JTextField userText = createModernTextField();

        // Password Label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Roboto", Font.PLAIN, 18));

        // Password Field
        JPasswordField passText = createModernPasswordField();

        // Login Button
        JButton loginButton = createButton("Login");
        styleButton(loginButton, new Color(76, 175, 80), Color.WHITE);// Green background
        loginPanel.add(loginButton, gbc);

        // Register Button
        JButton registerButton = createButton("Register");
        styleButton(registerButton, new Color(33, 150, 243), Color.WHITE); // Blue background

        // Add action listeners
        loginButton.addActionListener(_ -> {
            if (authenticateUser(userText.getText(), new String(passText.getPassword()))) {
                new MainDashboardGUI(userText.getText()).setVisible(true);
                userText.setText("");
                passText.setText("");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        });

        registerButton.addActionListener(_ -> {
            cardLayout.show(mainPanel, "Register");
            userText.setText("");
            passText.setText("");
        });

        // Login on Enter key
        passText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                    userText.setText("");
                    passText.setText("");
                }
            }
        });

        addComponentsToPanel(loginPanel, gbc, titleLabel, userLabel, userText, passLabel, passText);

        // Add buttons to a separate panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridy++;
        loginPanel.add(buttonPanel, gbc);

        return loginPanel;
    }


    private JPanel createRegisterPanel() {
        JPanel regPanel = new JPanel(new GridBagLayout());
        regPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Title Label
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(243, 33, 148)); // Modern blue color

        // Username or Email Label and Field
        JLabel userLabel = new JLabel("Username or Email:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Username or Email Field
        JTextField userText = createModernTextField();

        // Password Label and Field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Password Field
        JPasswordField passText = createModernPasswordField();

        // Confirm Password Label and Field
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Confirm Password Field
        JPasswordField confirmPassText = createModernPasswordField();

        JButton registerButton = createButton("Register");
        styleButton(registerButton, new Color(76, 175, 80), Color.WHITE); // Green background

        JButton backButton = createButton("Back");
        styleButton(backButton, new Color(244, 67, 54), Color.WHITE); // Red background

        registerButton.addActionListener(_ -> {
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

        backButton.addActionListener(_ -> {
            cardLayout.show(mainPanel, "Login");
            userText.setText("");
            passText.setText("");
            confirmPassText.setText("");
        });

        // Register on Enter key
        confirmPassText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    registerButton.doClick();
                }
            }
        });

        addComponentsToPanel(regPanel, gbc, titleLabel, userLabel, userText, passLabel, passText, confirmPassLabel, confirmPassText);

        // Add buttons to a separate panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        gbc.gridy++;
        regPanel.add(buttonPanel, gbc);

        return regPanel;
    }

    private void addComponentsToPanel(JPanel panel, GridBagConstraints gbc, JComponent... components) {
        for (int i = 0; i < components.length; i++) {
            gbc.gridx = 0; // Keep x position constant
            gbc.gridy = i; // Increment y position for each component
            gbc.anchor = GridBagConstraints.CENTER; // Center alignment
            panel.add(components[i], gbc);
        }
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.setFocusPainted(false);
        return button;
    }

    private boolean authenticateUser(String identifier, String password) {
        try {
            String query = identifier.contains("@") ? "SELECT * FROM user WHERE email = ? AND password = ?" : "SELECT * FROM user WHERE username = ? AND password = ?";
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
            String checkQuery = identifier.contains("@") ? "SELECT * FROM user WHERE email = ?" : "SELECT * FROM user WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, identifier);
            ResultSet checkResultSet = checkStatement.executeQuery();

            if (checkResultSet.next()) {
                JOptionPane.showMessageDialog(this, "Username or Email already exists");
                return false;
            }

            // Insert new user
            String insertQuery = identifier.contains("@") ?
                    "INSERT INTO user(email, username, password, avatar, role_id) VALUES(?, ?, ?, 'src/Resources/avatars/user.png', 2)" :
                    "INSERT INTO user(username, email, password, avatar, role_id) VALUES(?, ?, ?, 'src/Resources/avatars/user.png', 2)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, identifier);
            insertStatement.setString(2, identifier);
            insertStatement.setString(3, password);
            insertStatement.executeUpdate();

            int userId = 0;
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            // Auto add Age and Hometown Column in user_details table
            String insertQuery2 = "INSERT INTO user_details(id,age, hometown) VALUES (?, 0, 'Unknown')";
            PreparedStatement insertStatement2 = connection.prepareStatement(insertQuery2);
            insertStatement2.setInt(1, userId);
            insertStatement2.executeUpdate();

            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private JTextField createModernTextField() {
        JTextField textField = new JTextField(18);
        textField.setPreferredSize(new Dimension(500, 40));
        textField.setFont(new Font("Roboto", Font.PLAIN, 18));
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding
        textField.setBackground(new Color(245, 245, 245)); // Light background
        textField.setForeground(new Color(50, 50, 50)); // Dark text color
        textField.setCaretColor(new Color(33, 150, 243)); // Blue caret (cursor)
        textField.setOpaque(true);

        // Add rounded border with hover effect
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2), // Default border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(33, 150, 243), 2), // Blue border on focus
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 2), // Default border
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });

        return textField;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField passwordField = new JPasswordField(18);
        passwordField.setPreferredSize(new Dimension(500, 40));
        passwordField.setFont(new Font("Roboto", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding
        passwordField.setBackground(new Color(245, 245, 245)); // Light background
        passwordField.setForeground(new Color(50, 50, 50)); // Dark text color
        passwordField.setCaretColor(new Color(33, 150, 243)); // Blue caret (cursor)
        passwordField.setOpaque(true);

        // Add rounded border with hover effect
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2), // Default border
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding
        ));

        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(33, 150, 243), 2), // Blue border on focus
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 2), // Default border
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });

        return passwordField;
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

    // Helper method to style buttons
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Roboto", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginRegisterGUI().setVisible(true));
    }
}