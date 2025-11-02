package com.virtualclassroom.ui.teacher;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Lecture;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AttendanceReportPanel extends JPanel {

    private User teacher;

    public AttendanceReportPanel(User teacher) {
        this.teacher = teacher;

        setLayout(new BorderLayout());
        setBackground(UIConstants.MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(UIConstants.HEADER_FONT);

        // Lectures list
        JPanel lecturesPanel = new JPanel();
        lecturesPanel.setLayout(new BoxLayout(lecturesPanel, BoxLayout.Y_AXIS));
        lecturesPanel.setBackground(UIConstants.MAIN_BG);

        List<Lecture> lectures = DatabaseManager.getLecturesByTeacher(teacher.getId());

        if (lectures.isEmpty()) {
            JLabel noLecturesLabel = new JLabel("No lectures found. Create a lecture first!");
            noLecturesLabel.setFont(UIConstants.BODY_FONT);
            lecturesPanel.add(noLecturesLabel);
        } else {
            for (Lecture lecture : lectures) {
                JPanel lectureCard = createLectureCard(lecture);
                lecturesPanel.add(lectureCard);
                lecturesPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(lecturesPanel);
        scrollPane.setBorder(null);

        add(titleLabel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(20));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.MAIN_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createLectureCard(Lecture lecture) {
        JPanel card = UIConstants.createCard();
        card.setLayout(new BorderLayout(10, 10));
        card.setMaximumSize(new Dimension(700, 100));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(lecture.getTitle());
        titleLabel.setFont(UIConstants.SUBHEADER_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel(lecture.getDate() + " at " + lecture.getTime());
        dateLabel.setFont(UIConstants.BODY_FONT);
        dateLabel.setForeground(UIConstants.TEXT_SECONDARY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(dateLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton takeAttendanceBtn = new JButton("Take Attendance");
        UIConstants.styleButton(takeAttendanceBtn, UIConstants.PRIMARY);
        takeAttendanceBtn.setPreferredSize(new Dimension(160, 35));
        takeAttendanceBtn.addActionListener(e -> {
            TakeAttendanceDialog dialog = new TakeAttendanceDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this), lecture);
            dialog.setVisible(true);
        });

        buttonPanel.add(takeAttendanceBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }
}