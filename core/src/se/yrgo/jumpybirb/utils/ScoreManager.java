package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScoreManager {
    private int score;
    private int highScore;
    private float elapsedTime;
    private Preferences preferences;

    public ScoreManager() {
        // Initialize score, high score, and elapsed time
        preferences = Gdx.app.getPreferences("se.yrgo.jumpybirb.settings");
        loadHighScore(); // Load high score from preferences
        score = 0;
        elapsedTime = 0;
    }

    public void update(float delta) {
        // Increment the elapsed time
        elapsedTime += delta;

        // Check if 3 seconds have passed
        if (elapsedTime >= 3) {
            // Add 1 to the score
            score += 10;

            // Update high score if the current score is higher
            if (score > highScore) {
                highScore = score;
            }

            // Reset the elapsed time
            elapsedTime = 0;
        }
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void resetScore() {
        score = 0;
        elapsedTime = 0;
        saveHighScore(); // Save the high score when resetting the score
    }

    private void loadHighScore() {
        highScore = preferences.getInteger("highScore", 0);
    }

    private void saveHighScore() {
        preferences.putInteger("highScore", highScore);
        preferences.flush(); // Ensure preferences are saved immediately

        Gdx.app.log("ScoreManager", "High score saved: " + highScore);
    }
}
