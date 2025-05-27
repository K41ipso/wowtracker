package com.wowtracker.quest;

import com.wowtracker.model.Achievement;
import com.wowtracker.model.PersonalGoal;
import com.wowtracker.model.Task;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDate;
import java.util.UUID;

// Генератор заданий
public class QuestGenerator {
    private ImageIcon[] taskIcons = {
            createResizedIcon("⚔️", 50, 50),
            createResizedIcon("📜", 50, 50),
            createResizedIcon("🔮", 50, 50),
            createResizedIcon("🗡️", 50, 50),
            createResizedIcon("🏹", 50, 50)
    };

    private ImageIcon[] achievementIcons = loadAchievementIcons();

    private ImageIcon[] loadAchievementIcons() {
        ImageIcon[] icons = new ImageIcon[23];
        for (int i = 0; i < 23; i++) {
            String path = "icons/icon_" + (i + 1) + ".png";
            icons[i] = resizeIcon(path, 50, 50);
        }
        return icons;
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            // Загружаем изображение как URL
            URL url = getClass().getClassLoader().getResource(path);
            if (url == null) {
                System.err.println("Файл не найден: " + path);
                return null;
            }

            BufferedImage originalImage = ImageIO.read(url);
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = resizedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(originalImage, 0, 0, width, height, null);
            g2.dispose();

            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon createResizedIcon(String emoji, int width, int height) {
        JLabel label = new JLabel(emoji);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        label.setSize(width, height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        label.paint(g2);
        g2.dispose();
        return new ImageIcon(image);
    }

    public Task generateWoWStyleTask() {
        String[] titles = {
                "Поиск утраченного артефакта",
                "Охота на дракона",
                "Сбор редких трав",
                "Зачистка подземелья",
                "Спасение деревни"
        };
        String[] descriptions = {
                "Найдите древний артефакт в глубинах Темного леса.",
                "Победите дракона, терроризирующего окрестности.",
                "Соберите 10 редких трав для алхимика.",
                "Очистите подземелье от нежити.",
                "Защитите деревню от набега гоблинов."
        };
        int randomIndex = (int) (Math.random() * titles.length);
        String id = UUID.randomUUID().toString();
        return new Task(id, titles[randomIndex], descriptions[randomIndex],
                LocalDate.now().plusDays(7), taskIcons[randomIndex % taskIcons.length],
                (int) (Math.random() * 50) + 50); // Награда от 50 до 100 монет
    }

    public Achievement generateWoWAchievement() {
        String[] titles = {
                "Первый шаг",
                "Мастер заданий",
                "Истребитель монстров",
                "Собиратель сокровищ",
                "Защитник слабых"
        };
        String[] descriptions = {
                "Выполните первое задание.",
                "Выполните 5 заданий.",
                "Победите 10 монстров.",
                "Соберите все сокровища.",
                "Защитите 5 деревень."
        };
        int[] costs = {50, 100, 150, 200, 250};
        int randomIndex = (int) (Math.random() * titles.length);
        String id = UUID.randomUUID().toString();
        return new Achievement(id, titles[randomIndex], descriptions[randomIndex],
                costs[randomIndex], achievementIcons[randomIndex % achievementIcons.length]);
    }

    public PersonalGoal generatePersonalGoal() {
        String[] titles = {
                "Купить кроссовки",
                "Сходить в кафе",
                "Купить книгу",
                "Сходить в кино",
                "Купить подарок"
        };
        String[] descriptions = {
                "Новые кроссовки для тренировок",
                "Попробовать новый кофе в любимом кафе",
                "Интересная книга для саморазвития",
                "Посмотреть новый фильм в кинотеатре",
                "Подарок для друга на день рождения"
        };
        int[] costs = {300, 150, 200, 250, 350};
        int randomIndex = (int) (Math.random() * titles.length);
        String id = UUID.randomUUID().toString();
        return new PersonalGoal(id, titles[randomIndex], descriptions[randomIndex], costs[randomIndex]);
    }
}
