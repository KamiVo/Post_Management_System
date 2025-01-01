package GUI;

import Main.UserRole;

import javax.swing.*;
import java.awt.*;

public class ManagePostsGUI extends JFrame {
    private final String username;

    public ManagePostsGUI(String username) {
        this.username = username;
        setTitle("Post Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = getTopPanel(username);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
        JButton[] buttons = new JButton[4];
        String[] buttonText = {"Add Post", "Edit Post", "Delete Post", "View Posts"};
        for (int i = 0; i < 4; i++) {
            buttons[i] = createSquareButton(buttonText[i]);
            buttons[i].setPreferredSize(new Dimension(200, 50));
            buttonPanel.add(buttons[i]);
        }

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createSquareButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 100));
        button.addActionListener(_ -> handleButtonClick(text));
        return button;
    }

    private void handleButtonClick(String action) {
        String role = UserRole.getUserRole(username);
        if ("user".equals(role) && !action.equals("View Posts")) {
            JOptionPane.showMessageDialog(this, "You do not have permission to perform this action", "Access Denied", JOptionPane.WARNING_MESSAGE);
        } else {
            // Handle the action (e.g., open a new window for adding/editing/deleting/viewing posts)
        }
    }

    private JPanel getTopPanel(String username) {
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel("Welcome, " + username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(userLabel, BorderLayout.WEST);
        JLabel appLabel = new JLabel("Post Management System", SwingConstants.CENTER);
        appLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        topPanel.add(appLabel, BorderLayout.CENTER);

        return topPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagePostsGUI("User").setVisible(true));
    }
}