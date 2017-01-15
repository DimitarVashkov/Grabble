package com.example.dimitarvashkov.grabble;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dimitarvashkov on 15/01/2017.
 */

public class Bucket extends Application {
    private ArrayList<Letter> bucketOfLetters = new ArrayList<>();

    public void addToBucket(Letter l){
        bucketOfLetters.add(l);
    }
    public void removeFromBucket(Letter l){
        bucketOfLetters.remove(l);
    }

    public void displayBucket(){
        for (Letter l : bucketOfLetters){
            Log.d("Letter:", l.getLetter());
        }
    }

}
