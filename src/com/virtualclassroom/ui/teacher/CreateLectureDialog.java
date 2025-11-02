package com.virtualclassroom.ui.teacher;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Lecture;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class CreateLectureDialog extends JDialog {

    private User teacher;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dateField;
    private JTextField timeField;

    public CreateLectureDialog(Frame parent, User teacher) {
        super(parent, "Create New Lecture", true);
        this.teacher = teacher;

        setSize(500, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(UIConstants.MAIN_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = UIConstants.createCard();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Header
        JLabel headerLabel = new JLabel("Create New Lecture");
        headerLabel.setFont(UIConstants.HEADER_FONT);
        headerLabel.setForeground(UIConstants.TEXT_PRIMARY);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Lecture Title:");
        titleLabel.setFont(UIConstants.BODY_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleField = new JTextField();
        UIConstants.styleTextField(titleField);
        titleField.setMaximumSize(new Dimension(450, UIConstants.FIELD_HEIGHT));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(UIConstants.BODY_FONT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(UIConstants.BODY_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setMaximumSize(new Dimension(450, 100));
        descScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Date
        JLabel dateLabel = new JLabel("Date (DD/MM/YYYY):");
        dateLabel.setFont(UIConstants.BODY_FONT);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dateField = new JTextField();
        UIConstants.styleTextField(dateField);
        dateField.setMaximumSize(new Dimension(450, UIConstants.FIELD_HEIGHT));
        dateField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Time
        JLabel timeLabel = new JLabel("Time (HH:MM):");
        timeLabel.setFont(UIConstants.BODY_FONT);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        timeField = new JTextField();
        UIConstants.styleTextField(timeField);
        timeField.setMaximumSize(new Dimension(450, UIConstants.FIELD_HEIGHT));
        timeField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(450, 50));

        JButton createBtn = new JButton("Create");
        UIConstants.styleButton(createBtn, UIConstants.SUCCESS);
        createBtn.addActionListener(e -> handleCreate());

        JButton cancelBtn = new JButton("Cancel");
        UIConstants.styleButton(cancelBtn, UIConstants.TEXT_SECONDARY);
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(createBtn);

        // Add all components
        formPanel.add(headerLabel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(descLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(descScrollPane);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(dateLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(dateField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(timeLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(timeField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel);
        add(mainPanel);
    }

    private void handleCreate() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Basic date validation (DD/MM/YYYY)
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format! Use DD/MM/YYYY",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Basic time validation (HH:MM)
        if (!time.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid time format! Use HH:MM",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Lecture lecture = new Lecture(title, description, date, time, teacher.getId());
        boolean success = DatabaseManager.createLecture(lecture);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Lecture created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create lecture!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}