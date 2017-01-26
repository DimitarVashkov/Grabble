package com.example.dimitarvashkov.grabble;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class InventoryFragment extends Fragment {
    private ArrayList<String> bucket;
    private GridView gridView;
    private LetterAdapter letterAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inventory, container, false);

        if (DataHolder.getInstance().getLetters() == null) {
            bucket = new ArrayList<>();
        } else {
            bucket = DataHolder.getInstance().getLetters();
        }

        //bucket = DataHolder.getInstance().getLetters();
        gridView = (GridView) rootView.findViewById(R.id.letterStorage);
        letterAdapter = new LetterAdapter(getActivity(), bucket);
        gridView.setAdapter(letterAdapter);


        final TextView lettersCollected = (TextView) rootView.findViewById(R.id.lettersCollected);
        lettersCollected.setText(Integer.toString(bucket.size()));


        Button dropper = (Button) rootView.findViewById(R.id.dropLetters);
        dropper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bucket = DataHolder.getInstance().getLetters();
                Log.d("Size", Integer.toString(bucket.size()));
                if (bucket.size() > 10) {
                    DataHolder.getInstance().removeALetter();
                    letterAdapter.notifyDataSetChanged();
                    lettersCollected.setText(Integer.toString(bucket.size()));
                } else {
                    Toast.makeText(getContext(), "You need at least 11 letters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;

    }

    @Override
    public void onStop() {
        super.onStop();


    }
}
