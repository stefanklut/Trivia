package com.example.stefan.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity implements HighScoreHelper.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        Intent intent = getIntent();
        String numberOfQuestions = intent.getStringExtra("numberOfQuestions");
        String difficulty = intent.getStringExtra("difficulty");
        String type = intent.getStringExtra("type");

        TextView gameInfo = findViewById(R.id.textViewGameInfo);
        gameInfo.setText("Number of Questions: " + numberOfQuestions + "\n" +
                "Difficulty: " + difficulty + "\n" +
                "Game Type: " + type);

        HighScoreHelper highScoreHelper = new HighScoreHelper(this);
        highScoreHelper.getHighScore(this, numberOfQuestions, difficulty, type);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HighScoreActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void gotHighScore(ArrayList<HighScore> highScores) {
        ListView listView = findViewById(R.id.listView);

        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(this, R.layout.high_score_item, highScores);
        listView.setAdapter(highScoreAdapter);
    }

    @Override
    public void gotHighScoreError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
