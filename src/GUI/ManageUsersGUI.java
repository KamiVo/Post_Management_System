package GUI;

import Function.addUser;
import Function.viewUser;
import Main.LoginRegisterGUI;
import Main.MainDashboardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public class ManageUsersGUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainRightPanel;

    public ManageUsersGUI(String username) {
        setTitle("User Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Main layered pane to manage layers
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.setLayout(null);

        // Left panel
        JPanel leftPanel = createLeftPanel(username);
        leftPanel.setBounds(0, 0, getWidth() / 3, getHeight());
        layeredPane.add(leftPanel, JLayeredPane.DEFAULT_LAYER);

        // Card layout for the right panel
        cardLayout = new CardLayout();
        mainRightPanel = new JPanel(cardLayout);
        mainRightPanel.setBounds(getWidth() / 3, 0, getWidth() - (getWidth() / 3), getHeight());
        mainRightPanel.setBackground(Color.decode("#2C2C2C"));

        mainRightPanel.add(createWelcomePanel(username), "Main");
        mainRightPanel.add(createAddUserPanel(), "Add User");
        mainRightPanel.add(createViewPanel(), "View Users");

        layeredPane.add(mainRightPanel, JLayeredPane.DEFAULT_LAYER);

        // Logout button panel (added to a higher layer)
        JPanel topEastPanel = createLogoutButtonPanel();
        topEastPanel.setBounds(getWidth() - 130, 10, 90, 35);
        layeredPane.add(topEastPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);

        // Show the default card
        cardLayout.show(mainRightPanel, "Main");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                layeredPane.setBounds(0, 0, newWidth, newHeight);
                leftPanel.setBounds(0, 0, newWidth / 3, newHeight);
                topEastPanel.setBounds(newWidth - 130, 10, 90, 35);
                mainRightPanel.setBounds(newWidth / 3, 0, newWidth - (newWidth / 3), newHeight);
                revalidate();
            }
        });
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new viewUser(), gbc);

        return panel;
    }

    private JPanel createWelcomePanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("Welcome " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddUserPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Add Username:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setForeground(Color.WHITE);

        JTextField nameField = new JTextField(20);

        JLabel hometownLabel = new JLabel("Add Hometown:");
        hometownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        hometownLabel.setForeground(Color.WHITE);

        JTextField hometownField = new JTextField(20);

        JLabel ageLabel = new JLabel("Add Age:");
        ageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        ageLabel.setForeground(Color.WHITE);

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

        JButton exitButton = createButton("Exit");
        exitButton.addActionListener(_ -> {
            new MainDashboardGUI("admin").setVisible(true);
            dispose();
        });
        panel.add(exitButton, gbc);

        return panel;
    }

    private JPanel createLogoutButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);

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
        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/user.png")));
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageUsersGUI("User").setVisible(true));
    }
}
