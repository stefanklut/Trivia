package com.example.stefan.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HighScoreActivity extends AppCompatActivity implements HighScoreHelper.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Get intent add the game options that were selected
        Intent intent = getIntent();
        String numberOfQuestions = intent.getStringExtra("numberOfQuestions");
        String difficulty = intent.getStringExtra("difficulty");
        String type = intent.getStringExtra("type");

        // Update the game info text view with the retrieved values
        TextView gameInfo = findViewById(R.id.textViewGameInfo);
        gameInfo.setText("Number of Questions: " + numberOfQuestions + "\n" +
                "Difficulty: " + difficulty + "\n" +
                "Game Type: " + type);

        // Ask to retrieve the high scores
        HighScoreHelper highScoreHelper = new HighScoreHelper(this);
        highScoreHelper.getHighScore(this, numberOfQuestions, difficulty, type);
    }

    public void newTrivia(View view) {
        // Start new trivia by going back to the MenuActivity
        Intent intent = new Intent(HighScoreActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Pressing the back button will take you to the MenuActivity and remove all open activities
        Intent intent = new Intent(HighScoreActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void gotHighScore(ArrayList<HighScore> highScores) {
        // The list view variable
        ListView listView = findViewById(R.id.listView);

        // Add an adapter to the list view
        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(this, R.layout.high_score_item, highScores);
        listView.setAdapter(highScoreAdapter);
    }

    @Override
    public void gotHighScoreError(String message) {
        // If the high scores could not be retrieved, display an error message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
