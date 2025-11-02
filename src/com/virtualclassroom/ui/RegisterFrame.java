package com.virtualclassroom.ui;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;

    public RegisterFrame() {
        setTitle("Virtual Classroom - Register");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(UIConstants.MAIN_BG);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(UIConstants.MAIN_BG);

        // Register card
        JPanel registerCard = UIConstants.createCard();
        registerCard.setLayout(new BoxLayout(registerCard, BoxLayout.Y_AXIS));
        registerCard.setPreferredSize(new Dimension(380, 450));

        // Header
        JLabel headerLabel = new JLabel("Create New Account");
        headerLabel.setFont(UIConstants.HEADER_FONT);
        headerLabel.setForeground(UIConstants.TEXT_PRIMARY);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Name field
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(UIConstants.BODY_FONT);
        nameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = new JTextField();
        UIConstants.styleTextField(nameField);
        nameField.setMaximumSize(new Dimension(350, UIConstants.FIELD_HEIGHT));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(UIConstants.BODY_FONT);
        usernameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        UIConstants.styleTextField(usernameField);
        usernameField.setMaximumSize(new Dimension(350, UIConstants.FIELD_HEIGHT));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(UIConstants.BODY_FONT);
        passwordLabel.setForeground(UIConstants.TEXT_PRIMARY);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        UIConstants.styleTextField(passwordField);
        passwordField.setMaximumSize(new Dimension(350, UIConstants.FIELD_HEIGHT));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Confirm password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(UIConstants.BODY_FONT);
        confirmPasswordLabel.setForeground(UIConstants.TEXT_PRIMARY);
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmPasswordField = new JPasswordField();
        UIConstants.styleTextField(confirmPasswordField);
        confirmPasswordField.setMaximumSize(new Dimension(350, UIConstants.FIELD_HEIGHT));
        confirmPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Role selection
        JLabel roleLabel = new JLabel("Register as:");
        roleLabel.setFont(UIConstants.BODY_FONT);
        roleLabel.setForeground(UIConstants.TEXT_PRIMARY);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        roleComboBox = new JComboBox<>(new String[]{"Student", "Teacher"});
        roleComboBox.setFont(UIConstants.BODY_FONT);
        roleComboBox.setMaximumSize(new Dimension(350, UIConstants.FIELD_HEIGHT));
        roleComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton registerBtn = new JButton("Register");
        UIConstants.styleButton(registerBtn, UIConstants.SUCCESS);
        registerBtn.addActionListener(e -> handleRegister());

        JButton backBtn = new JButton("Back to Login");
        UIConstants.styleButton(backBtn, UIConstants.TEXT_SECONDARY);
        backBtn.addActionListener(e -> backToLogin());

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        // Add components with spacing
        registerCard.add(headerLabel);
        registerCard.add(Box.createVerticalStrut(20));
        registerCard.add(nameLabel);
        registerCard.add(Box.createVerticalStrut(5));
        registerCard.add(nameField);
        registerCard.add(Box.createVerticalStrut(12));
        registerCard.add(usernameLabel);
        registerCard.add(Box.createVerticalStrut(5));
        registerCard.add(usernameField);
        registerCard.add(Box.createVerticalStrut(12));
        registerCard.add(passwordLabel);
        registerCard.add(Box.createVerticalStrut(5));
        registerCard.add(passwordField);
        registerCard.add(Box.createVerticalStrut(12));
        registerCard.add(confirmPasswordLabel);
        registerCard.add(Box.createVerticalStrut(5));
        registerCard.add(confirmPasswordField);
        registerCard.add(Box.createVerticalStrut(12));
        registerCard.add(roleLabel);
        registerCard.add(Box.createVerticalStrut(5));
        registerCard.add(roleComboBox);
        registerCard.add(Box.createVerticalStrut(20));
        registerCard.add(buttonPanel);

        centerPanel.add(registerCard);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        // Validation
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 4 characters!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if username already exists
        if (DatabaseManager.usernameExists(username)) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists! Please choose another.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create user
        User newUser = new User(name, username, password, role);
        boolean success = DatabaseManager.registerUser(newUser);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed! Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        dispose();
        new LoginFrame();
    }
}