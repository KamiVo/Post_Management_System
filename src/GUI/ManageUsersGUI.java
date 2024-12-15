package GUI;

import Function.addUser;

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
        setLayout(null); // Using null layout for manual placement of components

        JPanel leftPanel = createLeftPanel(username);
        leftPanel.setBounds(0, 0, 500, 900);
        add(leftPanel);

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

    private JPanel createAddUserPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel NameLabel = new JLabel("Add Username:");
        NameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField addNameText = new JTextField(18);

        JLabel HometownLabel = new JLabel("Add Hometown:");
        HometownLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField addHometownText = new JTextField(18);

        JLabel AgeLabel = new JLabel("Add Age:");
        AgeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField addAgeText = new JTextField(18);

        JButton SubmitButton = createButton("Submit");

        SubmitButton.addActionListener(e -> {
            if(addNameText.getText().isEmpty() || addHometownText.getText().isEmpty() || addAgeText.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please fill in all fields");
            } else {
                new addUser(addNameText.getText(), addHometownText.getText(), Integer.parseInt(addAgeText.getText()));
                addNameText.setText("");
                addHometownText.setText("");
                addAgeText.setText("");
            }
        });

        addAgeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    SubmitButton.doClick();
                }
            }
        });

        JButton BackButton = createButton("Back");

        BackButton.addActionListener(e -> cardLayout.show(mainRightPanel, "Main"));

        gbc.gridx = 0;
        panel.add(NameLabel, gbc);

        gbc.gridx = 1;
        panel.add(addNameText, gbc);

        gbc.gridx = 0;
        panel.add(HometownLabel, gbc);

        gbc.gridx = 1;
        panel.add(addHometownText, gbc);

        gbc.gridx = 0;
        panel.add(AgeLabel, gbc);

        gbc.gridx = 1;
        panel.add(addAgeText, gbc);

        gbc.gridx = 0;
        panel.add(SubmitButton, gbc);

        gbc.gridx = 1;
        panel.add(BackButton, gbc);

        return panel;
    }

    private JPanel createLeftPanel(String username) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 0, 0)); // Black background for contrast

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo
        JLabel logo = createScaledLogo("C:\\Users\\admin\\Downloads\\user.png", 100, 100); // Add your logo image path

        panel.add(logo, gbc);

        // Username
        JLabel userLabel = new JLabel(username, SwingConstants.CENTER);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel, gbc);

        // Buttons
        JButton addUserButton = createButton("Add User");
        JButton viewUsersButton = createButton("View Users");
        JButton updateUserButton = createButton("Update User");
        JButton deleteUserButton = createButton("Delete User");
        JButton backToMainButton = createButton("Back to Main");

        addUserButton.addActionListener(e -> cardLayout.show(mainRightPanel, "Add User"));

        // Add buttons to the panel
        panel.add(addUserButton, gbc);
        panel.add(viewUsersButton, gbc);
        panel.add(updateUserButton, gbc);
        panel.add(deleteUserButton, gbc);
        panel.add(backToMainButton, gbc);

        return panel;
    }

    private JLabel createScaledLogo(String imagePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1)); // Subtle border

        return button;
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
