package com.example.dimitarvashkov.grabble;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class LetterCombinerActivity extends AppCompatActivity {

    private InputStream input;
    private BufferedReader reader;
    private String line;
    private ArrayList<String> dictionary;
    private GridView gridView;
    private ArrayAdapter<String> storage;
    private LetterAdapter letterAdapter;
    private ArrayList<String> bucket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_combiner);

        //Get letters and show them in a gridView
        if (DataHolder.getInstance().getLetters() == null) {
            bucket = new ArrayList<>();
        } else {
            bucket = DataHolder.getInstance().getLetters();
        }


        gridView = (GridView) findViewById(R.id.letterStorage);
        letterAdapter = new LetterAdapter(this, bucket);
        gridView.setAdapter(letterAdapter);


        Button mapButton = (Button) findViewById(R.id.mapButton);
        createDictionary();
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LetterCombinerActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        final Button menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LetterCombinerActivity.this, MenuActivity.class);
                startActivity(i);
            }
        });

        final EditText wordEditText = (EditText) findViewById(R.id.wordCombiner);


        //Triggers word check
        final Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWord(wordEditText)) {
                    submitButton.setBackgroundColor(Color.GREEN);
                    wordEditText.getText().clear();
                } else {
                    submitButton.setBackgroundColor(Color.RED);
                    wordEditText.setTextColor(Color.RED);
                }

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Save information from DataHolder into SharedPreferences
        SharedPreferences sharedPrefs = getSharedPreferences("Sup", 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(DataHolder.getInstance().getLetters());

        editor.putString("Letters", json);
        editor.putInt("Words", DataHolder.getInstance().getWordsCreated());
        editor.putInt("Score", DataHolder.getInstance().getScore());

        editor.commit();


    }

    public boolean checkWord(EditText wordEditText) {
        String word = wordEditText.getText().toString().toUpperCase();
        ArrayList<String> letters = DataHolder.getInstance().getLetters();

        Toast.makeText(this, Integer.toString(letters.size()), Toast.LENGTH_SHORT).show();
        boolean hasLetters = true;

        //Checks if the user has all the letters in the typed word
        for (int i = 0; i < word.length(); i++) {
            if (!letters.contains(Character.toString(word.charAt(i)))) {
                hasLetters = false;
            }
        }

        //Shows the user why his word wasn't accepted
        if (!hasLetters) {
            Toast.makeText(this, "No letters", Toast.LENGTH_SHORT).show();
        }
        if (word.length() != 7) {
            Toast.makeText(this, "Not 7 letters long", Toast.LENGTH_SHORT).show();
        }
        if (!dictionary.contains(word)) {
            Toast.makeText(this, "Word not in dictionary", Toast.LENGTH_SHORT).show();

        }

        //Get values of the letters
        HashMap<String, Integer> values = DataHolder.getInstance().getValues();


        if (hasLetters && word.length() == 7 && dictionary.contains(word)) {
            for (int i = 0; i < word.length(); i++) {
                String letter = Character.toString(word.charAt(i));

                //Count letter value
                if (values.containsKey(letter)) {
                    int number = values.get(letter);
                    DataHolder.getInstance().addToScore(number);
                }
                //Remove the letters used
                DataHolder.getInstance().removeLetters(letter);

            }

            //Increase the number of created words in DataHolder
            DataHolder.getInstance().incrementCreatedWords(1);
            Toast.makeText(this, Integer.toString(DataHolder.getInstance().getScore()), Toast.LENGTH_SHORT).show();
            return true;
        }


        return false;
    }

    //Take information from grabble.txt and store in ArrayList
    public ArrayList<String> createDictionary() {
        dictionary = new ArrayList<>();
        input = getResources().openRawResource(R.raw.grabble);
        reader = new BufferedReader(new InputStreamReader(input));
        try {
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                dictionary.add(fileLine.toUpperCase());
            }
            reader.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;

    }


}
