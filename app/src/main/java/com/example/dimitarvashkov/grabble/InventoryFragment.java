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

        SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences("Sup",0);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Letters", null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        bucket = gson.fromJson(json, type);
        if(bucket == null){
            bucket = DataHolder.getInstance().getLetters();
        }

        //bucket = DataHolder.getInstance().getLetters();
        gridView = (GridView) rootView.findViewById(R.id.letterStorage);
        letterAdapter = new LetterAdapter(getActivity(), bucket);
        gridView.setAdapter(letterAdapter);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences("Sup",0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(DataHolder.getInstance().getLetters());

        editor.putString("Letters", json);
        editor.commit();

    }
}
