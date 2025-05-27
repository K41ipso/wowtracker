package com.wowtracker.service;

import com.wowtracker.model.Task;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Сервисы
public class TaskService implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Task> tasks = new ArrayList<>();

    public void createTask(String title, String description, LocalDate dueDate, ImageIcon icon, int reward) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, title, description, dueDate, icon, reward);
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task completeTask(String taskId) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.setCompleted(true);
                return task; // Возвращаем задачу, чтобы получить награду
            }
        }
        return null;
    }
}
