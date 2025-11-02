package com.virtualclassroom.ui.teacher;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Assignment;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageAssignmentsPanel extends JPanel {

    private User teacher;
    private JTable assignmentsTable;
    private DefaultTableModel tableModel;

    public ManageAssignmentsPanel(User teacher) {
        this.teacher = teacher;

        setLayout(new BorderLayout());
        setBackground(UIConstants.MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.MAIN_BG);

        JLabel titleLabel = new JLabel("My Assignments");
        titleLabel.setFont(UIConstants.HEADER_FONT);

        JButton createBtn = new JButton("+ Create Assignment");
        UIConstants.styleButton(createBtn, UIConstants.SUCCESS);
        createBtn.addActionListener(e -> openCreateDialog());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(createBtn, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Title", "Description", "Due Date", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        assignmentsTable = new JTable(tableModel);
        assignmentsTable.setFont(UIConstants.BODY_FONT);
        assignmentsTable.setRowHeight(40);
        assignmentsTable.getTableHeader().setFont(UIConstants.SUBHEADER_FONT);
        assignmentsTable.getTableHeader().setBackground(UIConstants.PRIMARY);
        assignmentsTable.getTableHeader().setForeground(Color.WHITE);

        assignmentsTable.getColumnModel().getColumn(0).setMinWidth(0);
        assignmentsTable.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(assignmentsTable);

        add(headerPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIConstants.MAIN_BG);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        tablePanel.add(scrollPane);

        add(tablePanel, BorderLayout.CENTER);

        loadAssignments();
    }

    private void loadAssignments() {
        tableModel.setRowCount(0);
        List<Assignment> assignments = DatabaseManager.getAssignmentsByTeacher(teacher.getId());

        for (Assignment assignment : assignments) {
            Object[] row = {
                    assignment.getId(),
                    assignment.getTitle(),
                    assignment.getDescription(),
                    assignment.getDueDate(),
                    "Delete"
            };
            tableModel.addRow(row);
        }
    }

    private void openCreateDialog() {
        CreateAssignmentDialog dialog = new CreateAssignmentDialog(
                (Frame) SwingUtilities.getWindowAncestor(this), teacher);
        dialog.setVisible(true);
        loadAssignments();
    }
}