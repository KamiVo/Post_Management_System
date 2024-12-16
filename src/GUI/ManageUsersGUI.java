package GUI;

import Function.addUser;
import Main.LoginRegisterGUI;

import javax.swing.*;
import java.awt.*;

public class ManageUsersGUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainRightPanel;

    public ManageUsersGUI(String username) {
        setTitle("User Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel leftPanel = createLeftPanel(username);
        leftPanel.setBounds(0, 0, 500, 900);
        add(leftPanel);

        JPanel topEastPanel = getLogoutButtonPanel();
        topEastPanel.setBounds(20, 10, 80, 30);
        topEastPanel.setBackground(Color.BLACK);
        add(topEastPanel);

        cardLayout = new CardLayout();
        mainRightPanel = new JPanel(cardLayout);

        mainRightPanel.add(createRightPanel(username), "Main");
        mainRightPanel.add(createAddUserPanel(), "Add User");

        mainRightPanel.setBounds(500, 0, 1100, 900);
        add(mainRightPanel);

        cardLayout.show(mainRightPanel, "Main");
    }

    private JPanel createRightPanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Welcome " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddUserPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Add Username:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField addNameText = new JTextField(18);

        JLabel hometownLabel = new JLabel("Add Hometown:");
        hometownLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField addHometownText = new JTextField(18);

        JLabel ageLabel = new JLabel("Add Age:");
        ageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField addAgeText = new JTextField(18);

        JButton submitButton = createButton("Submit", 200, 50);
        submitButton.addActionListener(e -> handleAddUser(addNameText, addHometownText, addAgeText));

        addAgeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    submitButton.doClick();
                }
            }
        });

        JButton backButton = createButton("Back", 200, 50);
        backButton.addActionListener(e -> cardLayout.show(mainRightPanel, "Main"));

        gbc.gridx = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(addNameText, gbc);
        gbc.gridx = 0;
        panel.add(hometownLabel, gbc);
        gbc.gridx = 1;
        panel.add(addHometownText, gbc);
        gbc.gridx = 0;
        panel.add(ageLabel, gbc);
        gbc.gridx = 1;
        panel.add(addAgeText, gbc);
        gbc.gridx = 0;
        panel.add(submitButton, gbc);
        gbc.gridx = 1;
        panel.add(backButton, gbc);

        return panel;
    }

    private void handleAddUser(JTextField addNameText, JTextField addHometownText, JTextField addAgeText) {
        if (addNameText.getText().isEmpty() || addHometownText.getText().isEmpty() || addAgeText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields");
        } else {
            new addUser(addNameText.getText(), addHometownText.getText(), Integer.parseInt(addAgeText.getText()));
            addNameText.setText("");
            addHometownText.setText("");
            addAgeText.setText("");
        }
    }

    private JPanel createLeftPanel(String username) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel logo = createScaledLogo("C:\\Users\\admin\\Downloads\\user.png", 100, 100);
        panel.add(logo, gbc);

        JLabel userLabel = new JLabel(username, SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel, gbc);

        JButton addUserButton = createButton("Add User", 200, 50);
        addUserButton.addActionListener(e -> cardLayout.show(mainRightPanel, "Add User"));
        panel.add(addUserButton, gbc);

        panel.add(createButton("Update User", 200, 50), gbc);
        panel.add(createButton("View Users", 200, 50), gbc);
        panel.add(createButton("Delete User", 200, 50), gbc);
        panel.add(createButton("Back to Main", 200, 50), gbc);

        return panel;
    }

    private JLabel createScaledLogo(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
        return button;
    }

    private JPanel getLogoutButtonPanel() {
        JPanel logoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = createButton("Logout", 80, 30);
        logoutButton.addActionListener(e -> {
            new LoginRegisterGUI().setVisible(true);
            dispose();
        });
        logoutButtonPanel.add(logoutButton);
        return logoutButtonPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ManageUsersGUI("User").setVisible(true);
        });
    }
}