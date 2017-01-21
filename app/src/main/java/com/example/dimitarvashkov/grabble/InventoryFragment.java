package com.example.dimitarvashkov.grabble;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


public class InventoryFragment extends Fragment {
    private ArrayList<String> bucket;
    private GridView gridView;
    private LetterAdapter letterAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inventory, container, false);

        bucket = DataHolder.getInstance().getLetters();
        gridView = (GridView) rootView.findViewById(R.id.letterStorage);
        letterAdapter = new LetterAdapter(getActivity(), bucket);
        gridView.setAdapter(letterAdapter);
        return rootView;
    }
}
