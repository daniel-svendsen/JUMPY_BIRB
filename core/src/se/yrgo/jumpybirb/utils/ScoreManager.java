package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/***
 * This class is used to keep score of the game.
 * Saves the players score in preferences
 * so the players highscore gets saved and loads even
 * if the player exits and runs the game at another time.
 */

public class ScoreManager {
    private int score;
    private int highScore;
    private float elapsedTime;
    private Preferences preferences;
    private static ScoreManager instance;  // Declare the instance field

    /***
     * Constructor. Initialize score and highscore to 0 for first run.
     * Loads previous highscore if there is one.
     */
    public ScoreManager() {
        // Initialize score, high score, and elapsed time
        preferences = Gdx.app.getPreferences("se.yrgo.jumpybirb.settings");

        // Check if high score has been initialized
        if (!preferences.contains("highScore")) {
            Gdx.app.log("ScoreManager", "High score not found, setting to 0");
            highScore = 0;
            saveHighScore();
        } else {
            loadHighScore(); // Load high score from preferences
        }

        score = 0;
        elapsedTime = 0;
    }

    /***
     * "Singleton pattern" that restricts the instantiation of this class
     * and ensures that only one instance of the class exists in the JVM
     * @return at maximum: only one instance of ScoreManager
     */
    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }


    /***
     * This method is used to update the score
     * @param delta elapsed time
     */
    public void update(float delta) {
        // Increment the elapsed time
        elapsedTime += delta;

        // Add 10 score every 3rd second
        if (elapsedTime >= 3) {
            score += 10;

            // Update high score if the current score is higher than existing highscore
            if (score > highScore) {
                highScore = score;
                saveHighScore();
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

    /***
     * This method is used to pass the scoreManager to PlayScreen.
     * @return the only instance of ScoreManager in the JVM
     * @see se.yrgo.jumpybirb.screens.PlayScreen
     */
    public ScoreManager getScoreManager() {
        return instance;
    }

    /**
     * Save the highscore when resetting the score.
     */
    public void resetScore() {
        score = 0;
        elapsedTime = 0;
        saveHighScore();
    }

    /**
     * This method is used to load the highscore from Preferences.
     * @see Preferences
     */
    private void loadHighScore() {
        if (!preferences.contains("highScore")) {
            Gdx.app.log("ScoreManager", "High score not found, setting to 0");
            // If "highScore" is not present, set it to 0
            highScore = 0;
            preferences.putInteger("highScore", 0);
            preferences.flush(); // Save the initial value
        } else {
            highScore = preferences.getInteger("highScore", 0);
        }
        Gdx.app.log("ScoreManager", "Loaded high score: " + highScore);
    }

    /**
     * This method is used to save the highscore to Preferences.
     */
    private void saveHighScore() {
        preferences.putInteger("highScore", highScore);
        preferences.flush(); // Ensure preferences are saved immediately

        Gdx.app.log("ScoreManager", "High score saved: " + highScore);
    }
}
