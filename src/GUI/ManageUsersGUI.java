package GUI;

import Function.addUser;
import Function.viewUser;
import Main.LoginRegisterGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Supplier;

public class ManageUsersGUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainRightPanel;
    int width = 1600;
    int height = 900;

    public ManageUsersGUI(String username) {
        setTitle("User Management System");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Create and add left panel
        JPanel leftPanel = createLeftPanel(username);
        leftPanel.setBounds(0, 0, width / 3, height);
        add(leftPanel);

        // Create and add top-right logout button panel
        JPanel topEastPanel = createLogoutButtonPanel();
        topEastPanel.setBounds(width - 130, 10, 90, 35);
        add(topEastPanel);

        // Create right panel with card layout
        cardLayout = new CardLayout();
        mainRightPanel = new JPanel(cardLayout);
        mainRightPanel.setBounds(width / 3, 0, width - (width / 3), height);

        // Add main panel to the card layout and load dynamically
        loadPanelDynamically(() -> createWelcomePanel(username));
        mainRightPanel.add(createAddUserPanel(), "Add User");
        mainRightPanel.add(new viewUser(), "View Users");
        add(mainRightPanel);

        // Show the main panel by default
        cardLayout.show(mainRightPanel, "Main");

        // Add component listener for resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                leftPanel.setBounds(0, 0, newWidth / 3, newHeight);
                topEastPanel.setBounds(newWidth - 130, 10, 90, 35);
                mainRightPanel.setBounds(newWidth / 3, 0, newWidth - (newWidth / 3), newHeight);
                revalidate();
            }
        });
    }

    private JPanel createWelcomePanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Welcome " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddUserPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Add Username:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JTextField nameField = new JTextField(20);
        JLabel hometownLabel = new JLabel("Add Hometown:");
        hometownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JTextField hometownField = new JTextField(20);
        JLabel ageLabel = new JLabel("Add Age:");
        ageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JTextField ageField = new JTextField(20);

        JButton submitButton = createButton("Submit");
        submitButton.addActionListener(_ -> handleAddUser(nameField, hometownField, ageField));

        ageField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    submitButton.doClick();
                }
            }
        });

        // Add components to the panel
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(nameLabel, gbc);
        gbc.gridy = 1;
        panel.add(hometownLabel, gbc);
        gbc.gridy = 2;
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameField, gbc);
        gbc.gridy = 1;
        panel.add(hometownField, gbc);
        gbc.gridy = 2;
        panel.add(ageField, gbc);
        gbc.gridy = 3;
        panel.add(submitButton, gbc);

        return panel;
    }

    private void handleAddUser(JTextField nameField, JTextField hometownField, JTextField ageField) {
        if (nameField.getText().isEmpty() || hometownField.getText().isEmpty() || ageField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                int age = Integer.parseInt(ageField.getText());
                new addUser(nameField.getText(), hometownField.getText(), age);
                nameField.setText("");
                hometownField.setText("");
                ageField.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Age must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createLeftPanel(String username) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0);

        JLabel logo = createScaledLogo();
        JLabel userLabel = new JLabel(username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userLabel.setForeground(Color.WHITE);

        panel.add(logo, gbc);
        panel.add(userLabel, gbc);

        panel.add(createNavigationButton("Add User", "Add User"), gbc);
        panel.add(createNavigationButton("Update User", null), gbc);
        panel.add(createNavigationButton("View Users", "View Users"), gbc);
        panel.add(createNavigationButton("Delete User", null), gbc);
        panel.add(createNavigationButton("Back to Main", "Main"), gbc);

        return panel;
    }

    private JPanel createLogoutButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(80, 30));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(_ -> {
            new LoginRegisterGUI().setVisible(true);
            dispose();
        });
        panel.add(logoutButton);

        return panel;
    }

    private JButton createNavigationButton(String text, String targetPanel) {
        JButton button = createButton(text);
        if (targetPanel != null) {
            button.addActionListener(_ -> cardLayout.show(mainRightPanel, targetPanel));
        }
        return button;
    }

    private JLabel createScaledLogo() {
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\admin\\Downloads\\user.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        return button;
    }

    private void loadPanelDynamically(Supplier<JPanel> panelSupplier) {
        JPanel panel = panelSupplier.get();
        mainRightPanel.add(panel, "Main");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageUsersGUI("User").setVisible(true));
    }
}