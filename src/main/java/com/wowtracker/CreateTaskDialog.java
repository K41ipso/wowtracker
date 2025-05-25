package com.wowtracker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

// Диалог для создания задачи
class CreateTaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JSpinner dateSpinner;
    private JSpinner rewardSpinner;
    private boolean confirmed = false;

    public CreateTaskDialog(JFrame parent) {
        super(parent, "Создать новую задачу", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Название задачи:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Описание:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        formPanel.add(new JLabel("Срок выполнения:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd.MM.yyyy"));
        formPanel.add(dateSpinner);

        formPanel.add(new JLabel("Награда (монеты):"));
        rewardSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 1000, 10));
        formPanel.add(rewardSpinner);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = createStyledButton("Создать", new Color(76, 175, 80));
        okButton.setBackground(new Color(76, 175, 80));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        JButton cancelButton = createStyledButton("Отмена", new Color(244, 67, 54));
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Обновленный вариант с анимацией
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(color.darker().darker());
                } else if (getModel().isRollover()) {
                    g.setColor(color.darker());
                } else {
                    g.setColor(color);
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

            @Override
            public void updateUI() {
                // Переопределяем, чтобы кнопка не теряла стиль при обновлении UI
                super.updateUI();
                setContentAreaFilled(false);
                setOpaque(true);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.darker(), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        };

        // Изменено с Color.WHITE на Color.BLACK
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        return button;
    }

    public String getTitle() {
        return titleField.getText();
    }

    public String getDescription() {
        return descriptionArea.getText();
    }

    public LocalDate getDueDate() {
        return ((java.util.Date) dateSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    public int getReward() {
        return (int) rewardSpinner.getValue();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
