package com.wowtracker.state;

import com.wowtracker.model.User;
import com.wowtracker.service.AchievementService;
import com.wowtracker.service.GoalService;
import com.wowtracker.service.TaskService;

import java.io.*;

public class AppState implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;
    private TaskService taskService;
    private AchievementService achievementService;
    private GoalService goalService;

    public AppState(User user, TaskService taskService, AchievementService achievementService, GoalService goalService) {
        this.user = user;
        this.taskService = taskService;
        this.achievementService = achievementService;
        this.goalService = goalService;
    }

    public User getUser() {
        return user;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public AchievementService getAchievementService() {
        return achievementService;
    }

    public GoalService getGoalService() {
        return goalService;
    }

    // Метод для сохранения состояния в файл
    public void saveState(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    // Статический метод для загрузки состояния из файла
    public static AppState loadState(String filename) throws IOException, ClassNotFoundException {
        File file = new File(filename);

        // Проверяем, существует ли файл и не пустой ли он
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = in.readObject();
                if (obj instanceof AppState) {
                    return (AppState) obj;
                } else {
                    throw new IOException("Файл сохранения повреждён или имеет неверный формат.");
                }
            }
        } else {
            throw new IOException("Файл сохранения отсутствует или пустой.");
        }
    }
}
