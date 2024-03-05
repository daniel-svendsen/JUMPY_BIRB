package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScoreManager {
    private static final String PREFERENCES_KEY = "se.yrgo.jumpybirb.settings";
    private static final String HIGH_SCORE_KEY = "highScore";

    private static ScoreManager instance;

    private Preferences preferences;
    private int score;
    private int highScore;


    private ScoreManager() {
        preferences = Gdx.app.getPreferences(PREFERENCES_KEY);
        loadHighScore();
        score = 0;

    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void updateScore() {
        score += 1;
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    private void loadHighScore() {
        highScore = preferences.getInteger(HIGH_SCORE_KEY, 0);
        Gdx.app.log("ScoreManager", "Loaded high score: " + highScore);
    }

    private void saveHighScore() {
        preferences.putInteger(HIGH_SCORE_KEY, highScore);
        preferences.flush();
        Gdx.app.log("ScoreManager", "High score saved: " + highScore);
    }

    public void reset() {
        score = 0;

        saveHighScore(); // Resetting also implies resetting the high score
    }
}