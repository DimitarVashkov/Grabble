package com.example.dimitarvashkov.grabble;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dimitarvashkov on 15/01/2017.
 */

public class Letter {
    private String mLetter;
    private int mValue;

    private static Map<String,Integer> values = createValuePairs();

    //private constructor, create Letters using a factory method

    private Letter(String mLetter, int mValue) {
        this.mLetter = mLetter;
        this.mValue = mValue;
    }

    public static Letter createLetter(String letter){
        int value = 0;
        for (Map.Entry<String, Integer> entry : values.entrySet()){
            if (letter.trim().equals(entry.getKey())){
                value = entry.getValue();
            }
        }

        return new Letter(letter,value);

    }

    public String getLetter() {
        return mLetter;
    }

    public int getValue() {
        return mValue;
    }

    private static Map<String,Integer> createValuePairs() {
        values = new HashMap<String, Integer>();
        values.put("A",3);
        values.put("B",20);
        values.put("C",13);
        values.put("D",10);
        values.put("E",1);
        values.put("F",15);
        values.put("G",18);
        values.put("H",9);
        values.put("I",5);
        values.put("J",25);
        values.put("K",22);
        values.put("L",11);
        values.put("M",14);
        values.put("N",6);
        values.put("O",4);
        values.put("P",19);
        values.put("Q",24);
        values.put("R",8);
        values.put("S",7);
        values.put("T",2);
        values.put("U",12);
        values.put("V",21);
        values.put("W",17);
        values.put("X",23);
        values.put("Y",16);
        values.put("Z",26);
        return values;
    }

}
