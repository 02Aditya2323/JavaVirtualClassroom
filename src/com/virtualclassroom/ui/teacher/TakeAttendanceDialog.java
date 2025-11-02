package com.virtualclassroom.ui.teacher;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Lecture;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TakeAttendanceDialog extends JDialog {

    private Lecture lecture;
    private List<JCheckBox> studentCheckboxes;
    private List<User> students;

    public TakeAttendanceDialog(Frame parent, Lecture lecture) {
        super(parent, "Take Attendance - " + lecture.getTitle(), true);
        this.lecture = lecture;

        setSize(500, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(UIConstants.MAIN_BG);

        // Header
        JLabel headerLabel = new JLabel("Mark Students Present");
        headerLabel.setFont(UIConstants.HEADER_FONT);

        // Student list panel
        JPanel studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.setBackground(Color.WHITE);

        students = DatabaseManager.getAllStudents();
        studentCheckboxes = new ArrayList<>();

        if (students.isEmpty()) {
            JLabel noStudentsLabel = new JLabel("No students registered yet!");
            noStudentsLabel.setFont(UIConstants.BODY_FONT);
            studentsPanel.add(noStudentsLabel);
        } else {
            for (User student : students) {
                JCheckBox checkbox = new JCheckBox(student.getName());
                checkbox.setFont(UIConstants.BODY_FONT);
                checkbox.setBackground(Color.WHITE);
                studentCheckboxes.add(checkbox);
                studentsPanel.add(checkbox);
                studentsPanel.add(Box.createVerticalStrut(5));
            }
        }

        JScrollPane scrollPane = new JScrollPane(studentsPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(UIConstants.MAIN_BG);

        JButton saveBtn = new JButton("Save Attendance");
        UIConstants.styleButton(saveBtn, UIConstants.SUCCESS);
        saveBtn.addActionListener(e -> saveAttendance());

        JButton cancelBtn = new JButton("Cancel");
        UIConstants.styleButton(cancelBtn, UIConstants.TEXT_SECONDARY);
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void saveAttendance() {
        List<Integer> presentStudentIds = new ArrayList<>();

        for (int i = 0; i < studentCheckboxes.size(); i++) {
            if (studentCheckboxes.get(i).isSelected()) {
                presentStudentIds.add(students.get(i).getId());
            }
        }

        boolean success = DatabaseManager.markAttendance(lecture.getId(), presentStudentIds);

        if (success) {
            JOptionPane.showMessageDialog(this, "Attendance saved successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save attendance!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}