package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;

import java.util.*;

@SuppressWarnings("java:S6548")
public class HighscoreManager {
    public static final String TAG = ScreenSwitcher.class.getSimpleName();
    private static final int HIGHSCORE_ENTRIES = 10;
    private TreeMap<Integer, String> highScoreBoard;
    private static final String PREF_FILE_NAME = "se.yrgo.jumpybirb.settings";
    private static final String HIGHSCORE_PREF_KEY = "highscore";
    private Preferences preferences;
    private static HighscoreManager instance;

    private HighscoreManager() {
        highScoreBoard = new TreeMap<>(Comparator.reverseOrder());
        loadHighScores();
    }

    public static HighscoreManager getInstance() {
        if (instance == null) {
            instance = new HighscoreManager();
        }
        return instance;
    }

    public Map<Integer, String> getHighScoreBoard() {
        return highScoreBoard;
    }

    /**
     * This method is initially called to check if the player
     * has a score that qualifies to be on the highScoreBoard.
     * If the score is high enough, this method calls saveHighScores().
     * @param score from the player's game session
     */
    public void checkIfScoreIsHighScore(int score, String playerName) {
        Gdx.app.log(TAG, "Checking if score is a high score. Score: " + score + ", Player Name: " + playerName);
        if (score > getLowestScore()) {
            addHighscore(score, playerName);
            saveHighScores();
        }
    }

    /**
     * When adding a new highscore entry, it will be sorted in
     * the correct position using the sortByKey() method.
     * @param score
     */
    public void addHighscore(int score, String playerName) {

        highScoreBoard.put(score, playerName);

        // Remove any excess entries beyond the HIGHSCORE_ENTRIES limit
        if (highScoreBoard.size() > HIGHSCORE_ENTRIES) {
            Iterator<Map.Entry<Integer, String>> iterator = highScoreBoard.entrySet().iterator();
            int count = 0;
            while (iterator.hasNext() && count < HIGHSCORE_ENTRIES) {
                iterator.next();
                count++;
            }
            iterator.remove();
        }
        saveHighScores();
    }

    /***
     * Load highScores from preferences file and
     * put them into the HashMap highScores.
     */
    private void loadHighScores() {
        preferences = Gdx.app.getPreferences(PREF_FILE_NAME);
        String savedHighScores = preferences.getString(HIGHSCORE_PREF_KEY, "");

        // if there are saved highScores, parse and put them into the highscoreBoard
        if (!savedHighScores.isEmpty()) {
            String[] highscoreRows = savedHighScores.split(",");
            for (String row : highscoreRows) {
                String[] splitHighScoreRows = row.split(":");
                highScoreBoard.put(Integer.parseInt(splitHighScoreRows[0]), splitHighScoreRows[1]);
            }
        } else {
            Gdx.app.log(TAG, " loaded highSores but zero entries found");
        }
        Gdx.app.log(TAG, " loaded highScores successfully");
    }

    /**
     * Save all entries in the TreeMap highScoreBoard into the Preferences file.
     * This method converts the TreeMap into a single string (serializing it) and
     * saves it into the Preferences file.
     */
    private void saveHighScores() {
        StringBuilder sb = new StringBuilder();
        if (!highScoreBoard.isEmpty()) {
            for (Map.Entry<Integer, String> entry : highScoreBoard.entrySet()) {
                sb.append(entry.getKey())
                        .append(":")
                        .append(entry.getValue())
                        .append(",");
            }
            preferences.putString(HIGHSCORE_PREF_KEY, sb.toString());
            preferences.flush();
        } else {
            Gdx.app.log(TAG, " tried saving highScores but highScoreBoard is empty");
        }
        Gdx.app.log(TAG, " saved highScores successfully");
    }

    /***
     * Utility method to retrieve the lowest score on the highScoreBoard
     * @return int value of the lowest highScore entry
     */
    public int getLowestScore() {
        if (!highScoreBoard.isEmpty()) {
            return highScoreBoard.keySet().stream()
                    .skip((long) HIGHSCORE_ENTRIES - 1)
                    .findFirst().
                    orElse(0);
        }
        return 0;
    }

    /***
     * Utility method to retrieve the highest score on the highScoreBoard
     * @return int value of the highest highScore entry
     */
    public int getHighestScore() {
        if (!highScoreBoard.isEmpty()) {
            return highScoreBoard.lastKey();
        }
        return 0;
    }

    // Method to get player name by index
    public String getPlayerNameByIndex(int index) {
        if (index >= 0 && index < highScoreBoard.size()) {
            int count = 0;
            for (Integer score : highScoreBoard.keySet()) {
                if (count == index) {
                    return highScoreBoard.get(score);
                }
                count++;
            }
        }
        return "----"; // Placeholder if index is out of bounds
    }

    // Method to get score by index
    public int getHighscoreByIndex(int index) {
        if (index >= 0 && index < highScoreBoard.size()) {
            int count = 0;
            for (Integer score : highScoreBoard.keySet()) {
                if (count == index) {
                    return score;
                }
                count++;
            }
        }
        return 0; // Placeholder if index is out of bounds
    }
}
