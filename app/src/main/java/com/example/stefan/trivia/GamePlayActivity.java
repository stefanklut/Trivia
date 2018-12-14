package com.example.stefan.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.util.Collections.shuffle;

public class GamePlayActivity extends AppCompatActivity implements TriviaHelper.PassThrough, PostRequestHelper.Callback {

    TriviaHelper triviaHelper;
    Question currentQuestion;
    double score;
    String numberOfQuestions;
    String difficulty;
    String type;


    @Override
    public void questionsReady() {
        findViewById(R.id.buttonFourth).setClickable(true);
        findViewById(R.id.buttonThird).setClickable(true);
        findViewById(R.id.buttonSecond).setClickable(true);
        findViewById(R.id.buttonFirst).setClickable(true);

        nextQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        Intent intent = getIntent();
        numberOfQuestions = intent.getStringExtra("number of questions");
        difficulty = intent.getStringExtra("difficulty");
        type = intent.getStringExtra("type");

        TextView textViewGameInfo = findViewById(R.id.textViewGameInfo);
        textViewGameInfo.setText("Number of Questions: " + numberOfQuestions + "\n" +
                "Difficulty: " + difficulty + "\n" +
                "Game Type: " + type);
        updateScore();

        findViewById(R.id.buttonFourth).setClickable(false);
        findViewById(R.id.buttonThird).setClickable(false);
        findViewById(R.id.buttonSecond).setClickable(false);
        findViewById(R.id.buttonFirst).setClickable(false);

        triviaHelper = new TriviaHelper(this, this, numberOfQuestions, difficulty, type);
    }

    public void answerQuestion(View view) {
        String buttonText = ((Button) view).getText().toString();
        String correctAnswer = Html.fromHtml(currentQuestion.getCorrectAnswer(), Html.FROM_HTML_MODE_LEGACY).toString();
        if (buttonText.equals(correctAnswer)) {
            score++;
            updateScore();
        }

        if (triviaHelper.gameFinished()) {
            findViewById(R.id.buttonFourth).setClickable(false);
            findViewById(R.id.buttonThird).setClickable(false);
            findViewById(R.id.buttonSecond).setClickable(false);
            findViewById(R.id.buttonFirst).setClickable(false);

            score = score/Double.valueOf(numberOfQuestions);

            PostRequestHelper postRequestHelper = new PostRequestHelper(this);
            postRequestHelper.postScore(this, score, numberOfQuestions, difficulty, type);
            return;
        }
        nextQuestion();
    }

    private void nextQuestion() {
        currentQuestion = triviaHelper.getQuestion();
        updateQuestionFields();
    }

    private void updateScore() {
        TextView textViewScore = findViewById(R.id.textViewScore);
        textViewScore.setText("Current score: " + score + "/" + numberOfQuestions);
    }

    private void updateQuestionFields() {
        TextView textViewCategory = findViewById(R.id.textViewCategory);
        TextView textViewQuestion = findViewById(R.id.textViewQuestion);
        TextView textViewQuestionInfo = findViewById(R.id.textViewQuestionsInfo);

        Button buttonFirst = findViewById(R.id.buttonFirst);
        Button buttonSecond = findViewById(R.id.buttonSecond);
        Button buttonThird = findViewById(R.id.buttonThird);
        Button buttonFourth = findViewById(R.id.buttonFourth);


        textViewQuestionInfo.setText("Questions left: " + triviaHelper.getQuestionsLeft());
        textViewCategory.setText("Category: " + currentQuestion.getCategory());
        textViewQuestion.setText(Html.fromHtml(currentQuestion.getQuestion(), Html.FROM_HTML_MODE_LEGACY));


        if (currentQuestion.getType().equals("boolean")) {
            buttonThird.setVisibility(View.INVISIBLE);
            buttonFourth.setVisibility(View.INVISIBLE);

            buttonFirst.setText("True");
            buttonSecond.setText("False");
        } else {
            buttonThird.setVisibility(View.VISIBLE);
            buttonFourth.setVisibility(View.VISIBLE);
            ArrayList<String> answers = currentQuestion.getIncorrectAnswers();
            answers.add(currentQuestion.getCorrectAnswer());
            shuffle(answers);
            buttonFourth.setText(Html.fromHtml(answers.get(0), Html.FROM_HTML_MODE_LEGACY));
            buttonThird.setText(Html.fromHtml(answers.get(1), Html.FROM_HTML_MODE_LEGACY));
            buttonSecond.setText(Html.fromHtml(answers.get(2) ,Html.FROM_HTML_MODE_LEGACY));
            buttonFirst.setText(Html.fromHtml(answers.get(3), Html.FROM_HTML_MODE_LEGACY));
        }
    }

    @Override
    public void postedHighScore() {
        Intent intent = new Intent(GamePlayActivity.this, HighScoreActivity.class);
        intent.putExtra("numberOfQuestions", numberOfQuestions);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void postError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
