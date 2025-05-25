package com.wowtracker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class AchievementService implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Achievement> achievements = new ArrayList<>();

    public AchievementService() {
        initDefaultAchievements();
    }

    private ImageIcon[] loadIconsFromResources() {
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

    public void initDefaultAchievements() {
        String[] easyTitles = {
                "Путь начинающего героя", "Первая цель достигнута!", "Маленький шаг", "Исследователь локальных мест",
                "Герой района", "Добро пожаловать в приключения", "Квест начат", "Стартовый импульс", "Посланник добра",
                "Первый успех", "Зелёный новичок", "Малыш-герой", "В поисках приключений", "Путешественник по району",
                "Разминка началась", "Боевой дух проснулся", "Начало великой истории", "Покоритель улиц",
                "Мир открылся", "Приключение начинается", "Тренировка началась", "Первые шаги", "Герой дня",
                "Один шаг вперёд", "Смелость вознаграждается", "Начало легенды", "Цель достигнута", "Простое задание",
                "Первый квест", "Призыв судьбы", "Следуй за мечтой", "Путь героя", "Испытание пройдено",
                "Маленькое дело", "Учусь побеждать", "Новая эра", "Сила внутри тебя", "Победа над страхом",
                "Первый вызов принят", "Смелость в действии", "Герой по зову сердца", "Путь развития",
                "Первый опыт", "Победа над собой", "Путь совершенствования", "Герой без клинка",
                "Победа без боя", "Сила намерения", "Герой без страха"
        };

        String[] easyDescriptions = {
                "Выполни свою первую задачу.", "Заверши любую цель.", "Сделай первый шаг к большим делам.",
                "Посети интересное место неподалёку.", "Прогуляйся по знакомому району с новым взглядом.",
                "Начни день с маленького подвига.", "Открой для себя новые горизонты.", "Попробуй что-то новое.",
                "Сделай добрые дела для окружающих.", "Поздравляем с первым успехом!", "Пройди путь новичка.",
                "Попробуй сделать что-то необычное.", "Ищи приключения рядом.", "Исследуй окрестности.",
                "Начни с простого.", "Вдохновляй других своими действиями.", "Сделай первый важный шаг.",
                "Открой для себя мир вокруг.", "Начни свой путь.", "Попробуй что-то новое сегодня.",
                "Тренируй силу воли.", "Сделай первый шаг к цели.", "Соверши подвиг дня.", "Не бойся действовать.",
                "Попробуй выполнить что-то новое.", "Пусть начинается твоя история.", "Покажи свои способности.",
                "Выполни простое задание.", "Начни своё приключение.", "Ответь на призыв судьбы.",
                "Иди за своей мечтой.", "Пройди через испытание.", "Соверши маленькое чудо.",
                "Попробуй выйти из зоны комфорта.", "Учись преодолевать трудности.", "Пусть начнётся твоя эра.",
                "Найди силы в себе.", "Преодолей свои страхи.", "Прими первый вызов.", "Смелость — твой щит.",
                "Иди вперёд.", "Покажи пример другим.", "Действуй без страха.", "Изменяй мир вокруг.",
                "Действуй от сердца.", "Покажи, что ты можешь."
        };

        String[] mediumTitles = {
                "Герой повседневности", "Мастер бытовых дел", "Искусный организатор", "Хранитель порядка",
                "Эксперт в деле", "Создатель изменений", "Лидер в команде", "Вдохновляющий лидер",
                "Мастер саморазвития", "Архитектор своих целей", "Победитель инерции", "Покоритель времени",
                "Стратагема дня", "Тактический гений", "Герой планирования", "Мастер контроля",
                "Чемпион продуктивности", "Покоритель хаоса", "Архитектор успеха", "Герой стратегии",
                "Мастер распорядка", "Гений организации", "Покоритель недели", "Герой графика",
                "Мастер рационализации", "Гений оптимизации", "Победитель забывчивости", "Герой планирования",
                "Архитектор порядка", "Герой системности"
        };

        String[] mediumDescriptions = {
                "Научись быть героем в обычной жизни.", "Освой управление своими делами.", "Создай идеальный порядок.",
                "Поддерживай гармонию вокруг.", "Стань экспертом в любимом деле.", "Создай что-то ценное.",
                "Возглавь группу людей.", "Вдохновляй команду на победы.", "Развивайся каждый день.",
                "Планируй жизнь как мастер.", "Победи инерцию и лень.", "Управляй временем как чемпион.",
                "Разработай план на день.", "Прояви тактическое мышление.", "Организуй идеальное планирование.",
                "Контролируй свои действия.", "Достигай максимальной продуктивности.", "Управь хаос.",
                "Построй путь к успеху.", "Разработай стратегию достижений.", "Создай идеальный режим дня.",
                "Стань гением организации.", "Покори неделю.", "Управляй своим графиком.",
                "Оптимизируй процессы.", "Стань мастером эффективности.", "Победи забывчивость.",
                "Планируй всё заранее.", "Создай систему порядка.", "Стань героем системного подхода."
        };

        String[] hardTitles = {
                "Герой всех времён", "Мастер сверхзадач", "Легендарный перформер", "Титан саморазвития",
                "Гений реализации", "Победитель невозможного", "Архимаг планирования", "Лидер эпохи",
                "Покоритель вершин", "Мастер невероятного", "Герой экстремальных условий", "Создатель нового мира",
                "Гений стратегического мышления", "Тактик уровня богов", "Герой перемен", "Мастер масштабных решений",
                "Победитель всех испытаний", "Архитектор будущего", "Герой трансформации", "Гений управления временем"
        };

        String[] hardDescriptions = {
                "Выполни то, что никто не смог.", "Реализуй самые смелые проекты.", "Стань эталоном результативности.",
                "Пройди путь саморазвития до максимума.", "Реализуй уникальные идеи.", "Преодолей невозможное.",
                "Создай идеальную стратегию на годы.", "Стань лидером поколения.", "Покори самые высокие цели.",
                "Реализуй мечты, казавшиеся недостижимыми.", "Достигай целей в самых сложных условиях.",
                "Создай что-то по-настоящему новое.", "Стань гением долгосрочного планирования.",
                "Прояви божественный уровень тактики.", "Измени мир вокруг себя.", "Реализуй масштабные изменения.",
                "Пройди все испытания.", "Создай визию будущего.", "Измени себя и мир вокруг.",
                "Управляй временем как бессмертный."
        };

        ImageIcon[] icons = loadIconsFromResources(); // Загружаем 23 картинки

        // Лёгкие (50)
        for (int i = 0; i < 50; i++) {
            int cost = (int) (Math.random() * 90) + 10;
            String title = easyTitles[i % easyTitles.length] + " #" + (i + 1);
            String description = easyDescriptions[i % easyDescriptions.length];
            int iconIndex = i % icons.length;
            addAchievement(new Achievement(UUID.randomUUID().toString(), title, description, cost, icons[iconIndex]));
        }

        // Средние (30)
        for (int i = 0; i < 30; i++) {
            int cost = (int) (Math.random() * 150) + 100;
            String title = mediumTitles[i % mediumTitles.length] + " #" + (i + 1);
            String description = mediumDescriptions[i % mediumDescriptions.length];
            int iconIndex = i % icons.length;
            addAchievement(new Achievement(UUID.randomUUID().toString(), title, description, cost, icons[iconIndex]));
        }

        // Сложные (20)
        for (int i = 0; i < 20; i++) {
            int cost = (int) (Math.random() * 2000) + 1000;
            String title = hardTitles[i % hardTitles.length] + " #" + (i + 1);
            String description = hardDescriptions[i % hardDescriptions.length];
            int iconIndex = i % icons.length;
            addAchievement(new Achievement(UUID.randomUUID().toString(), title, description, cost, icons[iconIndex]));
        }
    }

    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    public void unlockAchievement(User user, String achievementId) {
        Achievement achievement = achievements.stream()
                .filter(a -> a.getId().equals(achievementId))
                .findFirst()
                .orElse(null);

        if (achievement != null && !achievement.isUnlocked() && user.getCurrencyBalance() >= achievement.getCost()) {
            user.setCurrencyBalance(user.getCurrencyBalance() - achievement.getCost());
            achievement.setUnlocked(true);
            user.addAchievement(achievementId);
            JOptionPane.showMessageDialog(null, "Ачивка разблокирована: " + achievement.getTitle());
        } else if (achievement != null && achievement.isUnlocked()) {
            JOptionPane.showMessageDialog(null, "Ачивка уже разблокирована!");
        } else {
            JOptionPane.showMessageDialog(null, "Недостаточно монет или ачивка не найдена.");
        }
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }
}
