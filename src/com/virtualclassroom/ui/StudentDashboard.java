package com.virtualclassroom.ui;

import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;
import com.virtualclassroom.ui.student.*;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private User student;
    private JPanel contentPanel;
    private JPanel sidebarPanel;

    public StudentDashboard(User student) {
        this.student = student;

        setTitle("Student Dashboard - " + student.getName());
        setSize(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());

        createSidebar();

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.MAIN_BG);

        showHomePanel();

        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(UIConstants.SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, getHeight()));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UIConstants.SIDEBAR_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel nameLabel = new JLabel(student.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(UIConstants.TEXT_WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel("Student");
        roleLabel.setFont(UIConstants.BODY_FONT);
        roleLabel.setForeground(UIConstants.TEXT_SECONDARY);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(nameLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(roleLabel);

        sidebarPanel.add(headerPanel);
        sidebarPanel.add(createSeparator());

        // Menu items
        sidebarPanel.add(createMenuItem("🏠 Home", () -> showHomePanel()));
        sidebarPanel.add(createMenuItem("📚 Lectures", () -> showLecturesPanel()));
        sidebarPanel.add(createMenuItem("✓ My Attendance", () -> showAttendancePanel()));
        sidebarPanel.add(createMenuItem("📝 Assignments", () -> showAssignmentsPanel()));

        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(createSeparator());
        sidebarPanel.add(createMenuItem("🚪 Logout", () -> logout()));
        sidebarPanel.add(Box.createVerticalStrut(20));
    }

    private JButton createMenuItem(String text, Runnable action) {
        JButton menuItem = new JButton(text);
        menuItem.setFont(UIConstants.SIDEBAR_FONT);
        menuItem.setForeground(UIConstants.TEXT_WHITE);
        menuItem.setBackground(UIConstants.SIDEBAR_BG);
        menuItem.setFocusPainted(false);
        menuItem.setBorderPainted(false);
        menuItem.setContentAreaFilled(false);
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuItem.setMaximumSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 45));
        menuItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuItem.setHorizontalAlignment(SwingConstants.LEFT);
        menuItem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(UIConstants.SIDEBAR_HOVER);
                menuItem.setContentAreaFilled(true);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuItem.setContentAreaFilled(false);
            }
        });

        menuItem.addActionListener(e -> action.run());
        return menuItem;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(UIConstants.SIDEBAR_HOVER);
        separator.setMaximumSize(new Dimension(UIConstants.SIDEBAR_WIDTH, 1));
        return separator;
    }

    private void showHomePanel() {
        contentPanel.removeAll();

        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(UIConstants.MAIN_BG);
        homePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel welcomeCard = UIConstants.createCard();
        welcomeCard.setLayout(new BoxLayout(welcomeCard, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(UIConstants.TEXT_PRIMARY);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- New Intro Label ---
        JLabel introLabel = new JLabel("Here's a quick overview of what you can do:");
        introLabel.setFont(UIConstants.BODY_FONT);
        introLabel.setForeground(UIConstants.TEXT_SECONDARY);
        introLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- New Features Panel ---
        JPanel featuresPanel = new JPanel();
        featuresPanel.setBackground(welcomeCard.getBackground()); // Match card background
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center this panel in the card
        featuresPanel.setMaximumSize(new Dimension(450, 150)); // Constrain the width

        JLabel feature1 = new JLabel("•  View Lectures: Check your daily and weekly class schedule.");
        feature1.setFont(UIConstants.BODY_FONT);
        feature1.setForeground(UIConstants.TEXT_PRIMARY);
        feature1.setAlignmentX(Component.LEFT_ALIGNMENT); // Left-align text within this panel

        JLabel feature2 = new JLabel("•  My Attendance: Review your personal attendance record.");
        feature2.setFont(UIConstants.BODY_FONT);
        feature2.setForeground(UIConstants.TEXT_PRIMARY);
        feature2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel feature3 = new JLabel("•  View Assignments: See upcoming assignments and submit your work.");
        feature3.setFont(UIConstants.BODY_FONT);
        feature3.setForeground(UIConstants.TEXT_PRIMARY);
        feature3.setAlignmentX(Component.LEFT_ALIGNMENT);

        featuresPanel.add(feature1);
        featuresPanel.add(Box.createVerticalStrut(10));
        featuresPanel.add(feature2);
        featuresPanel.add(Box.createVerticalStrut(10));
        featuresPanel.add(feature3);

        // --- Add components to card ---
        welcomeCard.add(Box.createVerticalStrut(50));
        welcomeCard.add(welcomeLabel);
        welcomeCard.add(Box.createVerticalStrut(20)); // Replaced old strut
        welcomeCard.add(introLabel); // Replaced old subLabel
        welcomeCard.add(Box.createVerticalStrut(25));
        welcomeCard.add(featuresPanel);
        welcomeCard.add(Box.createVerticalGlue()); // Pushes content to the top
        welcomeCard.add(Box.createVerticalStrut(50));

        homePanel.add(welcomeCard, BorderLayout.NORTH);
        contentPanel.add(homePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLecturesPanel() {
        contentPanel.removeAll();
        contentPanel.add(new LectureSchedulePanel());
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAttendancePanel() {
        contentPanel.removeAll();
        contentPanel.add(new MyAttendancePanel(student));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAssignmentsPanel() {
        contentPanel.removeAll();
        contentPanel.add(new AssignmentsPanel(student));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}