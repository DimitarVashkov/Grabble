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

        TextView powerUser = (TextView) rootView.findViewById(R.id.powerUser);

        //Level up the user after 1000 points; The user can't revert back to normal user.
        if (DataHolder.getInstance().getScore() > 1000) {
            DataHolder.getInstance().setPowerUser();
            powerUser.setText("You're a power user! Search radius increased!");
        } else {
            powerUser.setText("Nope! Score 1000 points!");
        }

        return rootView;
    }
}
