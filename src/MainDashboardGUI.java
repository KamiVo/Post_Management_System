import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboardGUI extends JFrame {
    private String username;
    private Manage manage;
    private JPanel contentPanel;

    public MainDashboardGUI(String username) {
        this.username = username;
        this.manage = new Manage();
        setTitle("User and Post Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel("Welcome, " + username);
        JLabel appLabel = new JLabel("User and Post Management System", SwingConstants.CENTER);
        topPanel.add(userLabel, BorderLayout.WEST);
        topPanel.add(appLabel, BorderLayout.CENTER);

        JPanel menuPanel = new JPanel(new GridLayout(0, 1));
        String[] menuItems = {"Add User", "Update User", "Delete User", "Show Users", "Add Post", "Update Post", "Delete Post", "Show Posts"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.addActionListener(new MenuActionListener());
            menuPanel.add(button);
        }

        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, contentPanel);
        splitPane.setDividerLocation(200);

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String action = source.getText();
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            switch (action) {
                case "Add User":
                    manage.addUser();
                    break;
                case "Update User":
                    manage.updateUser();
                    break;
                case "Delete User":
                    manage.deleteUser();
                    break;
                case "Show Users":
                    manage.showUsers();
                    break;
                case "Add Post":
                    manage.addPost();
                    break;
                case "Update Post":
                    manage.updatePost();
                    break;
                case "Delete Post":
                    manage.deletePost();
                    break;
                case "Show Posts":
                    manage.showPosts();
                    break;
            }
        }
    }
}