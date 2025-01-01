package Main;

import GUI.ManagePostsGUI;
import GUI.ManageUsersGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainDashboardGUI extends JFrame {

    public MainDashboardGUI(String username) {
        setTitle("User and Post Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        String role = UserRole.getUserRole(username);

        JPanel topPanel = getTopPanel(username);
        topPanel.add(getLogoutButtonPanel(), BorderLayout.EAST);
        JPanel midPanel = getMidPanel(role);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(midPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
            }
        });
    }

    private JPanel getTopPanel(String username) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(100, 100));

        JLabel mainLabel = new JLabel("Welcome " + username + " to the User and Post Management System", SwingConstants.CENTER);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 30));
        mainLabel.setBounds(1000, 0, 1600, 100);
        topPanel.add(mainLabel, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel getMidPanel(String role) {
        JPanel midPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel topMidPanel = new JPanel();
        topMidPanel.setLayout(new BoxLayout(topMidPanel, BoxLayout.Y_AXIS));
        JLabel midLabel = new JLabel("Select an option to manage users or posts", JLabel.CENTER);
        midLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        topMidPanel.add(midLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        midPanel.add(topMidPanel, gbc);

        JPanel buttonPanel = new JPanel();
        if ("admin".equals(role)) {
            buttonPanel.add(mnUserButton());
        }
        buttonPanel.add(mnPostButton());

        gbc.gridy = 1;
        midPanel.add(buttonPanel, gbc);

        return midPanel;
    }

    private JButton mnUserButton() {
        JButton button = new JButton("Manage Users");
        button.setPreferredSize(new Dimension(200, 50));
        button.addActionListener(_ -> {
            new ManageUsersGUI("User").setVisible(true);
            dispose();
        });
        return button;
    }

    private JButton mnPostButton() {
        JButton button = new JButton("Manage Posts");
        button.setPreferredSize(new Dimension(200, 50));
        button.addActionListener(_ -> {
            new ManagePostsGUI("Posts").setVisible(true);
            dispose();
        });
        return button;
    }

    private JButton createButton() {
        JButton button = new JButton("Logout");
        button.setPreferredSize(new Dimension(80, 30));
        button.setFocusPainted(false);
        return button;
    }

    private JPanel getLogoutButtonPanel() {
        JPanel logoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = createButton();
        logoutButton.addActionListener(_ -> {
            new LoginRegisterGUI().setVisible(true);
            dispose();
        });
        logoutButtonPanel.add(logoutButton);
        return logoutButtonPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboardGUI("Admin").setVisible(true));
    }
}