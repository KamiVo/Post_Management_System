import GUI.ManagePostsGUI;
import GUI.ManageUsersGUI;

import javax.swing.*;
import java.awt.*;


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

        midPanel.add(buttonPanel, gbc);
        add(midPanel, BorderLayout.CENTER);

    }

    private static JPanel getTopPanel(String username) {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Welcome, " + username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 25));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appLabel = new JLabel("User and Post Management System", SwingConstants.CENTER);
        appLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        appLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(userLabel);
        topPanel.add(appLabel);
        return topPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboardGUI("User").setVisible(true));
    }
}