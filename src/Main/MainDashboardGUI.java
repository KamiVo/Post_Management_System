package Main;

import GUI.ManagePostsGUI;
import GUI.ManageUsersGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainDashboardGUI extends JFrame {
    private final String username;

    public MainDashboardGUI(String username) {
        this.username = username;
        setTitle("User and Post Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new BorderLayout());
        add(gradientPanel, BorderLayout.CENTER);

        String role = UserRole.getUserRole(username);

        JPanel topPanel = getTopPanel(username);
        topPanel.add(getLogoutButtonPanel(), BorderLayout.EAST);

        JPanel midPanel = getMidPanel(role);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(midPanel, BorderLayout.CENTER);

        gradientPanel.add(mainPanel, BorderLayout.CENTER);

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
        topPanel.setPreferredSize(new Dimension(1600, 100));
        topPanel.setOpaque(false);

        JLabel mainLabel = new JLabel("Welcome " + username + " to the User and Post Management System", JLabel.CENTER);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 30));
        topPanel.add(mainLabel, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel getMidPanel(String role) {
        JPanel midPanel = new JPanel(new GridBagLayout());
        midPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel topMidPanel = new JPanel();
        topMidPanel.setOpaque(false);
        topMidPanel.setLayout(new BoxLayout(topMidPanel, BoxLayout.Y_AXIS));
        JLabel midLabel = new JLabel("Select an option to manage users or posts", JLabel.CENTER);
        midLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        topMidPanel.add(midLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        midPanel.add(topMidPanel, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
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
        button.setOpaque(false);
        styleButton(button, new Color(0x30A60A), Color.WHITE);
        button.setPreferredSize(new Dimension(250, 40));
        button.addActionListener(_ -> {
            new ManageUsersGUI(this.username).setVisible(true);
            dispose();
        });
        return button;
    }

    private JButton mnPostButton() {
        JButton button = new JButton("Manage Posts");
        button.setOpaque(false);
        styleButton(button, new Color(0xF38464), Color.WHITE);
        button.setPreferredSize(new Dimension(250, 40));
        button.addActionListener(_ -> {
            new ManagePostsGUI(this.username).setVisible(true);
            dispose();
        });
        return button;
    }

    private JButton createButton() {
        JButton button = new JButton("Logout");
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(100, 30));
        button.setFocusPainted(false);
        return button;
    }

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

    private JPanel getLogoutButtonPanel() {
        JPanel logoutButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutButtonPanel.setOpaque(false);
        JButton logoutButton = createButton();
        styleButton(logoutButton, new Color(0x000000), Color.WHITE);
        logoutButton.addActionListener(_ -> {
            new LoginRegisterGUI().setVisible(true);
            dispose();
        });
        logoutButtonPanel.add(logoutButton);
        return logoutButtonPanel;
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
        SwingUtilities.invokeLater(() -> new MainDashboardGUI("Admin").setVisible(true));
    }
}