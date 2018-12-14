package com.example.stefan.trivia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScoreAdapter extends ArrayAdapter {
    ArrayList<HighScore> highScores;

    public HighScoreAdapter(Context context, int resource, ArrayList objects) {
        super(context, resource, objects);
        highScores = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.high_score_item, parent, false);
        }

        TextView index = convertView.findViewById(R.id.textViewIndex);
        TextView time = convertView.findViewById(R.id.textViewTime);
        TextView score = convertView.findViewById(R.id.textViewScore);

        HighScore highScore = highScores.get(position);
        index.setText(String.valueOf(position+1) + ".");
        time.setText(highScore.getTimestamp());
        score.setText(String.format("%.0f%%",100*highScore.getScore()));

        return convertView;
    }
}
