package com.wowtracker;

import javax.swing.*;
import java.io.Serializable;

class Achievement implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String description;
    private int cost;
    private ImageIcon icon;
    private boolean isUnlocked;

    public Achievement(String id, String title, String description, int cost, ImageIcon icon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.icon = icon;
        this.isUnlocked = false;
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

    public ImageIcon getIcon() {
        return icon;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    @Override
    public String toString() {
        return "Ачивка: " + title +
                "\nОписание: " + description +
                "\nСтоимость: " + cost + " монет" +
                "\nСтатус: " + (isUnlocked ? "Разблокировано" : "Заблокировано");
    }
}
