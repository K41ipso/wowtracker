package com.wowtracker;

import com.wowtracker.components.IconListPanel;
import com.wowtracker.dialogs.CreateGoalDialog;
import com.wowtracker.dialogs.CreateTaskDialog;
import com.wowtracker.media.BackgroundMusicPlayer;
import com.wowtracker.model.PersonalGoal;
import com.wowtracker.model.Task;
import com.wowtracker.model.User;
import com.wowtracker.quest.QuestGenerator;
import com.wowtracker.service.AchievementService;
import com.wowtracker.service.GoalService;
import com.wowtracker.service.TaskService;
import com.wowtracker.state.AppState;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.UUID;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static com.wowtracker.quest.QuestGenerator.createResizedIcon;


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

    private BackgroundMusicPlayer musicPlayer = new BackgroundMusicPlayer();

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
                //achievementService.addAchievement(questGenerator.generateWoWAchievement());
                //achievementService.addAchievement(questGenerator.generateWoWAchievement());

                // Перезаписываем файл сохранения
                try {
                    new AppState(user, taskService, achievementService, goalService).saveState(SAVE_FILE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Не удалось пересоздать файл сохранения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            this.user = new User(UUID.randomUUID().toString(), "Игрок", 100);
            //achievementService.addAchievement(questGenerator.generateWoWAchievement());
            //achievementService.addAchievement(questGenerator.generateWoWAchievement());
        }

        URL imageUrl = getClass().getClassLoader().getResource("sounds/main_theme.wav");
        musicPlayer.playSound(imageUrl, true);

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
            playRandomButtonClickSound(); // Звук при нажатии

            CreateTaskDialog dialog = new CreateTaskDialog(this);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                ImageIcon randomIcon = createResizedIcon(
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
            playRandomButtonClickSound(); // Звук при нажатии
            Task task = questGenerator.generateWoWStyleTask();
            taskService.createTask(task.getTitle(), task.getDescription(), task.getDueDate(), task.getIcon(), task.getReward());
            updateTaskPanel();
            JOptionPane.showMessageDialog(this, "Задача сгенерирована: " + task.getTitle());
        });

        completeTaskButton.addActionListener(e -> {
            playRandomButtonClickSound(); // Звук при нажатии

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
            playRandomButtonClickSound(); // Звук при нажатии
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
            playRandomButtonClickSound(); // Звук при нажатии
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
            playRandomButtonClickSound(); // Звук при нажатии
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

    private void playRandomButtonClickSound() {
        String[] soundFiles = {
                "sounds/buldiga_1.wav",
                "sounds/buldiga_2.wav",
                "sounds/buldiga_3.wav"
        };

        int randomIndex = (int) (Math.random() * soundFiles.length);
        String selectedSound = soundFiles[randomIndex];

        URL soundUrl = getClass().getClassLoader().getResource(selectedSound);
        if (soundUrl != null) {
            musicPlayer.playSound(soundUrl, false); // false — это не фоновая музыка
        } else {
            System.err.println("Звуковой файл не найден: " + selectedSound);
        }
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

    public static void playButtonClickSound(String soundPath) {
        try {
            File soundFile = new File(soundPath);
            if (!soundFile.exists()) return;

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception ex) {
            System.err.println("Ошибка воспроизведения звука");
        }
    }
}
