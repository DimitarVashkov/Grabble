package com.example.dimitarvashkov.grabble;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);

        TextView points = (TextView) rootView.findViewById(R.id.totalPoints);
        points.setText(Integer.toString(DataHolder.getInstance().getScore()));

        TextView wordsCreated = (TextView) rootView.findViewById(R.id.wordsCreated);
        wordsCreated.setText(Integer.toString(DataHolder.getInstance().getWordsCreated()));


        return rootView;
    }
}
