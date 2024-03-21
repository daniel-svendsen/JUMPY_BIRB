package se.yrgo.jumpybirb.utils;

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

    public void addHighscore(String name, int score) {
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
}
