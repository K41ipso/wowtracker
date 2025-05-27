package com.wowtracker.dialogs;

import javax.swing.*;
import java.awt.*;

// Диалог для создания цели
public class CreateGoalDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JSpinner costSpinner;
    private boolean confirmed = false;

    public CreateGoalDialog(JFrame parent) {
        super(parent, "Создать новую цель", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        formPanel.add(new JLabel("Название цели:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Описание:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        formPanel.add(new JLabel("Стоимость (монеты):"));
        costSpinner = new JSpinner(new SpinnerNumberModel(100, 10, 1000, 10));
        formPanel.add(costSpinner);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Создать");
        okButton.setBackground(new Color(76, 175, 80));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getTitle() {
        return titleField.getText();
    }

    public String getDescription() {
        return descriptionArea.getText();
    }

    public int getCost() {
        return (int) costSpinner.getValue();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
