package com.example.dimitarvashkov.grabble;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dimitarvashkov on 21/01/2017.
 */

public class DataHolder {
    private static final DataHolder holder = new DataHolder();
    private ArrayList<String> letters;
    private int score;
    private int wordsCreated;
    private boolean vibrate = false;
    private HashMap<String, Integer> values = new HashMap<>();

    public static DataHolder getInstance() {
        return holder;
    }

    public void addLetter(String letter) {
        if (letters == null) {
            letters = new ArrayList<>();
        }

        letters.add(letter);
    }

    public void removeLetter(String... bunchOfLetters) {
        for (String i : bunchOfLetters) {
            letters.remove(i);
        }
    }

    public HashMap<String, Integer> getValues() {
        values.put("A", 3);
        values.put("B", 20);
        values.put("C", 13);
        values.put("D", 10);
        values.put("E", 1);
        values.put("F", 15);
        values.put("G", 18);
        values.put("H", 9);
        values.put("I", 5);
        values.put("J", 25);
        values.put("K", 22);
        values.put("L", 11);
        values.put("M", 14);
        values.put("N", 6);
        values.put("O", 4);
        values.put("P", 19);
        values.put("Q", 24);
        values.put("R", 8);
        values.put("S", 7);
        values.put("T", 2);
        values.put("U", 12);
        values.put("V", 21);
        values.put("W", 17);
        values.put("X", 23);
        values.put("Y", 16);
        values.put("Z", 26);

        return values;
    }

    public ArrayList<String> getLetters() {
        return letters;
    }

    public int getScore() {
        return score;
    }

    public void addToScore(int i) {
        score += i;
    }

    public void createdWords(int i) {
        wordsCreated += i;
    }

    public int getWordsCreated() {
        return wordsCreated;
    }

    public void insertLetterSet(ArrayList<String> lettersFromPreviousSession) {
        letters = lettersFromPreviousSession;
    }

    public boolean getVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean b) {
        vibrate = b;
    }

    public void removeALetter() {
        if (letters.size() > 10) {
            letters.remove(0);
        }
    }

}
