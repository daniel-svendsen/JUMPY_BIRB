package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.*;

/***
 * This class is responsible for managing the highscore top 10 list and saving
 * it locally so that it's available when you run the game again after closing it.
 * The list should always be sorted in DESC order of score.
 * Check if score from game session (play) should enter the top 10 list.
 * Player name from GameOverScreen should be saved with its score to the top 10 list.
 */
public class HighscoreManager {
    private static final int MAX_HIGHSCORES = 10;
    private HashMap<Integer, String> highscores;

    public HighscoreManager() {
        highscores = new HashMap<>();
    }

    public HashMap<Integer, String> getHighscores() {
        return highscores;
    }

    public void addHighscore(int score) {
        String name = getPlayerName();

        highscores.put(score, name);
        highscores = sortByKey(highscores);

        if (highscores.size() > MAX_HIGHSCORES) {
            Iterator<Map.Entry<Integer, String>> iterator = highscores.entrySet().iterator();
            int count = 0;
            while (iterator.hasNext() && count < MAX_HIGHSCORES) {
                iterator.next();
                count++;
            }
            iterator.remove();
        }
    }

    // Utility method to sort HashMap by keys
    private HashMap<Integer, String> sortByKey(HashMap<Integer, String> hm) {
        LinkedHashMap<Integer, String> sortedMap = new LinkedHashMap<>();

        hm.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));

        return sortedMap;
    }

    // Method to get player name using LibGDX input
    public static String getPlayerName() {
        StringBuilder name = new StringBuilder();
        boolean enterPressed = false;
        while (!enterPressed) {
            for (int i = 29; i >= 0; i--) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.A + i)) {
                    name.append((char)('A' + i));
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && name.length() > 0) {
                name.deleteCharAt(name.length() - 1);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && name.length() > 0) {
                enterPressed = true;
            }
        }
        return name.toString();
    }

    // Method to get player name by index
    public String getPlayerNameByIndex(int index) {
        if (index >= 0 && index < highscores.size()) {
            int count = 0;
            for (Integer score : highscores.keySet()) {
                if (count == index) {
                    return highscores.get(score);
                }
                count++;
            }
        }
        return "----"; // Placeholder if index is out of bounds
    }

    // Method to get score by index
    public int getHighscoreByIndex(int index) {
        if (index >= 0 && index < highscores.size()) {
            int count = 0;
            for (Integer score : highscores.keySet()) {
                if (count == index) {
                    return score;
                }
                count++;
            }
        }
        return 0; // Placeholder if index is out of bounds
    }
}
