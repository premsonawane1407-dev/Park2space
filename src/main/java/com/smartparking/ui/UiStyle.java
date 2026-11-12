package com.smartparking.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

final class UiStyle {
    static final Color NAVY=new Color(25,39,63), BLUE=new Color(45,111,205), BG=new Color(245,247,250), GREEN=new Color(224,244,233), GREEN_TEXT=new Color(28,120,70), RED=new Color(250,228,229), RED_TEXT=new Color(173,48,55), TEXT=new Color(37,49,66), MUTED=new Color(104,117,135), WHITE=Color.WHITE;
    static final Font REGULAR=new Font("Segoe UI",Font.PLAIN,14), BOLD=new Font("Segoe UI",Font.BOLD,14), TITLE=new Font("Segoe UI",Font.BOLD,25);
    static JButton primaryButton(String text){
        JButton button=new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setBackground(BLUE);
        button.setForeground(WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createEmptyBorder(10,16,10,16));
        button.setFont(BOLD);
        button.setFocusPainted(false);
        return button;
    }
    static JButton secondaryButton(String text){
        JButton button=new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setBackground(WHITE);
        button.setForeground(TEXT);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,217,226)),
                BorderFactory.createEmptyBorder(9,15,9,15)));
        button.setFont(REGULAR);
        button.setFocusPainted(false);
        return button;
    }
    private UiStyle() { }
}
