package com.wowtracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int currencyBalance;
    private List<String> unlockedAchievements = new ArrayList<>();

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

    public void addAchievement(String achievementId) {
        unlockedAchievements.add(achievementId);
    }

    public List<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }

    @Override
    public String toString() {
        return "Пользователь: " + name + "\nБаланс: " + currencyBalance + " монет";
    }
}
