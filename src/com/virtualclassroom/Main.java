package com.virtualclassroom;

import com.virtualclassroom.database.DatabaseManager;
import com.virtualclassroom.ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize database first
        DatabaseManager.initialize();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}