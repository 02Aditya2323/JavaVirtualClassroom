package com.virtualclassroom.ui;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Virtual Classroom - Login");
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel with background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(UIConstants.MAIN_BG);

        // Center panel for login form
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(UIConstants.DANGER);

        // Login card
        JPanel loginCard = UIConstants.createCard();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
//        loginCard.setPreferredSize(new Dimension(380, 330));

        // Header
        JLabel headerLabel = new JLabel("Login to Virtual Classroom");
        headerLabel.setFont(UIConstants.HEADER_FONT);
        headerLabel.setForeground(UIConstants.TEXT_PRIMARY);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(350, 50));

        JButton loginBtn = new JButton("Login");
        UIConstants.styleButton(loginBtn, UIConstants.PRIMARY);
        loginBtn.addActionListener(e -> handleLogin());

        JButton registerBtn = new JButton("Register");
        UIConstants.styleButton(registerBtn, UIConstants.SUCCESS);
        registerBtn.addActionListener(e -> openRegisterFrame());

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        // Add components to login card with spacing
        loginCard.add(headerLabel);
        loginCard.add(Box.createVerticalStrut(25));
        loginCard.add(usernameLabel);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(usernameField);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(passwordLabel);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(passwordField);
        loginCard.add(Box.createVerticalStrut(15)); // You can make this spacer smaller
        loginCard.add(Box.createVerticalGlue());
        loginCard.add(buttonPanel);
        loginCard.add(Box.createVerticalStrut(20)); // <-- Add a new spacer for bottom padding

        centerPanel.add(loginCard);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        // Enter key to login
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = DatabaseManager.authenticateUser(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Welcome, " + user.getName() + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

            // Open appropriate dashboard based on role
            if (user.getRole().equalsIgnoreCase("Teacher")) {
                new TeacherDashboard(user);
            } else {
                new StudentDashboard(user);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password!",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void openRegisterFrame() {
        dispose();
        new RegisterFrame();
    }
}