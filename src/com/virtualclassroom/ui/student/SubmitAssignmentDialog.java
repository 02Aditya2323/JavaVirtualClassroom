package com.virtualclassroom.ui.student;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Assignment;
import com.virtualclassroom.models.Submission;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubmitAssignmentDialog extends JDialog {

    private Assignment assignment;
    private User student;
    private JTextArea contentArea;

    public SubmitAssignmentDialog(Frame parent, Assignment assignment, User student) {
        super(parent, "Submit Assignment", true);
        this.assignment = assignment;
        this.student = student;

        setSize(550, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(UIConstants.MAIN_BG);

        JPanel formPanel = UIConstants.createCard();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Submit: " + assignment.getTitle());
        headerLabel.setFont(UIConstants.HEADER_FONT);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Enter your submission below:");
        instructionLabel.setFont(UIConstants.BODY_FONT);
        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentArea = new JTextArea(10, 40);
        contentArea.setFont(UIConstants.BODY_FONT);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(500, 50));

        JButton submitBtn = new JButton("Submit");
        UIConstants.styleButton(submitBtn, UIConstants.SUCCESS);
        submitBtn.addActionListener(e -> handleSubmit());

        JButton cancelBtn = new JButton("Cancel");
        UIConstants.styleButton(cancelBtn, UIConstants.TEXT_SECONDARY);
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(submitBtn);

        formPanel.add(headerLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(instructionLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(scrollPane);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel);
        add(mainPanel);
    }

    private void handleSubmit() {
        String content = contentArea.getText().trim();

        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your submission!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Submission submission = new Submission(assignment.getId(), student.getId(), content, timestamp);

        boolean success = DatabaseManager.submitAssignment(submission);

        if (success) {
            JOptionPane.showMessageDialog(this, "Assignment submitted successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit assignment!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}