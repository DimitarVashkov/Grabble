package com.example.dimitarvashkov.grabble;

import java.util.ArrayList;

/**
 * Created by dimitarvashkov on 21/01/2017.
 */

public class DataHolder {
    private ArrayList<String> letters = new ArrayList<>();

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public void addLetter(String letter){
        letters.add(letter);
    }

    public void removeLetter(String... bunchOfLetters){
        for( String i : bunchOfLetters){
            letters.remove(i);
        }
    }

    public ArrayList<String> getLetters(){
        return letters;
    }
}
