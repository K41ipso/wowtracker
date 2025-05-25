package com.wowtracker;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.net.URL;

public class BackgroundMusicPlayer {
    private Clip clip;
    private static Clip backgroundClip;

    public void playBackgroundMusic(String resourcePath) {
        try {
            // Загружаем звук как InputStream
            InputStream audioFile = getClass().getResourceAsStream(resourcePath);
            if (audioFile == null) {
                System.err.println("Файл не найден: " + resourcePath);
                return;
            }

            // Открываем аудиопоток
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Включаем повторение
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Ошибка воспроизведения фоновой музыки");
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    public static void playSound(URL url, boolean main_music) {
        try {
            if (url == null) {
                System.err.println("Звуковой файл не найден: " + url);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            if (main_music) {
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioIn);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.open(audioIn);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Ошибка воспроизведения звука: " + e.getMessage() + url);
        }
    }
}