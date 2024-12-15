package GUI;

import javax.swing.*;
import java.awt.*;

public class ManageUsersGUI extends JFrame {
    public ManageUsersGUI(String username) {
        int countPage = 0;

        setTitle("User Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel leftPanel = createLeftPanel();
        leftPanel.setBounds(0, 0, 500, 900);
        add(leftPanel);

        JPanel rightPanel = createRightPanel();
        rightPanel.setBounds(500, 0, 1100, 900);
        rightPanel.setLayout(new BorderLayout());
        add(rightPanel);

        switch (countPage) {
            case 0:
                rightPanel.add(createMainMenu(), BorderLayout.CENTER);
                break;
            case 1:
                rightPanel.add(new JLabel("Add User"), BorderLayout.CENTER);
                break;
            case 2:
                rightPanel.add(new JLabel("View Users"), BorderLayout.CENTER);
                break;
            case 3:
                rightPanel.add(new JLabel("Update User"), BorderLayout.CENTER);
                break;
            case 4:
                rightPanel.add(new JLabel("Delete User"), BorderLayout.CENTER);
                break;
            default:
                rightPanel.add(new JLabel("Back to Main"), BorderLayout.CENTER);
                break;
        }
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(255, 87, 34)); // Modern color
        return rightPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(33, 150, 243)); // Modern color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(50, 0, 50, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JButton addUserButton = createButton("Add User");
        JButton viewUsersButton = createButton("View Users");
        JButton updateUserButton = createButton("Update User");
        JButton deleteUserButton = createButton("Delete User");
        JButton backToMainButton = createButton("Back to Main");

        leftPanel.add(addUserButton, gbc);
        leftPanel.add(viewUsersButton, gbc);
        leftPanel.add(updateUserButton, gbc);
        leftPanel.add(deleteUserButton, gbc);
        leftPanel.add(backToMainButton, gbc);

        return leftPanel;
    }

    private JLabel createMainMenu() {
        JLabel mainMenu = new JLabel("Choose your option", SwingConstants.CENTER);
        mainMenu.setFont(new Font("Arial", Font.BOLD, 30));
        mainMenu.setVerticalAlignment(SwingConstants.CENTER);
        mainMenu.setHorizontalAlignment(SwingConstants.CENTER);
        mainMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        return mainMenu;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(76, 175, 80)); // Modern color
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
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