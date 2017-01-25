package com.example.dimitarvashkov.grabble;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context

           SharedPreferences sharedPrefs = getSharedPreferences("Sup",0);
           Gson gson = new Gson();
           String json = sharedPrefs.getString("Letters", null);
           Type type = new TypeToken<ArrayList<String>>(){}.getType();

            bucket = gson.fromJson(json, type);
            if(bucket == null){
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


        final Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkWord(wordEditText)){
                    submitButton.setBackgroundColor(Color.GREEN);
                    wordEditText.getText().clear();
                }
                else{
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

        SharedPreferences sharedPrefs = getSharedPreferences("Sup",0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(DataHolder.getInstance().getLetters());

        editor.putString("Letters", json);
        editor.commit();
    }

    public boolean checkWord(EditText wordEditText){
        String word = wordEditText.getText().toString().toUpperCase();
        ArrayList<String> letters =  DataHolder.getInstance().getLetters();

        Toast.makeText(this,Integer.toString(letters.size()),Toast.LENGTH_SHORT).show();
        boolean hasLetters = true;

        for(int i=0; i<word.length();i++){
            if(!letters.contains(Character.toString(word.charAt(i)))){
                hasLetters = false;
            }
        }

        if(!hasLetters){
            Toast.makeText(this,"No letters",Toast.LENGTH_SHORT).show();
        }
        if(word.length() != 7){
            Toast.makeText(this,"Not 7 letters long",Toast.LENGTH_SHORT).show();
        }
        if(!dictionary.contains(word)){
            Toast.makeText(this,"Word not in dictionary",Toast.LENGTH_SHORT).show();

        }

        //TODO count letter score
        HashMap<String,Integer> values = DataHolder.getInstance().getValues();


        if (hasLetters && word.length() == 7 && dictionary.contains(word)){
            for(int i=0; i<word.length();i++){
                String letter = Character.toString(word.charAt(i));

                if (values.containsKey(letter)){
                    int number = values.get(letter);
                    DataHolder.getInstance().addToScore(number);
                }

                DataHolder.getInstance().removeLetter(letter);
            }



            Toast.makeText(this,Integer.toString(DataHolder.getInstance().getScore()),Toast.LENGTH_SHORT).show();
            return true;
        }


        return false;
    }

    //TODO createDictionary in the background
    public ArrayList<String> createDictionary(){
        dictionary = new ArrayList<>();
        input = getResources().openRawResource(R.raw.grabble);
        reader = new BufferedReader(new InputStreamReader(input));
        try {
            String fileLine;
            while((fileLine =reader.readLine()) != null){
                dictionary.add(fileLine.toUpperCase());
            }

            reader.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("Dictionary",Integer.toString(dictionary.size()));
        //Log.d("Dictionary",dictionary.get(1));
        //Log.d("Dictionary",dictionary.get(1000));
        //Log.d("Dictionary",dictionary.get(15000));
        return dictionary;

    }



}
