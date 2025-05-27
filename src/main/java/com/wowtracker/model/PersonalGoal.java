package com.wowtracker.model;

import java.io.Serializable;

public class PersonalGoal implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String description;
    private int cost;
    private boolean isCompleted;

    public PersonalGoal(String id, String title, String description, int cost) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
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

    public int getCost() {
        return cost;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Цель: " + title +
                "\nОписание: " + description +
                "\nСтоимость: " + cost + " монет" +
                "\nСтатус: " + (isCompleted ? "Достигнуто" : "В процессе");
    }
}
