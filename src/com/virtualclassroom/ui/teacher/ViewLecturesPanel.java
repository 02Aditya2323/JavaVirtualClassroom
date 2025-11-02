package com.virtualclassroom.ui.teacher;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.models.Lecture;
import com.virtualclassroom.models.User;
import com.virtualclassroom.utils.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewLecturesPanel extends JPanel {

    private User teacher;
    private JTable lecturesTable;
    private DefaultTableModel tableModel;

    public ViewLecturesPanel(User teacher) {
        this.teacher = teacher;

        setLayout(new BorderLayout());
        setBackground(UIConstants.MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.MAIN_BG);

        JLabel titleLabel = new JLabel("My Lectures");
        titleLabel.setFont(UIConstants.HEADER_FONT);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);

        JButton createBtn = new JButton("+ Create Lecture");
        UIConstants.styleButton(createBtn, UIConstants.SUCCESS);
        createBtn.addActionListener(e -> openCreateLectureDialog());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(createBtn, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Title", "Description", "Date", "Time", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Actions column editable
            }
        };

        lecturesTable = new JTable(tableModel);
        lecturesTable.setFont(UIConstants.BODY_FONT);
        lecturesTable.setRowHeight(40);
        lecturesTable.getTableHeader().setFont(UIConstants.SUBHEADER_FONT);
        lecturesTable.getTableHeader().setBackground(UIConstants.PRIMARY);
        lecturesTable.getTableHeader().setForeground(Color.WHITE);

        // Hide ID column
        lecturesTable.getColumnModel().getColumn(0).setMinWidth(0);
        lecturesTable.getColumnModel().getColumn(0).setMaxWidth(0);
        lecturesTable.getColumnModel().getColumn(0).setWidth(0);

        // Add delete button to Actions column
        lecturesTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        lecturesTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(lecturesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        add(headerPanel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(20), BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIConstants.MAIN_BG);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        tablePanel.add(scrollPane);

        add(tablePanel, BorderLayout.CENTER);

        loadLectures();
    }

    private void loadLectures() {
        tableModel.setRowCount(0);
        List<Lecture> lectures = DatabaseManager.getLecturesByTeacher(teacher.getId());

        if (lectures.isEmpty()) {
            // Show empty state
            Object[] row = {0, "No lectures yet", "Create your first lecture!", "", "", ""};
            tableModel.addRow(row);
        } else {
            for (Lecture lecture : lectures) {
                Object[] row = {
                        lecture.getId(),
                        lecture.getTitle(),
                        lecture.getDescription(),
                        lecture.getDate(),
                        lecture.getTime(),
                        "Delete"
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openCreateLectureDialog() {
        CreateLectureDialog dialog = new CreateLectureDialog((Frame) SwingUtilities.getWindowAncestor(this), teacher);
        dialog.setVisible(true);
        loadLectures(); // Refresh table
    }

    // Button Renderer for table
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            UIConstants.styleButton(this, UIConstants.DANGER);
            return this;
        }
    }

    // Button Editor for table
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            UIConstants.styleButton(button, UIConstants.DANGER);

            button.addActionListener(e -> {
                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                int lectureId = (int) tableModel.getValueAt(row, 0);

                if (lectureId > 0) {
                    int confirm = JOptionPane.showConfirmDialog(button,
                            "Are you sure you want to delete this lecture?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = DatabaseManager.deleteLecture(lectureId);
                        if (success) {
                            JOptionPane.showMessageDialog(button, "Lecture deleted successfully!");
                            loadLectures();
                        } else {
                            JOptionPane.showMessageDialog(button, "Failed to delete lecture!");
                        }
                    }
                }
            }
            clicked = false;
            return label;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}