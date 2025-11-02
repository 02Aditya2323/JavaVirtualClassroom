package com.virtualclassroom.ui.student;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Assignment;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AssignmentsPanel extends JPanel {

    private User student;

    public AssignmentsPanel(User student) {
        this.student = student;

        setLayout(new BorderLayout());
        setBackground(UIConstants.MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Assignments");
        titleLabel.setFont(UIConstants.HEADER_FONT);

        JPanel assignmentsPanel = new JPanel();
        assignmentsPanel.setLayout(new BoxLayout(assignmentsPanel, BoxLayout.Y_AXIS));
        assignmentsPanel.setBackground(UIConstants.MAIN_BG);

        List<Assignment> assignments = DatabaseManager.getAllAssignments();

        if (assignments.isEmpty()) {
            JLabel noAssignmentsLabel = new JLabel("No assignments available!");
            noAssignmentsLabel.setFont(UIConstants.BODY_FONT);
            assignmentsPanel.add(noAssignmentsLabel);
        } else {
            for (Assignment assignment : assignments) {
                JPanel card = createAssignmentCard(assignment);
                assignmentsPanel.add(card);
                assignmentsPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(assignmentsPanel);
        scrollPane.setBorder(null);

        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.MAIN_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createAssignmentCard(Assignment assignment) {
        JPanel card = UIConstants.createCard();
        card.setLayout(new BorderLayout(10, 10));
        card.setMaximumSize(new Dimension(700, 100));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(assignment.getTitle());
        titleLabel.setFont(UIConstants.SUBHEADER_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(assignment.getDescription());
        descLabel.setFont(UIConstants.BODY_FONT);
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dueDateLabel = new JLabel("Due: " + assignment.getDueDate());
        dueDateLabel.setFont(UIConstants.BODY_FONT);
        dueDateLabel.setForeground(UIConstants.DANGER);
        dueDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dueDateLabel);

        JButton submitBtn = new JButton(
                DatabaseManager.hasSubmitted(assignment.getId(), student.getId()) ? "Resubmit" : "Submit"
        );
        UIConstants.styleButton(submitBtn, UIConstants.PRIMARY);
        submitBtn.setPreferredSize(new Dimension(120, 35));
        submitBtn.addActionListener(e -> {
            SubmitAssignmentDialog dialog = new SubmitAssignmentDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this), assignment, student);
            dialog.setVisible(true);
        });

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(submitBtn, BorderLayout.EAST);

        return card;
    }
}