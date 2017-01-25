package com.example.dimitarvashkov.grabble;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class InventoryFragment extends Fragment {
    private ArrayList<String> bucket;
    private GridView gridView;
    private LetterAdapter letterAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inventory, container, false);

        if(DataHolder.getInstance().getLetters() == null){
            bucket = new ArrayList<>();
        }else {
            bucket = DataHolder.getInstance().getLetters();
        }

        //bucket = DataHolder.getInstance().getLetters();
        gridView = (GridView) rootView.findViewById(R.id.letterStorage);
        letterAdapter = new LetterAdapter(getActivity(), bucket);
        gridView.setAdapter(letterAdapter);


        TextView lettersCollected = (TextView) rootView.findViewById(R.id.lettersCollected);
        lettersCollected.setText(Integer.toString(bucket.size()));

        return rootView;

    }

    @Override
    public void onStop() {
        super.onStop();
        
    }
}
