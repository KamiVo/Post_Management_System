import GUI.ManagePostsGUI;
import GUI.ManageUsersGUI;

import javax.swing.*;
import java.awt.*;

public class MainDashboardGUI extends JFrame {

    public MainDashboardGUI(String username) {
        setTitle("User and Post Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);

        JPanel topPanel = getTopPanel(username);
        topPanel.setBounds(50, 50, 1500, 100);
        add(topPanel);

        JPanel topEastPanel = getLogoutButtonPanel();
        topEastPanel.setBounds(20, 10, 1500, 100);
        add(topEastPanel);

        JPanel midPanel = getMidPanel();
        midPanel.setBounds(50, 200, 1500, 600);
        add(midPanel);
    }

    private JPanel getTopPanel(String username) {
        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(1500, 100));

        JLabel mainLabel = new JLabel("Welcome " + username + " to the User and Post Management System", SwingConstants.CENTER);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 30));
        mainLabel.setBounds(100, 20, 1300, 60);

        JPanel labelsPanel = new JPanel(null);
        labelsPanel.setBounds(0, 0, 1500, 100);
        labelsPanel.add(mainLabel);

        topPanel.add(labelsPanel);
//        topPanel.add(getLogoutButtonPanel(), BorderLayout.EAST);

        return topPanel;
    }

    private JPanel getMidPanel() {
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
        buttonPanel.add(createButton("Manage Users", 200, 50));
        buttonPanel.add(createButton("Manage Posts", 200, 50));

        gbc.gridy = 1;
        midPanel.add(buttonPanel, gbc);

        return midPanel;
    }

    private JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
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
        SwingUtilities.invokeLater(() -> new MainDashboardGUI("User").setVisible(true));
    }
}