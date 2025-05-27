package com.wowtracker.model;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDate;

// Модели данных
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean isCompleted;
    private ImageIcon icon;
    private int reward;

    public Task(String id, String title, String description, LocalDate dueDate, ImageIcon icon, int reward) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
        this.icon = icon;
        this.reward = reward;
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

    public ImageIcon getIcon() {
        return icon;
    }

    public int getReward() {
        return reward;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Задача: " + title +
                "\nОписание: " + description +
                "\nСрок: " + dueDate +
                "\nНаграда: " + reward + " монет" +
                "\nСтатус: " + (isCompleted ? "Выполнено" : "В процессе");
    }
}
