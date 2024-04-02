package se.yrgo.jumpybirb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.*;

public class HighscoreManager {
    private static final int MAX_HIGHSCORES = 10;
    private HashMap<String, Integer> highscores;

    public HighscoreManager() {
        highscores = new HashMap<>();
    }

    public HashMap<String, Integer> getHighscores() {
        return highscores;
    }

    public void addHighscore(int score) {
        String name = getPlayerName();

        highscores.put(name, score);
        highscores = sortByValue(highscores);

        if (highscores.size() > MAX_HIGHSCORES) {
            Iterator<Map.Entry<String, Integer>> iterator = highscores.entrySet().iterator();
            int count = 0;
            while (iterator.hasNext() && count < MAX_HIGHSCORES) {
                iterator.next();
                count++;
            }
            iterator.remove();
        }
    }

    // Utility method to sort HashMap by values
    private HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());

        // Sorting the list based on values
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedHashMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
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
            for (String playerName : highscores.keySet()) {
                if (count == index) {
                    return playerName;
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
            for (Integer score : highscores.values()) {
                if (count == index) {
                    return score;
                }
                count++;
            }
        }
        return 0; // Placeholder if index is out of bounds
    }
}
