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

        //Get the letters and show them in a gridView
        if (DataHolder.getInstance().getLetters() == null) {
            bucket = new ArrayList<>();
        } else {
            bucket = DataHolder.getInstance().getLetters();
        }

        gridView = (GridView) rootView.findViewById(R.id.letterStorage);
        letterAdapter = new LetterAdapter(getActivity(), bucket);
        gridView.setAdapter(letterAdapter);


        //Show number of letters collected
        final TextView lettersCollected = (TextView) rootView.findViewById(R.id.lettersCollected);
        lettersCollected.setText(Integer.toString(bucket.size()));


        //Remove a letter; Only possible after having more than 10 letters
        Button remove1Letter = (Button) rootView.findViewById(R.id.remove1Letter);
        remove1Letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bucket = DataHolder.getInstance().getLetters();
                Log.d("Size", Integer.toString(bucket.size()));
                if (bucket.size() > 10) {
                    DataHolder.getInstance().removeALetter();
                    //Update information
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
