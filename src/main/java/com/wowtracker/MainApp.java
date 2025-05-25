package com.wowtracker;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.*;
import java.io.Serializable;


// Модели данных
class Task implements Serializable {
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

class User implements Serializable {
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

class PersonalGoal implements Serializable {
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

// Сервисы
class TaskService implements Serializable {
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

    public void completeTask(String taskId) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                task.setCompleted(true);
                break;
            }
        }
    }
}

class AchievementService implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Achievement> achievements = new ArrayList<>();

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

// Генератор заданий
class QuestGenerator {
    private ImageIcon[] taskIcons = {
            createResizedIcon("⚔️", 50, 50),
            createResizedIcon("📜", 50, 50),
            createResizedIcon("🔮", 50, 50),
            createResizedIcon("🗡️", 50, 50),
            createResizedIcon("🏹", 50, 50)
    };

    private ImageIcon[] achievementIcons = {
            createResizedIcon("🏆", 50, 50),
            createResizedIcon("🎖️", 50, 50),
            createResizedIcon("🏅", 50, 50),
            createResizedIcon("🥇", 50, 50),
            createResizedIcon("🌟", 50, 50)
    };

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
                (int)(Math.random() * 50) + 50); // Награда от 50 до 100 монет
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

// Панель для отображения задач/ачивок/целей с иконками
class IconListPanel extends JPanel {
    public IconListPanel(List<?> items) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 240, 240));

        for (Object item : items) {
            JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.setBackground(new Color(250, 250, 250));
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            if (item instanceof Task) {
                Task task = (Task) item;
                JLabel iconLabel = new JLabel(task.getIcon());
                JTextArea textArea = new JTextArea(task.toString());
                textArea.setEditable(false);
                textArea.setBackground(new Color(250, 250, 250));
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                itemPanel.add(iconLabel, BorderLayout.WEST);
                itemPanel.add(textArea, BorderLayout.CENTER);

                if (task.isCompleted()) {
                    itemPanel.setBackground(new Color(220, 255, 220));
                }
            }
            else if (item instanceof Achievement) {
                Achievement achievement = (Achievement) item;
                JLabel iconLabel = new JLabel(achievement.getIcon());
                JTextArea textArea = new JTextArea(achievement.toString());
                textArea.setEditable(false);
                textArea.setBackground(new Color(250, 250, 250));
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                itemPanel.add(iconLabel, BorderLayout.WEST);
                itemPanel.add(textArea, BorderLayout.CENTER);

                if (achievement.isUnlocked()) {
                    itemPanel.setBackground(new Color(220, 220, 255));
                }
            }
            else if (item instanceof PersonalGoal) {
                PersonalGoal goal = (PersonalGoal) item;
                JLabel iconLabel = new JLabel(new ImageIcon(
                        new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB)));
                JTextArea textArea = new JTextArea(goal.toString());
                textArea.setEditable(false);
                textArea.setBackground(new Color(250, 250, 250));
                textArea.setFont(new Font("Arial", Font.PLAIN, 12));
                itemPanel.add(iconLabel, BorderLayout.WEST);
                itemPanel.add(textArea, BorderLayout.CENTER);

                if (goal.isCompleted()) {
                    itemPanel.setBackground(new Color(255, 255, 220));
                }
            }

            add(itemPanel);
            add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
}

