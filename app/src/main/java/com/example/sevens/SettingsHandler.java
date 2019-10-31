package com.example.sevens;

import android.content.Context;

public class SettingsHandler {
    static ActivityMain m_app = null;
    public SettingsHandler(Context app) {
        m_app = (ActivityMain) app;
    }

    private static float music_volume = 1;
    private static float sound_volume = 1;
    private static boolean isSoundOn = true;
    private static boolean isMusicOn = true;
    private static int language = 0;
    private static int score = 0;
    private static int high_score = 0;



    public static void setMusicVolume(float new_value) {
        music_volume = new_value;
        if(m_app != null)
            m_app.soundBox.resetMusicVolume();
    }

    public static void setSoundVolume(float new_value) {
        sound_volume = new_value;
        if(m_app != null)
            m_app.soundBox.resetSoundVolume();
    }

    public static void setLanguage(int new_value) {
        language = new_value;
    }

    public static void setIsSoundOn(boolean new_value) {
        isSoundOn = new_value;
        if(m_app != null)
            m_app.soundBox.resetSoundVolume();
    }

    public static void setIsMusicOn(boolean new_value) {
        isMusicOn = new_value;
        if(m_app != null) {
            m_app.soundBox.resetMusicVolume();
        }

    }

    public static void setScore(int new_score) {
        score = new_score;
    }
    public static void setHighScore(int new_score) {
        high_score = new_score;
    }


    public static float getMusicVolume() {
        if(isMusicOn)
            return music_volume;
        else
            return 0;
    }

    public static float getSoundVolume() {
        if(isSoundOn)
            return sound_volume;
        else
            return 0;
    }

    public static int getLanguage() {
        return language;
    }
    public static boolean getIsSoundOn() {
        return isSoundOn;
    }
    public static boolean getIsMusicOn() {
        return isMusicOn;
    }
    public static int getScore() {
        return score;
    }
    public static int getHighScore() {
        return high_score;
    }

}
