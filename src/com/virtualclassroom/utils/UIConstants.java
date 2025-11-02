package com.virtualclassroom.utils;

import java.awt.*;

public class UIConstants {

    // COLORS - Professional + Modern Mix
    public static final Color SIDEBAR_BG = new Color(44, 62, 80);        // Dark blue-gray
    public static final Color SIDEBAR_HOVER = new Color(52, 73, 94);     // Lighter on hover
    public static final Color SIDEBAR_SELECTED = new Color(41, 128, 185); // Blue when selected

    public static final Color MAIN_BG = new Color(236, 240, 241);         // Light gray background
    public static final Color CARD_BG = Color.WHITE;                      // White cards

    public static final Color PRIMARY = new Color(52, 152, 219);          // Modern blue
    public static final Color SUCCESS = new Color(39, 174, 96);           // Green
    public static final Color DANGER = new Color(231, 76, 60);            // Red
    public static final Color WARNING = new Color(241, 196, 15);          // Yellow

    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);       // Dark text
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);  // Gray text
    public static final Color TEXT_WHITE = Color.WHITE;

    // FONTS
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font SIDEBAR_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    // DIMENSIONS
    public static final int SIDEBAR_WIDTH = 200;
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 650;

    public static final int BUTTON_HEIGHT = 40;
    public static final int FIELD_HEIGHT = 35;

    // SPACING
    public static final int PADDING = 20;
    public static final int MARGIN = 15;

    // BORDER RADIUS
    public static final int BORDER_RADIUS = 8;

    // Helper method to create styled button
    public static void styleButton(javax.swing.JButton button, Color bgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setForeground(TEXT_WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, BUTTON_HEIGHT));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    // Helper method to create styled text field
    public static void styleTextField(javax.swing.JTextField field) {
        field.setFont(BODY_FONT);
        field.setPreferredSize(new Dimension(350, FIELD_HEIGHT));
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    // Helper method to create card panel with shadow effect
    public static javax.swing.JPanel createCard() {
        javax.swing.JPanel card = new javax.swing.JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                javax.swing.BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
        ));
        return card;
    }
}