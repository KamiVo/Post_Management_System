package GUI;

import Function.addUser;
import Function.editUser;
import Function.viewUser;
import Main.LoginRegisterGUI;
import Main.MainDashboardGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageUsersGUI extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainRightPanel;
    private final JPanel editUserCardPanel;
    private final CardLayout editUserCardLayout;
    private int userIdToEdit;
    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USER = "root";
    private static final String PASSWORD = "K@miVo_02825";
    private boolean editAllSelected;
    private List<JTextField> editTextFields;

    public ManageUsersGUI(String username) {
        setTitle("User Management System");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.setLayout(null);

        JPanel leftPanel = createLeftPanel(username);
        leftPanel.setBounds(0, 0, getWidth() / 3, getHeight());
        layeredPane.add(leftPanel, JLayeredPane.DEFAULT_LAYER);

        cardLayout = new CardLayout();
        mainRightPanel = new JPanel(cardLayout);
        mainRightPanel.setBounds(getWidth() / 3, 0, getWidth() - (getWidth() / 3), getHeight());
        mainRightPanel.setBackground(Color.decode("#2C2C2C"));

        mainRightPanel.add(createWelcomePanel(username), "Main");
        mainRightPanel.add(createAddUserPanel(), "Add User");
        mainRightPanel.add(createViewPanel(), "View Users");

        editUserCardLayout = new CardLayout();
        editUserCardPanel = new JPanel(editUserCardLayout);
        editUserCardPanel.setOpaque(false);
        editUserCardPanel.add(createEditUserIdPanel(), "EditUserId");
        editUserCardPanel.add(createEditUserSelectionPanel(), "EditUserSelection");
        editUserCardPanel.add(createEditUserEditPanel(), "EditUserEdit");

        mainRightPanel.add(editUserCardPanel, "Edit User");

        layeredPane.add(mainRightPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel topEastPanel = createLogoutButtonPanel();
        topEastPanel.setBounds(getWidth() - 130, 10, 90, 35);
        layeredPane.add(topEastPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);
        cardLayout.show(mainRightPanel, "Main");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                layeredPane.setBounds(0, 0, newWidth, newHeight);
                leftPanel.setBounds(0, 0, newWidth / 3, newHeight);
                topEastPanel.setBounds(newWidth - 130, 10, 90, 35);
                mainRightPanel.setBounds(newWidth / 3, 0, newWidth - (newWidth / 3), newHeight);
                revalidate();
            }
        });
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new viewUser(), gbc);

        return panel;
    }

    private JPanel createWelcomePanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("Welcome " + username, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAddUserPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Add Username:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setForeground(Color.WHITE);

        JTextField nameField = new JTextField(20);

        JLabel hometownLabel = new JLabel("Add Hometown:");
        hometownLabel.setFont(new Font("Arial", Font.BOLD, 20));
        hometownLabel.setForeground(Color.WHITE);

        JTextField hometownField = new JTextField(20);

        JLabel ageLabel = new JLabel("Add Age:");
        ageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        ageLabel.setForeground(Color.WHITE);

        JTextField ageField = new JTextField(20);

        JButton submitButton = createButton("Submit");
        submitButton.addActionListener(_ -> handleAddUser(nameField, hometownField, ageField));

        ageField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    submitButton.doClick();
                }
            }
        });

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(nameLabel, gbc);

        gbc.gridy = 1;
        panel.add(hometownLabel, gbc);

        gbc.gridy = 2;
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameField, gbc);

        gbc.gridy = 1;
        panel.add(hometownField, gbc);

        gbc.gridy = 2;
        panel.add(ageField, gbc);

        gbc.gridy = 3;
        panel.add(submitButton, gbc);

        return panel;
    }


    private JPanel createEditUserIdPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel idLabel = new JLabel("Enter User ID to Edit:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 20));
        idLabel.setForeground(Color.WHITE);
        JTextField idField = new JTextField(20);

        JButton fetchUserButton = createButton("Next");
        fetchUserButton.addActionListener(_ -> handleFetchUser(idField.getText()));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(idField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 1;
        panel.add(fetchUserButton, gbc);

        return panel;
    }

    private void handleFetchUser(String idText) {
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            if (!isUserExist(id)) {
                JOptionPane.showMessageDialog(this, "User ID not identified, Please enter again", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isUserAdmin(id)) {
                JOptionPane.showMessageDialog(this, "Admin user cannot be edited", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userIdToEdit = id;
            editUserCardLayout.show(editUserCardPanel, "EditUserSelection");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isUserAdmin(int userId) {
        String sql = "SELECT role_id FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int roleId = resultSet.getInt("role_id");
                    return roleId == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isUserExist(int userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private JPanel createEditUserSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel editLabel = new JLabel("Select what to edit:");
        editLabel.setFont(new Font("Arial", Font.BOLD, 20));
        editLabel.setForeground(Color.WHITE);
        panel.add(editLabel, gbc);

        JCheckBox editNameCheckBox = new JCheckBox("Name");
        editNameCheckBox.setFont(new Font("Arial", Font.BOLD, 18));

        JCheckBox editHometownCheckBox = new JCheckBox("Hometown");
        editHometownCheckBox.setFont(new Font("Arial", Font.BOLD, 18));

        JCheckBox editAgeCheckBox = new JCheckBox("Age");
        editAgeCheckBox.setFont(new Font("Arial", Font.BOLD, 18));

        JCheckBox editAllCheckBox = new JCheckBox("All");
        editAllCheckBox.setFont(new Font("Arial", Font.BOLD, 18));

        editNameCheckBox.setOpaque(false);
        editNameCheckBox.setForeground(Color.WHITE);
        editHometownCheckBox.setOpaque(false);
        editHometownCheckBox.setForeground(Color.WHITE);
        editAgeCheckBox.setOpaque(false);
        editAgeCheckBox.setForeground(Color.WHITE);
        editAllCheckBox.setOpaque(false);
        editAllCheckBox.setForeground(Color.WHITE);

        editAllCheckBox.addActionListener(e -> {
            boolean selected = editAllCheckBox.isSelected();
            editNameCheckBox.setSelected(selected);
            editHometownCheckBox.setSelected(selected);
            editAgeCheckBox.setSelected(selected);
            editNameCheckBox.setEnabled(!selected);
            editHometownCheckBox.setEnabled(!selected);
            editAgeCheckBox.setEnabled(!selected);
            editAllSelected = selected;
        });
        editAllSelected = editAllCheckBox.isSelected();

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(editNameCheckBox, gbc);
        gbc.gridy++;
        panel.add(editHometownCheckBox, gbc);
        gbc.gridy++;
        panel.add(editAgeCheckBox, gbc);
        gbc.gridy++;
        panel.add(editAllCheckBox, gbc);

        JButton nextButton = createButton("Next");
        nextButton.addActionListener(_ -> handleEditSelection(editNameCheckBox.isSelected(),
                editHometownCheckBox.isSelected(),
                editAgeCheckBox.isSelected(),
                editAllCheckBox.isSelected(),
                panel
        ));

        gbc.gridy++;
        panel.add(nextButton, gbc);

        JButton backButton = createButton("Back");
        backButton.addActionListener(_ -> editUserCardLayout.show(editUserCardPanel, "EditUserId"));
        gbc.gridy++;
        panel.add(backButton, gbc);

        return panel;
    }


    private void handleEditSelection(boolean editName, boolean editHometown, boolean editAge, boolean editAll, JPanel selectionPanel) {
        if(!editName && !editHometown && !editAge && !editAll){
            JOptionPane.showMessageDialog(this, "Please select at least one option", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        editUserCardLayout.show(editUserCardPanel, "EditUserEdit");
        JPanel editPanel = (JPanel) editUserCardPanel.getComponent(2);
        editPanel.removeAll();
        editTextFields = new ArrayList<>();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        int gridY = 0;

        if (editName || editAll) {
            JLabel newNameLabel = new JLabel("New Name:");
            newNameLabel.setForeground(Color.WHITE);
            newNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
            JTextField newNameField = new JTextField(20);
            newNameField.setVisible(true);
            editTextFields.add(newNameField);
            gbc.gridx = 0;
            gbc.gridy = gridY++;
            gbc.anchor = GridBagConstraints.EAST;
            editPanel.add(newNameLabel, gbc);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            editPanel.add(newNameField, gbc);

        }

        if (editHometown || editAll) {
            JLabel newHometownLabel = new JLabel("New Hometown:");
            newHometownLabel.setForeground(Color.WHITE);
            newHometownLabel.setFont(new Font("Arial", Font.BOLD, 20));
            JTextField newHometownField = new JTextField(20);
            newHometownField.setVisible(true);
            editTextFields.add(newHometownField);
            gbc.gridx = 0;
            gbc.gridy = gridY++;
            gbc.anchor = GridBagConstraints.EAST;
            editPanel.add(newHometownLabel, gbc);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            editPanel.add(newHometownField, gbc);

        }

        if (editAge || editAll) {
            JLabel newAgeLabel = new JLabel("New Age:");
            newAgeLabel.setForeground(Color.WHITE);
            newAgeLabel.setFont(new Font("Arial", Font.BOLD, 20));
            JTextField newAgeField = new JTextField(20);
            newAgeField.setVisible(true);
            editTextFields.add(newAgeField);
            gbc.gridx = 0;
            gbc.gridy = gridY++;
            gbc.anchor = GridBagConstraints.EAST;
            editPanel.add(newAgeLabel, gbc);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            editPanel.add(newAgeField, gbc);
        }
        gbc.gridx=1;
        gbc.anchor = GridBagConstraints.WEST;


        JButton submitButton = createButton("Submit");
        submitButton.addActionListener(_ -> handleEditUser(userIdToEdit,
                editName || editAll ? editTextFields.size()>0 ?  editTextFields.get(0).getText() : null : null ,
                editHometown || editAll ? editTextFields.size()>1 ?  editTextFields.get(1).getText() : null : null,
                editAge || editAll ? editTextFields.size()>2 ?  editTextFields.get(2).getText() : null : null,
                editAll
        ));

        gbc.gridy = gridY++;
        editPanel.add(submitButton, gbc);

        JButton backButton = createButton("Back");
        backButton.addActionListener(_ -> editUserCardLayout.show(editUserCardPanel, "EditUserSelection"));
        gbc.gridy = gridY++;
        editPanel.add(backButton, gbc);



        editPanel.revalidate();
        editPanel.repaint();

    }

    private JPanel createEditUserEditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        return panel;
    }


    private void handleEditUser(int id, String newName, String newHometown, String newAge, boolean editAll) {
        try {
            Integer age = newAge == null ? null : Integer.parseInt(newAge);
            boolean success = new editUser().editUser(id, newName, newHometown, age, editAll);

            if (success) {
                JOptionPane.showMessageDialog(this, "User updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                editUserCardLayout.show(editUserCardPanel, "EditUserId");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age format", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddUser(JTextField nameField, JTextField hometownField, JTextField ageField) {
        if (nameField.getText().isEmpty() || hometownField.getText().isEmpty() || ageField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                int age = Integer.parseInt(ageField.getText());
                new addUser(nameField.getText(), hometownField.getText(), age);
                nameField.setText("");
                hometownField.setText("");
                ageField.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Age must be a number", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private JPanel createLeftPanel(String username) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0);

        JLabel logo = createScaledLogo();
        JLabel userLabel = new JLabel(username);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userLabel.setForeground(Color.WHITE);

        panel.add(logo, gbc);
        panel.add(userLabel, gbc);

        panel.add(createNavigationButton("Add User", "Add User"), gbc);
        styleButton((JButton) panel.getComponent(panel.getComponentCount() - 1), (new Color(0xF38464)), Color.WHITE);

        panel.add(createNavigationButton("Edit User", "Edit User"), gbc);
        styleButton((JButton) panel.getComponent(panel.getComponentCount() - 1), (new Color(0xF38464)), Color.WHITE);

        panel.add(createNavigationButton("View Users", "View Users"), gbc);
        styleButton((JButton) panel.getComponent(panel.getComponentCount() - 1), (new Color(0xF38464)), Color.WHITE);

        panel.add(createNavigationButton("Delete User", null), gbc);
        styleButton((JButton) panel.getComponent(panel.getComponentCount() - 1), (new Color(0xF38464)), Color.WHITE);

        JButton exitButton = createButton("Exit");
        styleButton(exitButton, (new Color(0xF38464)), Color.WHITE);
        exitButton.addActionListener(_ -> {
            new MainDashboardGUI("admin").setVisible(true);
            dispose();
        });
        panel.add(exitButton, gbc);

        return panel;
    }

    private JPanel createLogoutButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(80, 30));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton.setFocusPainted(false);

        logoutButton.addActionListener(_ -> {
            new LoginRegisterGUI().setVisible(true);
            dispose();
        });

        panel.add(logoutButton);

        return panel;
    }

    private JButton createNavigationButton(String text, String targetPanel) {
        JButton button = createButton(text);
        button.setFocusPainted(false);
        if (targetPanel != null) {
            button.addActionListener(_ -> cardLayout.show(mainRightPanel, targetPanel));
        }
        return button;
    }

    private JLabel createScaledLogo() {
        ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/user.png")));
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaledImage));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageUsersGUI("User").setVisible(true));
    }
}