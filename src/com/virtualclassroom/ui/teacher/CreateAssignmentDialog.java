package com.virtualclassroom.ui.teacher;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Assignment;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class CreateAssignmentDialog extends JDialog {

    private User teacher;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;

    public CreateAssignmentDialog(Frame parent, User teacher) {
        super(parent, "Create Assignment", true);
        this.teacher = teacher;

        setSize(500, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(UIConstants.MAIN_BG);

        JPanel formPanel = UIConstants.createCard();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Create New Assignment");
        headerLabel.setFont(UIConstants.HEADER_FONT);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleField = new JTextField();
        UIConstants.styleTextField(titleField);
        titleField.setMaximumSize(new Dimension(450, UIConstants.FIELD_HEIGHT));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(UIConstants.BODY_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setMaximumSize(new Dimension(450, 100));
        descScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        dueDateField = new JTextField();
        UIConstants.styleTextField(dueDateField);
        dueDateField.setMaximumSize(new Dimension(450, UIConstants.FIELD_HEIGHT));
        dueDateField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(450, 50));

        JButton createBtn = new JButton("Create");
        UIConstants.styleButton(createBtn, UIConstants.SUCCESS);
        createBtn.addActionListener(e -> handleCreate());

        JButton cancelBtn = new JButton("Cancel");
        UIConstants.styleButton(cancelBtn, UIConstants.TEXT_SECONDARY);
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(createBtn);

        formPanel.add(headerLabel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(new JLabel("Title:"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Description:"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(descScrollPane);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Due Date (DD/MM/YYYY):"));
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(dueDateField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel);
        add(mainPanel);
    }

    private void handleCreate() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String dueDate = dueDateField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Assignment assignment = new Assignment(title, description, dueDate, teacher.getId());
        boolean success = DatabaseManager.createAssignment(assignment);

        if (success) {
            JOptionPane.showMessageDialog(this, "Assignment created successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create assignment!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}