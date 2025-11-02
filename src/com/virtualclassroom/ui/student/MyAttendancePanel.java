package com.virtualclassroom.ui.student;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Attendance;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyAttendancePanel extends JPanel {

    private User student;

    public MyAttendancePanel(User student) {
        this.student = student;

        setLayout(new BorderLayout());
        setBackground(UIConstants.MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with percentage
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.MAIN_BG);

        JLabel titleLabel = new JLabel("My Attendance");
        titleLabel.setFont(UIConstants.HEADER_FONT);

        double percentage = DatabaseManager.getAttendancePercentage(student.getId());
        JLabel percentageLabel = new JLabel(String.format("Overall: %.1f%%", percentage));
        percentageLabel.setFont(UIConstants.SUBHEADER_FONT);
        percentageLabel.setForeground(percentage >= 75 ? UIConstants.SUCCESS : UIConstants.DANGER);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(percentageLabel, BorderLayout.EAST);

        // Attendance list
        JPanel attendancePanel = new JPanel();
        attendancePanel.setLayout(new BoxLayout(attendancePanel, BoxLayout.Y_AXIS));
        attendancePanel.setBackground(UIConstants.MAIN_BG);

        List<Attendance> attendanceList = DatabaseManager.getAttendanceByStudent(student.getId());

        if (attendanceList.isEmpty()) {
            JLabel noDataLabel = new JLabel("No attendance records yet!");
            noDataLabel.setFont(UIConstants.BODY_FONT);
            attendancePanel.add(noDataLabel);
        } else {
            for (Attendance attendance : attendanceList) {
                JPanel card = createAttendanceCard(attendance);
                attendancePanel.add(card);
                attendancePanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(attendancePanel);
        scrollPane.setBorder(null);

        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.MAIN_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createAttendanceCard(Attendance attendance) {
        JPanel card = UIConstants.createCard();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(700, 60));

        JLabel lectureLabel = new JLabel(attendance.getLectureTitle());
        lectureLabel.setFont(UIConstants.BODY_FONT);

        JLabel statusLabel = new JLabel(attendance.getStatus());
        statusLabel.setFont(UIConstants.SUBHEADER_FONT);
        statusLabel.setForeground(attendance.getStatus().equals("Present") ? UIConstants.SUCCESS : UIConstants.DANGER);

        card.add(lectureLabel, BorderLayout.WEST);
        card.add(statusLabel, BorderLayout.EAST);

        return card;
    }
}