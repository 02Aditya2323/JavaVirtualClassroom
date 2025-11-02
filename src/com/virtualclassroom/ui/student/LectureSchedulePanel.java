package com.virtualclassroom.ui.student;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Lecture;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LectureSchedulePanel extends JPanel {

    public LectureSchedulePanel() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Lecture Schedule");
        titleLabel.setFont(UIConstants.HEADER_FONT);

        JPanel lecturesPanel = new JPanel();
        lecturesPanel.setLayout(new BoxLayout(lecturesPanel, BoxLayout.Y_AXIS));
        lecturesPanel.setBackground(UIConstants.MAIN_BG);

        List<Lecture> lectures = DatabaseManager.getAllLectures();

        if (lectures.isEmpty()) {
            JLabel noLecturesLabel = new JLabel("No lectures scheduled yet!");
            noLecturesLabel.setFont(UIConstants.BODY_FONT);
            lecturesPanel.add(noLecturesLabel);
        } else {
            for (Lecture lecture : lectures) {
                JPanel card = createLectureCard(lecture);
                lecturesPanel.add(card);
                lecturesPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(lecturesPanel);
        scrollPane.setBorder(null);

        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.MAIN_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        contentPanel.add(scrollPane);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createLectureCard(Lecture lecture) {
        JPanel card = UIConstants.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(700, 120));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(lecture.getTitle());
        titleLabel.setFont(UIConstants.SUBHEADER_FONT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(lecture.getDescription());
        descLabel.setFont(UIConstants.BODY_FONT);
        descLabel.setForeground(UIConstants.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("📅 " + lecture.getDate() + " ⏰ " + lecture.getTime());
        dateLabel.setFont(UIConstants.BODY_FONT);
        dateLabel.setForeground(UIConstants.PRIMARY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel teacherLabel = new JLabel("By: " + lecture.getTeacherName());
        teacherLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        teacherLabel.setForeground(UIConstants.TEXT_SECONDARY);
        teacherLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(dateLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(teacherLabel);

        return card;
    }
}