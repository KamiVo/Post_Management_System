import javax.swing.*;
import java.awt.*;

public class MainDashboardGUI extends JFrame {
    private String username;

    public MainDashboardGUI(String username) {
        this.username = username;
        setTitle("User and Post Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        add(topPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboardGUI("User").setVisible(true));
    }
}