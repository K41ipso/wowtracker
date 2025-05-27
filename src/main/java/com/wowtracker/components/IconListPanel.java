package com.wowtracker.components;

import com.wowtracker.model.Achievement;
import com.wowtracker.model.PersonalGoal;
import com.wowtracker.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

// Панель для отображения задач/ачивок/целей с иконками
public class IconListPanel extends JPanel {
    public IconListPanel(List<?> items) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 240, 240));

        for (Object item : items) {
            JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.setBackground(new Color(250, 250, 250));
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            if (item instanceof Task) {
                Task task = (Task) item;
                JLabel iconLabel = new JLabel(task.getIcon());
                JTextArea textArea = new JTextArea(task.toString());
                textArea.setEditable(false);
                textArea.setBackground(new Color(250, 250, 250));
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                itemPanel.add(iconLabel, BorderLayout.WEST);
                itemPanel.add(textArea, BorderLayout.CENTER);

                if (task.isCompleted()) {
                    itemPanel.setBackground(new Color(220, 255, 220));
                }
            } else if (item instanceof Achievement) {
                Achievement achievement = (Achievement) item;
                JLabel iconLabel = new JLabel(achievement.getIcon());
                JTextArea textArea = new JTextArea(achievement.toString());
                textArea.setEditable(false);
                textArea.setBackground(new Color(250, 250, 250));
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                itemPanel.add(iconLabel, BorderLayout.WEST);
                itemPanel.add(textArea, BorderLayout.CENTER);

                if (achievement.isUnlocked()) {
                    itemPanel.setBackground(new Color(220, 220, 255));
                }
            } else if (item instanceof PersonalGoal) {
                PersonalGoal goal = (PersonalGoal) item;
                JLabel iconLabel = new JLabel(new ImageIcon(
                        new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB)));
                JTextArea textArea = new JTextArea(goal.toString());
                textArea.setEditable(false);
                textArea.setBackground(new Color(250, 250, 250));
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                itemPanel.add(iconLabel, BorderLayout.WEST);
                itemPanel.add(textArea, BorderLayout.CENTER);

                if (goal.isCompleted()) {
                    itemPanel.setBackground(new Color(255, 255, 220));
                }
            }

            add(itemPanel);
            add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
}
