import GUI.ManagePostsGUI;
import GUI.ManageUsersGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboardGUI extends JFrame {

    public MainDashboardGUI(String username) {

        setTitle("User and Post Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = getTopPanel(username);
        add(topPanel, BorderLayout.NORTH);

        JPanel midPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel topMidPanel = getTopMidPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        midPanel.add(topMidPanel, gbc);

        JButton mnUserBtn = new JButton("Manage Users");
        JButton mnPostBtn = new JButton("Manage Posts");

        mnUserBtn.setPreferredSize(new Dimension(200, 50));
        mnPostBtn.setPreferredSize(new Dimension(200, 50));

//        mnUserBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new ManageUsersGUI().setVisible(true);
//                dispose();
//            }
//        });
//
//        mnPostBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new ManagePostsGUI().setVisible(true);
//                dispose();
//            }
//        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(mnUserBtn);
        buttonPanel.add(mnPostBtn);

        gbc.gridy = 1;
        midPanel.add(buttonPanel, gbc);
        add(midPanel, BorderLayout.CENTER);
    }

    private JPanel getTopPanel(String username) {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setPreferredSize(new Dimension(1600, 100));

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Welcome, " + username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 40));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appLabel = new JLabel("User and Post Management System", SwingConstants.CENTER);
        appLabel.setFont(new Font("Arial", Font.PLAIN, 35));
        appLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        jPanel.add(userLabel);
        jPanel.add(appLabel);

        topPanel.add(jPanel, BorderLayout.CENTER);
        topPanel.add(getLogoutButtonPanel(), BorderLayout.EAST);
        return topPanel;
    }

    private static JPanel getTopMidPanel(){
        JPanel topMidPanel = new JPanel();
        topMidPanel.setLayout(new BoxLayout(topMidPanel, BoxLayout.Y_AXIS));
        topMidPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel midLabel = new JLabel("Select an option to manage users or posts", SwingConstants.CENTER);
        midLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        midLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topMidPanel.add(midLabel);
        return topMidPanel;
    }

    private JPanel getLogoutButtonPanel(){
        JPanel logoutButtonPanel = new JPanel();
        logoutButtonPanel.setLayout(new BoxLayout(logoutButtonPanel, BoxLayout.Y_AXIS));
        logoutButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(150, 30));

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