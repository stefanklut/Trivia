package com.example.stefan.trivia;

import android.content.Context;
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

        // Variables for the text views in the high score item
        TextView index = convertView.findViewById(R.id.textViewIndex);
        TextView time = convertView.findViewById(R.id.textViewTime);
        TextView score = convertView.findViewById(R.id.textViewScore);

        // Set value of the index to the position +1 (because arrays start at 0)
        index.setText(String.valueOf(position+1) + ".");

        // Get the high score at the position
        HighScore highScore = highScores.get(position);

        // Set the values of the text views to the values retrieve from the high score
        time.setText(highScore.getTimestamp());
        score.setText(String.format("%.0f%%",100*highScore.getScore()));

        return convertView;
    }
}