// Диалог для создания задачи
class CreateTaskDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JSpinner dateSpinner;
    private JSpinner rewardSpinner;
    private boolean confirmed = false;

    public CreateTaskDialog(JFrame parent) {
        super(parent, "Создать новую задачу", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Название задачи:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Описание:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        formPanel.add(new JLabel("Срок выполнения:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd.MM.yyyy"));
        formPanel.add(dateSpinner);

        formPanel.add(new JLabel("Награда (монеты):"));
        rewardSpinner = new JSpinner(new SpinnerNumberModel(50, 10, 1000, 10));
        formPanel.add(rewardSpinner);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = createStyledButton("Создать", new Color(76, 175, 80));
        okButton.setBackground(new Color(76, 175, 80));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        JButton cancelButton = createStyledButton("Отмена", new Color(244, 67, 54));
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Обновленный вариант с анимацией
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(color.darker().darker());
                } else if (getModel().isRollover()) {
                    g.setColor(color.darker());
                } else {
                    g.setColor(color);
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

            @Override
            public void updateUI() {
                // Переопределяем, чтобы кнопка не теряла стиль при обновлении UI
                super.updateUI();
                setContentAreaFilled(false);
                setOpaque(true);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color.darker(), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        };

        // Изменено с Color.WHITE на Color.BLACK
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        return button;
    }
    public String getTitle() {
        return titleField.getText();
    }

    public String getDescription() {
        return descriptionArea.getText();
    }

    public LocalDate getDueDate() {
        return ((java.util.Date)dateSpinner.getValue()).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    public int getReward() {
        return (int) rewardSpinner.getValue();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

// Диалог для создания цели
class CreateGoalDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JSpinner costSpinner;
    private boolean confirmed = false;

    public CreateGoalDialog(JFrame parent) {
        super(parent, "Создать новую цель", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        formPanel.add(new JLabel("Название цели:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Описание:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        formPanel.add(new JLabel("Стоимость (монеты):"));
        costSpinner = new JSpinner(new SpinnerNumberModel(100, 10, 1000, 10));
        formPanel.add(costSpinner);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Создать");
        okButton.setBackground(new Color(76, 175, 80));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getTitle() {
        return titleField.getText();
    }

    public String getDescription() {
        return descriptionArea.getText();
    }

    public int getCost() {
        return (int) costSpinner.getValue();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

// Основное окно приложения
public class MainApp extends JFrame {
    private TaskService taskService = new TaskService();
    private AchievementService achievementService = new AchievementService();
    private GoalService goalService = new GoalService();
    private QuestGenerator questGenerator = new QuestGenerator();
    private User user = new User(UUID.randomUUID().toString(), "Игрок", 100);

    private JLabel balanceLabel;
    private JPanel taskPanel;
    private JPanel achievementPanel;
    private JPanel goalPanel;

    private static final String SAVE_FILE = "save.dat";
    private Timer autoSaveTimer;

    public MainApp() {

        // Попытка загрузить сохранение
        if (new File(SAVE_FILE).exists()) {
            try {
                AppState loadedState = AppState.loadState(SAVE_FILE);
                this.user = loadedState.getUser();
                this.taskService = loadedState.getTaskService();
                this.achievementService = loadedState.getAchievementService();
                this.goalService = loadedState.getGoalService();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Файл сохранения повреждён или пустой. Будет создано новое.");

                // Создаём дефолтные данные
                this.user = new User(UUID.randomUUID().toString(), "Игрок", 100);
                this.taskService = new TaskService();
                this.achievementService = new AchievementService();
                this.goalService = new GoalService();

                // Добавляем ачивки
                achievementService.addAchievement(questGenerator.generateWoWAchievement());
                achievementService.addAchievement(questGenerator.generateWoWAchievement());

                // Перезаписываем файл сохранения
                try {
                    new AppState(user, taskService, achievementService, goalService).saveState(SAVE_FILE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Не удалось пересоздать файл сохранения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            this.user = new User(UUID.randomUUID().toString(), "Игрок", 100);
            achievementService.addAchievement(questGenerator.generateWoWAchievement());
            achievementService.addAchievement(questGenerator.generateWoWAchievement());
        }

        // Настройка автосохранения
        autoSaveTimer = new Timer(10_000, _ -> {
            try {
                new AppState(user, taskService, achievementService, goalService).saveState(SAVE_FILE);
            } catch (Exception ex) {
                System.err.println("Ошибка автосохранения");
                //ex.printStackTrace(); // Это покажет точное место ошибки
            }
        });
        autoSaveTimer.start();

        setTitle("WoW Task Manager");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Панель с балансом
        JPanel balancePanel = new JPanel(new BorderLayout());
        balancePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        balancePanel.setBackground(new Color(70, 130, 180));

        try {
            // Загружаем иконку монеты
            URL iconUrl = getClass().getClassLoader().getResource("token_icon.png");
            if (iconUrl == null) {
                System.err.println("Файл иконки не найден!");
                return;
            }

            // Читаем изображение через ImageIO
            BufferedImage originalImage = ImageIO.read(iconUrl);

            // Создаём ImageIcon из BufferedImage
            ImageIcon coinIcon = new ImageIcon(originalImage);

            // Создаём уменьшенную версию иконки (40x40)
            ImageIcon resizedCoinIcon = resizeIcon("token_icon.png", 40, 40);

            // Добавляем уменьшенную иконку монеты
            JLabel coinLabel = new JLabel(resizedCoinIcon);
            balancePanel.add(coinLabel, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Не удалось загрузить иконку монеты.");
        }

        // Добавляем текст баланса
        balanceLabel = new JLabel(" " + user.getCurrencyBalance());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(Color.WHITE);
        balancePanel.add(balanceLabel, BorderLayout.CENTER);

        add(balancePanel, BorderLayout.NORTH);

        // Скрываем кнопку "+10 монет"
        JButton addCurrencyButton = new JButton("+10 монет");
        addCurrencyButton.setBackground(new Color(255, 193, 7));
        addCurrencyButton.setForeground(Color.BLACK);
        addCurrencyButton.setFocusPainted(false);
        addCurrencyButton.setVisible(false); // Скрыта по умолчанию
        addCurrencyButton.addActionListener(e -> {
            user.setCurrencyBalance(user.getCurrencyBalance() + 10);
            updateBalance();
        });
        balancePanel.add(addCurrencyButton, BorderLayout.EAST);

        // Добавляем панель в верхнюю часть окна
        add(balancePanel, BorderLayout.NORTH);

        // Панель с кнопками
        // Добавить в конструктор MainApp перед созданием кнопок:
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            JFrame.setDefaultLookAndFeelDecorated(true);
            UIManager.put("Button.background", new ColorUIResource(238, 238, 238));
            UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton createTaskButton = createStyledButton("Создать задачу", new Color(33, 150, 243));
        JButton generateTaskButton = createStyledButton("Сгенерировать задачу", new Color(156, 39, 176));
        JButton completeTaskButton = createStyledButton("Выполнить задачу", new Color(76, 175, 80));
        JButton createGoalButton = createStyledButton("Создать цель", new Color(255, 152, 0));
        JButton completeGoalButton = createStyledButton("Достичь цель", new Color(233, 30, 99));
        JButton unlockAchievementButton = createStyledButton("Разблокировать ачивку", new Color(0, 150, 136));

        buttonPanel.add(createTaskButton);
        buttonPanel.add(generateTaskButton);
        buttonPanel.add(completeTaskButton);
        buttonPanel.add(createGoalButton);
        buttonPanel.add(completeGoalButton);
        buttonPanel.add(unlockAchievementButton);

        // Области для отображения задач, ачивок и целей
        taskPanel = new JPanel();
        achievementPanel = new JPanel();
        goalPanel = new JPanel();

        JScrollPane taskScrollPane = new JScrollPane(taskPanel);
        JScrollPane achievementScrollPane = new JScrollPane(achievementPanel);
        JScrollPane goalScrollPane = new JScrollPane(goalPanel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Задачи", taskScrollPane);
        tabbedPane.addTab("Ачивки", achievementScrollPane);
        tabbedPane.addTab("Личные цели", goalScrollPane);

        // Обработчики событий
        createTaskButton.addActionListener(e -> {
            CreateTaskDialog dialog = new CreateTaskDialog(this);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                ImageIcon randomIcon = questGenerator.createResizedIcon(
                        new String[]{"⚔️", "📜", "🔮", "🗡️", "🏹"}[(int)(Math.random() * 5)], 50, 50);
                taskService.createTask(
                        dialog.getTitle(),
                        dialog.getDescription(),
                        dialog.getDueDate(),
                        randomIcon,
                        dialog.getReward()
                );
                updateTaskPanel();
                JOptionPane.showMessageDialog(this, "Задача создана!");
            }
        });

        generateTaskButton.addActionListener(e -> {
            Task task = questGenerator.generateWoWStyleTask();
            taskService.createTask(task.getTitle(), task.getDescription(), task.getDueDate(), task.getIcon(), task.getReward());
            updateTaskPanel();
            JOptionPane.showMessageDialog(this, "Задача сгенерирована: " + task.getTitle());
        });

        completeTaskButton.addActionListener(e -> {
            if (taskService.getTasks().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Нет задач для выполнения!");
                return;
            }

            String[] taskOptions = taskService.getTasks().stream()
                    .filter(task -> !task.isCompleted())
                    .map(task -> task.getId() + ": " + task.getTitle() + " (" + task.getReward() + " монет)")
                    .toArray(String[]::new);

            if (taskOptions.length == 0) {
                JOptionPane.showMessageDialog(this, "Все задачи уже выполнены!");
                return;
            }

            String selectedTask = (String) JOptionPane.showInputDialog(
                    this, "Выберите задачу для выполнения:", "Выполнение задачи",
                    JOptionPane.PLAIN_MESSAGE, null, taskOptions, taskOptions[0]);

            if (selectedTask != null) {
                String taskId = selectedTask.split(":")[0].trim();
                Task task = taskService.getTasks().stream()
                        .filter(t -> t.getId().equals(taskId))
                        .findFirst()
                        .orElse(null);

                if (task != null) {
                    taskService.completeTask(taskId);
                    user.setCurrencyBalance(user.getCurrencyBalance() + task.getReward());
                    updateBalance();
                    updateTaskPanel();

                    // Проверка на разблокировку ачивки "Первый шаг"
                    long completedCount = taskService.getTasks().stream().filter(Task::isCompleted).count();
                    if (completedCount == 1) {
                        achievementService.getAchievements().stream()
                                .filter(a -> a.getTitle().contains("Первый шаг"))
                                .findFirst()
                                .ifPresent(a -> {
                                    achievementService.unlockAchievement(user, a.getId());
                                    updateAchievementPanel();
                                });
                    }

                    JOptionPane.showMessageDialog(this,
                            "Задача выполнена! Начислено " + task.getReward() + " монет.");
                }
            }
        });

        createGoalButton.addActionListener(e -> {
            CreateGoalDialog dialog = new CreateGoalDialog(this);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                String id = UUID.randomUUID().toString();
                goalService.addGoal(new PersonalGoal(
                        id,
                        dialog.getTitle(),
                        dialog.getDescription(),
                        dialog.getCost()
                ));
                updateGoalPanel();
                JOptionPane.showMessageDialog(this, "Цель создана!");
            }
        });

        completeGoalButton.addActionListener(e -> {
            if (goalService.getGoals().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Нет доступных целей!");
                return;
            }

            String[] goalOptions = goalService.getGoals().stream()
                    .filter(goal -> !goal.isCompleted())
                    .map(goal -> goal.getId() + ": " + goal.getTitle() + " (" + goal.getCost() + " монет)")
                    .toArray(String[]::new);

            if (goalOptions.length == 0) {
                JOptionPane.showMessageDialog(this, "Все цели уже достигнуты!");
                return;
            }

            String selectedGoal = (String) JOptionPane.showInputDialog(
                    this, "Выберите цель для достижения:", "Достижение цели",
                    JOptionPane.PLAIN_MESSAGE, null, goalOptions, goalOptions[0]);

            if (selectedGoal != null) {
                String goalId = selectedGoal.split(":")[0].trim();
                goalService.completeGoal(user, goalId);
                updateBalance();
                updateGoalPanel();
            }
        });

        unlockAchievementButton.addActionListener(e -> {
            if (achievementService.getAchievements().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Нет доступных ачивок!");
                return;
            }

            String[] achievementOptions = achievementService.getAchievements().stream()
                    .filter(achievement -> !achievement.isUnlocked())
                    .map(achievement -> achievement.getId() + ": " + achievement.getTitle() + " (" + achievement.getCost() + " монет)")
                    .toArray(String[]::new);

            if (achievementOptions.length == 0) {
                JOptionPane.showMessageDialog(this, "Все ачивки уже разблокированы!");
                return;
            }

            String selectedAchievement = (String) JOptionPane.showInputDialog(
                    this, "Выберите ачивку для разблокировки:", "Разблокировка ачивки",
                    JOptionPane.PLAIN_MESSAGE, null, achievementOptions, achievementOptions[0]);

            if (selectedAchievement != null) {
                String achievementId = selectedAchievement.split(":")[0].trim();
                achievementService.unlockAchievement(user, achievementId);
                updateBalance();
                updateAchievementPanel();
            }
        });

        // Добавляем компоненты в окно
        add(buttonPanel, BorderLayout.SOUTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Инициализация панелей
        updateTaskPanel();
        updateAchievementPanel();
        updateGoalPanel();
    }

    private ImageIcon resizeIcon(String resourcePath, int width, int height) {
        try {
            // Получаем URL ресурса
            URL imageUrl = getClass().getClassLoader().getResource(resourcePath);
            if (imageUrl == null) {
                System.err.println("Файл не найден: " + resourcePath);
                return null;
            }

            // Читаем BufferedImage напрямую
            BufferedImage originalImage = ImageIO.read(imageUrl);

            // Создаём новое изображение с поддержкой прозрачности
            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Масштабируем изображение
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();

            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Современный шрифт
        button.setForeground(Color.WHITE); // Белый текст
        button.setBackground(color); // Цвет кнопки
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Поля вокруг текста
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Курсор при наведении
        button.setContentAreaFilled(true); // Заливка области кнопки
        button.setOpaque(true); // Прозрачность
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker()); // Темнее при наведении
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color); // Возвращаем исходный цвет
            }
        });
        return button;
    }

    private void updateBalance() {
        balanceLabel.setText(" " + user.getCurrencyBalance());
    }

    private void updateTaskPanel() {
        taskPanel.removeAll();
        taskPanel.setLayout(new BorderLayout());

        if (taskService.getTasks().isEmpty()) {
            JLabel emptyLabel = new JLabel("Нет задач. Создайте новую задачу!", JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            taskPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            taskPanel.add(new IconListPanel(taskService.getTasks()), BorderLayout.NORTH);
        }

        taskPanel.revalidate();
        taskPanel.repaint();
    }

    private void updateAchievementPanel() {
        achievementPanel.removeAll();
        achievementPanel.setLayout(new BorderLayout());

        if (achievementService.getAchievements().isEmpty()) {
            JLabel emptyLabel = new JLabel("Нет ачивок.", JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            achievementPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            achievementPanel.add(new IconListPanel(achievementService.getAchievements()), BorderLayout.NORTH);
        }

        achievementPanel.revalidate();
        achievementPanel.repaint();
    }

    private void updateGoalPanel() {
        goalPanel.removeAll();
        goalPanel.setLayout(new BorderLayout());

        if (goalService.getGoals().isEmpty()) {
            JLabel emptyLabel = new JLabel("Нет личных целей.", JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            goalPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            goalPanel.add(new IconListPanel(goalService.getGoals()), BorderLayout.NORTH);
        }

        goalPanel.revalidate();
        goalPanel.repaint();
    }

    @Override
    public void dispose() {
        autoSaveTimer.stop();
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}


