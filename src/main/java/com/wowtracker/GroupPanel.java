package com.wowtracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class GroupPanel extends JPanel {
    private JLabel titleLabel;
    private JPanel buttonPanel;

    public GroupPanel(String title, JButton... buttons) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Создаем заголовок
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Курсор при наведении
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Действие при клике на заголовок
                System.out.println("Клик по заголовку: " + title);
            }
        });

        // Создаем панель для кнопок
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        // Добавляем заголовок и кнопки
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
}