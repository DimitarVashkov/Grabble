package com.example.dimitarvashkov.grabble;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dimitarvashkov on 21/01/2017.
 */

public class LetterAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<String> letters;

    public LetterAdapter(Context mContext, ArrayList<String> letters) {
        this.mContext = mContext;
        this.letters = letters;
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView dummyTextView = new TextView(mContext);
        dummyTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22.f);
        dummyTextView.setTypeface(null, Typeface.BOLD);
        dummyTextView.setTextColor(0xAA765f49);
        dummyTextView.setText(letters.get(i));
        return dummyTextView;

    }
}
