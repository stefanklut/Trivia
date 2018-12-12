package com.example.stefan.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static java.util.Collections.shuffle;

public class GamePlayActivity extends AppCompatActivity implements TriviaHelper.PassThrough {

    TriviaHelper triviaHelper;
    Question currentQuestion;
    int score;

    @Override
    public void questionsReady() {
        nextQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        Intent intent = getIntent();
        String numberOfQuestions = intent.getStringExtra("number of questions");
        String difficulty = intent.getStringExtra("difficulty");
        String type = intent.getStringExtra("type");

        triviaHelper = new TriviaHelper(this, this, numberOfQuestions, difficulty, type);
    }

    public void answerQuestion(View view) {
        String buttonText = ((Button) view).getText().toString();
        String correctAnswer = Html.fromHtml(currentQuestion.getCorrectAnswer(), Html.FROM_HTML_MODE_LEGACY).toString();
        if (buttonText.equals(correctAnswer)) {
            score++;
        }

        if (triviaHelper.gameFinished()) {
            Intent intent = new Intent(GamePlayActivity.this, HighScoreActivity.class);
            startActivity(intent);
            return;
        }
        nextQuestion();
    }

    private void nextQuestion() {
        currentQuestion = triviaHelper.getQuestion();
        updateViews();
    }

    private void updateViews() {
        TextView textViewCategory = findViewById(R.id.textViewCategory);
        TextView textViewQuestion = findViewById(R.id.textViewQuestion);
        TextView textViewQuestionInfo = findViewById(R.id.textViewQuestionsInfo);
        TextView textViewScore = findViewById(R.id.textViewScore);
        TextView textViewGameInfo = findViewById(R.id.textViewGameInfo);

        Button buttonTop = findViewById(R.id.buttonTop);
        Button buttonBottom = findViewById(R.id.buttonBottom);
        Button buttonRight = findViewById(R.id.buttonRight);
        Button buttonLeft = findViewById(R.id.buttonLeft);

        textViewGameInfo.setText("Number of Questions: " + triviaHelper.getNumberOfQuestions()+ "\n" +
                "Difficulty: " + triviaHelper.getDifficulty() + "\n" +
                "Game Type: " + triviaHelper.getType());
        textViewQuestionInfo.setText("Questions left: " + triviaHelper.getQuestionsLeft());
        textViewCategory.setText("Category: " + currentQuestion.getCategory());
        textViewQuestion.setText(Html.fromHtml(currentQuestion.getQuestion(), Html.FROM_HTML_MODE_LEGACY));
        textViewScore.setText("Current score: " + score);

        if (currentQuestion.getType().equals("boolean")) {
            buttonBottom.setVisibility(View.INVISIBLE);
            buttonTop.setVisibility(View.INVISIBLE);
            buttonLeft.setText("True");
            buttonRight.setText("False");
        } else {
            buttonBottom.setVisibility(View.VISIBLE);
            buttonTop.setVisibility(View.VISIBLE);
            ArrayList<String> answers = currentQuestion.getIncorrectAnswers();
            answers.add(currentQuestion.getCorrectAnswer());
            shuffle(answers);
            buttonTop.setText(Html.fromHtml(answers.get(0), Html.FROM_HTML_MODE_LEGACY));
            buttonBottom.setText(Html.fromHtml(answers.get(1), Html.FROM_HTML_MODE_LEGACY));
            buttonLeft.setText(Html.fromHtml(answers.get(2) ,Html.FROM_HTML_MODE_LEGACY));
            buttonRight.setText(Html.fromHtml(answers.get(3), Html.FROM_HTML_MODE_LEGACY));
        }
    }
}
