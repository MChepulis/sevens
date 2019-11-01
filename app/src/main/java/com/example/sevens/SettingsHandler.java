package com.example.sevens;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.BoringLayout;

import static android.content.Context.MODE_PRIVATE;

public class SettingsHandler {
    public static final String MUSIC_VOL_KEY = "music";
    public static final String SOUND_VOL_KEY = "sound";
    public static final String IS_SOUND_ON_KEY = "is_sound_on";
    public static final String IS_MUSIC_ON_KEY = "is_music_on";
    public static final String SCORE_KEY = "score";
    public static final String HIGH_SCORE_KEY = "high_score";
    public static final String HEX_GRID_KEY = "hex_grid";
    public static final String PUZZLE_KEY = "puzzle";


    private static ActivityMain m_app = null;
    private static SharedPreferences sPref;

    public SettingsHandler(Context app) {
        m_app = (ActivityMain) app;
        sPref = m_app.getPreferences(MODE_PRIVATE);
        music_volume = sPref.getFloat(MUSIC_VOL_KEY, 1);
        sound_volume = sPref.getFloat(SOUND_VOL_KEY, 1);
        isSoundOn = sPref.getBoolean(IS_SOUND_ON_KEY, true);
        isMusicOn = sPref.getBoolean(IS_MUSIC_ON_KEY, true);
        score = sPref.getInt(SCORE_KEY, 200);
        high_score = sPref.getInt(HIGH_SCORE_KEY, 0);
        hex_grid_str = sPref.getString(HEX_GRID_KEY, "");
        puzzle_str = sPref.getString(PUZZLE_KEY, "");

        System.out.println("InitScore: " + score);
        if (m_app != null) {
            m_app.soundBox.resetMusicVolume();
            m_app.soundBox.resetSoundVolume();
        }
    }

    private static float music_volume;
    private static float sound_volume;
    private static boolean isSoundOn;
    private static boolean isMusicOn;
    private static int score;
    private static int high_score;
    private static String hex_grid_str;
    private static String puzzle_str;

    private static void saveStringSetting(String name, String value) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(name, value);
        ed.apply();
    }

    private static void saveIntSetting(String name, Integer value) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(name, value);
        ed.apply();
    }

    private static void saveFloatSetting(String name, Float value) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putFloat(name, value);
        ed.apply();
    }

    private static void saveBoolSetting(String name, Boolean value) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(name, value);
        ed.apply();
    }

    public static void setMusicVolume(float new_value) {
        music_volume = new_value;
        saveFloatSetting(MUSIC_VOL_KEY, new_value);
        if (m_app != null)
            m_app.soundBox.resetMusicVolume();

    }

    public static void setSoundVolume(float new_value) {
        sound_volume = new_value;
        saveFloatSetting(SOUND_VOL_KEY, new_value);
        if (m_app != null)
            m_app.soundBox.resetSoundVolume();
    }


    public static void setIsSoundOn(boolean new_value) {
        isSoundOn = new_value;
        saveBoolSetting(IS_SOUND_ON_KEY, new_value);
        if (m_app != null)
            m_app.soundBox.resetSoundVolume();
    }

    public static void setIsMusicOn(boolean new_value) {
        isMusicOn = new_value;
        saveBoolSetting(IS_MUSIC_ON_KEY, new_value);
        if (m_app != null) {
            m_app.soundBox.resetMusicVolume();
        }
    }

    public static void setScore(int new_value) {
        score = new_value;
        System.out.println("setScore: " + new_value);
        saveIntSetting(SCORE_KEY, new_value);
    }

    public static void setHighScore(int new_value) {
        high_score = new_value;
        saveIntSetting(HIGH_SCORE_KEY, new_value);
    }

    public static void setHexGridStr(String new_value) {
        hex_grid_str = new_value;
        saveStringSetting(HEX_GRID_KEY, new_value);
    }

    public static void setPuzzleStr(String new_value) {
        puzzle_str = new_value;
        saveStringSetting(PUZZLE_KEY, new_value);
    }

    public static float getMusicVolume() {
        if (isMusicOn)
            return music_volume;
        else
            return 0;
    }

    public static float getSoundVolume() {
        if (isSoundOn)
            return sound_volume;
        else
            return 0;
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

    public static String getHexGridStr() {
        return hex_grid_str;
    }

    public static String getPuzzleStr() {
        return puzzle_str;
    }


}
