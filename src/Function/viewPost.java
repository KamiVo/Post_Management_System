package Function;

import Main.Post;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class viewPost extends JPanel {
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";
    private final int authorId;

    public viewPost(int authorId) {
        this.authorId = authorId;
        setLayout(new BorderLayout());
        setBackground(Color.decode("#2C2C2C"));

        List<Post> posts = getPosts();
        DefaultListModel<Post> listModel = new DefaultListModel<>();
        for (Post post : posts) {
            listModel.addElement(post);
        }

        JList<Post> postList = new JList<>(listModel);
        postList.setCellRenderer(new PostCellRenderer());
        JScrollPane scrollPane = new JScrollPane(postList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1000, 600));

        add(scrollPane, BorderLayout.CENTER);
    }

    private List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        String query = authorId == -1 ? "SELECT id, title, content, author_id, date FROM posts" : "SELECT id, title, content, author_id, date FROM posts WHERE author_id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (authorId != -1) {
                preparedStatement.setInt(1, authorId);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    int authorId = resultSet.getInt("author_id");
                    String date = resultSet.getString("date");
                    posts.add(new Post(id, title, content, authorId, date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private static class PostCellRenderer extends JPanel implements ListCellRenderer<Post> {
        private final JLabel idLabel;
        private final JLabel titleLabel;
        private final JLabel contentLabel;

        public PostCellRenderer() {
            setLayout(new GridLayout(4, 1));
            setBackground(new Color(0x0079FF));
            setPreferredSize(new Dimension(250, 200));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            idLabel = createLabel();
            titleLabel = createLabel();
            contentLabel = createLabel();

            add(idLabel);
            add(titleLabel);
            add(contentLabel);
        }

        private JLabel createLabel() {
            JLabel label = new JLabel();
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setForeground(Color.WHITE);
            return label;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Post> list, Post post, int index, boolean isSelected, boolean cellHasFocus) {
            idLabel.setText("<html><span style='color:yellow;'>ID:</span> " + post.getId() + "</html>");
            titleLabel.setText("<html><span style='color:yellow;'>Title:</span> " + post.getTitle() + "</html>");
            contentLabel.setText("<html><span style='color:yellow;'>Content:</span> " + post.getContent() + "</html>");

            return this;
        }
    }
}