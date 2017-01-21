package com.example.dimitarvashkov.grabble;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


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

        bucket = DataHolder.getInstance().getLetters();
         gridView = (GridView)findViewById(R.id.letterStorage);
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

        //TODO add a reader for the Grabble dictionary
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


    public boolean checkWord(EditText wordEditText){
        String word = wordEditText.getText().toString();
        if (word.length() == 7 && dictionary.contains(word)){
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
                dictionary.add(fileLine);
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
