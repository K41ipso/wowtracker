package com.wowtracker;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class GoalService implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<PersonalGoal> goals = new ArrayList<>();

    public void addGoal(PersonalGoal goal) {
        goals.add(goal);
    }

    public void completeGoal(User user, String goalId) {
        PersonalGoal goal = goals.stream()
                .filter(g -> g.getId().equals(goalId))
                .findFirst()
                .orElse(null);

        if (goal != null && !goal.isCompleted() && user.getCurrencyBalance() >= goal.getCost()) {
            user.setCurrencyBalance(user.getCurrencyBalance() - goal.getCost());
            goal.setCompleted(true);
            JOptionPane.showMessageDialog(null, "Цель достигнута: " + goal.getTitle());
        } else if (goal != null && goal.isCompleted()) {
            JOptionPane.showMessageDialog(null, "Цель уже достигнута!");
        } else {
            JOptionPane.showMessageDialog(null, "Недостаточно монет или цель не найдена.");
        }
    }

    public List<PersonalGoal> getGoals() {
        return goals;
    }
}
