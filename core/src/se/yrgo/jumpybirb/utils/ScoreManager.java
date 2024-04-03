package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

@SuppressWarnings("java:S6548")
public class ScoreManager {
    public static final String TAG = ScreenSwitcher.class.getSimpleName();
    private static ScoreManager instance;
    private int score;

    private ScoreManager() {
        score = 0;
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void updateScore() {
        score += 100;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
}