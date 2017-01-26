package com.example.dimitarvashkov.grabble;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPrefs = getSharedPreferences("Sup",0);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Letters", null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        ArrayList<String> bucket = gson.fromJson(json, type);

        DataHolder.getInstance().insertLetterSet(bucket);

        DataHolder.getInstance().createdWords(sharedPrefs.getInt("Words",0));

        DataHolder.getInstance().addToScore(sharedPrefs.getInt("Score",0));


        FloatingActionButton start = (FloatingActionButton) findViewById(R.id.start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();

    }
}