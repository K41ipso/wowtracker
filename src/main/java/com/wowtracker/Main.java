package com.wowtracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Task {
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean isCompleted;

    public Task(String id, String title, String description, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Задача: " + title + "\nОписание: " + description + "\nСрок: " + dueDate + "\nСтатус: " + (isCompleted ? "Выполнено" : "В процессе");
    }
}

class Achievement {
    private String id;
    private String title;
    private String description;
    private int cost;

    public Achievement(String id, String title, String description, int cost) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Ачивка: " + title + "\nОписание: " + description + "\nСтоимость: " + cost + " монет";
    }
}

class User {
    private String id;
    private String name;
    private int currencyBalance;

    public User(String id, String name, int currencyBalance) {
        this.id = id;
        this.name = name;
        this.currencyBalance = currencyBalance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrencyBalance() {
        return currencyBalance;
    }

    public void setCurrencyBalance(int currencyBalance) {
        this.currencyBalance = currencyBalance;
    }

    @Override
    public String toString() {
        return "Пользователь: " + name + "\nБаланс: " + currencyBalance + " монет";
    }
}

class TaskService {
    private List<Task> tasks = new ArrayList<>();

    public void createTask(String title, String description, LocalDate dueDate) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, title, description, dueDate);
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void completeTask(String taskId) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.setCompleted(true);
                break;
            }
        }
    }
}

class AchievementService {
    private List<Achievement> achievements = new ArrayList<>();

    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    public void unlockAchievement(User user, String achievementId) {
        Achievement achievement = achievements.stream()
                .filter(a -> a.getId().equals(achievementId))
                .findFirst()
                .orElse(null);

        if (achievement != null && user.getCurrencyBalance() >= achievement.getCost()) {
            user.setCurrencyBalance(user.getCurrencyBalance() - achievement.getCost());
            JOptionPane.showMessageDialog(null, "Ачивка разблокирована: " + achievement.getTitle());
        } else {
            JOptionPane.showMessageDialog(null, "Недостаточно монет или ачивка не найдена.");
        }
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }
}

class QuestGenerator {
    public Task generateWoWStyleTask() {
        String id = UUID.randomUUID().toString();
        return new Task(id, "Задание: Поиск утраченного артефакта", "Найдите древний артефакт в глубинах Темного леса.", LocalDate.now().plusDays(7));
    }
}

public class Main extends JFrame {
    private TaskService taskService = new TaskService();
    private AchievementService achievementService = new AchievementService();
    private QuestGenerator questGenerator = new QuestGenerator();
    private User user = new User(UUID.randomUUID().toString(), "Игрок", 100);

    private JLabel balanceLabel;
    private JTextArea taskArea;
    private JTextArea achievementArea;

    public Main() {
        setTitle("WoW Task Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        achievementService.addAchievement(new Achievement(UUID.randomUUID().toString(), "Первый шаг", "Выполните первое задание.", 50));
        achievementService.addAchievement(new Achievement(UUID.randomUUID().toString(), "Мастер заданий", "Выполните 5 заданий.", 100));

        balanceLabel = new JLabel("Баланс: " + user.getCurrencyBalance() + " монет");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(balanceLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton createTaskButton = new JButton("Создать задачу");
        JButton completeTaskButton = new JButton("Выполнить задачу");
        JButton showTasksButton = new JButton("Показать задачи");
        JButton unlockAchievementButton = new JButton("Разблокировать ачивку");
        buttonPanel.add(createTaskButton);
        buttonPanel.add(completeTaskButton);
        buttonPanel.add(showTasksButton);
        buttonPanel.add(unlockAchievementButton);

        taskArea = new JTextArea();
        taskArea.setEditable(false);
        JScrollPane taskScrollPane = new JScrollPane(taskArea);

        achievementArea = new JTextArea();
        achievementArea.setEditable(false);
        JScrollPane achievementScrollPane = new JScrollPane(achievementArea);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Задачи", taskScrollPane);
        tabbedPane.addTab("Ачивки", achievementScrollPane);

        createTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Task task = questGenerator.generateWoWStyleTask();
                taskService.createTask(task.getTitle(), task.getDescription(), task.getDueDate());
                updateTaskArea();
                JOptionPane.showMessageDialog(null, "Задача создана: " + task.getTitle());
            }
        });

        completeTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskId = JOptionPane.showInputDialog("Введите ID задачи для выполнения:");
                if (taskId != null && !taskId.isEmpty()) {
                    taskService.completeTask(taskId);
                    user.setCurrencyBalance(user.getCurrencyBalance() + 10);
                    balanceLabel.setText("Баланс: " + user.getCurrencyBalance() + " монет");
                    updateTaskArea();
                    JOptionPane.showMessageDialog(null, "Задача выполнена! Начислено 10 монет.");
                }
            }
        });

        showTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTaskArea();
            }
        });

        unlockAchievementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String achievementId = JOptionPane.showInputDialog("Введите ID ачивки для разблокировки:");
                if (achievementId != null && !achievementId.isEmpty()) {
                    achievementService.unlockAchievement(user, achievementId);
                    balanceLabel.setText("Баланс: " + user.getCurrencyBalance() + " монет");
                    updateAchievementArea();
                }
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void updateTaskArea() {
        StringBuilder tasksList = new StringBuilder("Список задач:\n");
        for (Task task : taskService.getTasks()) {
            tasksList.append(task).append("\n------\n");
        }
        taskArea.setText(tasksList.toString());
    }

    private void updateAchievementArea() {
        StringBuilder achievementsList = new StringBuilder("Список ачивок:\n");
        for (Achievement achievement : achievementService.getAchievements()) {
            achievementsList.append(achievement).append("\n------\n");
        }
        achievementArea.setText(achievementsList.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}