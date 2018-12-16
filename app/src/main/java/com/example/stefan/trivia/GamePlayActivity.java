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
        // Make buttons clickable
        findViewById(R.id.buttonFourth).setClickable(true);
        findViewById(R.id.buttonThird).setClickable(true);
        findViewById(R.id.buttonSecond).setClickable(true);
        findViewById(R.id.buttonFirst).setClickable(true);

        // Retrieve the next question
        nextQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        // Get intent add the game options that were selected
        Intent intent = getIntent();
        numberOfQuestions = intent.getStringExtra("number of questions");
        difficulty = intent.getStringExtra("difficulty");
        type = intent.getStringExtra("type");

        // Update the game info text view with the retrieved values
        TextView textViewGameInfo = findViewById(R.id.textViewGameInfo);
        textViewGameInfo.setText("Number of Questions: " + numberOfQuestions + "\n" +
                "Difficulty: " + difficulty + "\n" +
                "Game Type: " + type);

        // Update the score display
        updateScore();

        // Make the buttons not clickable
        findViewById(R.id.buttonFourth).setClickable(false);
        findViewById(R.id.buttonThird).setClickable(false);
        findViewById(R.id.buttonSecond).setClickable(false);
        findViewById(R.id.buttonFirst).setClickable(false);

        // Set the trivia helper that provides the questions
        triviaHelper = new TriviaHelper(this, this, numberOfQuestions, difficulty, type);
    }

    public void answerQuestion(View view) {
        // Retrieve the answer that was clicked
        String buttonText = ((Button) view).getText().toString();

        // Retrieve the correct answer
        String correctAnswer = Html.fromHtml(currentQuestion.getCorrectAnswer(), Html.FROM_HTML_MODE_LEGACY).toString();

        if (buttonText.equals(correctAnswer)) {
            // Add to score and update the score display
            score++;
            updateScore();
        }

        if (triviaHelper.gameFinished()) {
            // Make the buttons not clickable
            findViewById(R.id.buttonFourth).setClickable(false);
            findViewById(R.id.buttonThird).setClickable(false);
            findViewById(R.id.buttonSecond).setClickable(false);
            findViewById(R.id.buttonFirst).setClickable(false);

            // make the score a percentage
            score = score/Double.valueOf(numberOfQuestions);

            // Post the score using the PostRequestHelper
            PostRequestHelper postRequestHelper = new PostRequestHelper(this);
            postRequestHelper.postScore(this, score, numberOfQuestions, difficulty, type);
            return;
        }

        // Retrieve the next question
        nextQuestion();
    }

    private void nextQuestion() {
        // Get next question from the TriviaHelper
        currentQuestion = triviaHelper.getQuestion();

        // update the text views that are dependent on the question
        updateQuestionFields();
    }

    private void updateScore() {
        // Update the text view that displays the score
        TextView textViewScore = findViewById(R.id.textViewScore);
        textViewScore.setText("Current score: " + score + "/" + numberOfQuestions);
    }

    private void updateQuestionFields() {
        // Get the text views that are dependent on the question
        TextView textViewCategory = findViewById(R.id.textViewCategory);
        TextView textViewQuestion = findViewById(R.id.textViewQuestion);
        TextView textViewQuestionInfo = findViewById(R.id.textViewQuestionsInfo);
        TextView textViewDifficulty = findViewById(R.id.textViewDifficulty);

        // Get the clickable buttons for the answers
        Button buttonFirst = findViewById(R.id.buttonFirst);
        Button buttonSecond = findViewById(R.id.buttonSecond);
        Button buttonThird = findViewById(R.id.buttonThird);
        Button buttonFourth = findViewById(R.id.buttonFourth);

        // Update the text views with the info from the current question
        textViewQuestionInfo.setText("Questions left: " + triviaHelper.getQuestionsLeft());
        textViewDifficulty.setText("Question difficulty: " + currentQuestion.getDifficulty());
        textViewCategory.setText("Category: " + currentQuestion.getCategory());
        textViewQuestion.setText(Html.fromHtml(currentQuestion.getQuestion(), Html.FROM_HTML_MODE_LEGACY));

        // Changes the visible buttons depending on which type of question is being asked
        if (currentQuestion.getType().equals("boolean")) {
            // Hide the third and fourth button if the game type is true-false
            buttonThird.setVisibility(View.INVISIBLE);
            buttonFourth.setVisibility(View.INVISIBLE);

            // Set the First button to True and second button to False so the order is consistent
            buttonFirst.setText("True");
            buttonSecond.setText("False");
        } else {
            // Show the third and fourth buttons if the question is multiple choice
            buttonThird.setVisibility(View.VISIBLE);
            buttonFourth.setVisibility(View.VISIBLE);

            // Put all answers in a list
            ArrayList<String> answers = currentQuestion.getIncorrectAnswers();
            answers.add(currentQuestion.getCorrectAnswer());

            // Shuffle this list so the correct answer is always in a different place
            shuffle(answers);

            // Set the button text to the answers
            buttonFourth.setText(Html.fromHtml(answers.get(0), Html.FROM_HTML_MODE_LEGACY));
            buttonThird.setText(Html.fromHtml(answers.get(1), Html.FROM_HTML_MODE_LEGACY));
            buttonSecond.setText(Html.fromHtml(answers.get(2) ,Html.FROM_HTML_MODE_LEGACY));
            buttonFirst.setText(Html.fromHtml(answers.get(3), Html.FROM_HTML_MODE_LEGACY));
        }
    }

    @Override
    public void postedHighScore() {
        // Once the high score has been posted go to the high score activity
        Intent intent = new Intent(GamePlayActivity.this, HighScoreActivity.class);
        intent.putExtra("numberOfQuestions", numberOfQuestions);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void postError(String message) {
        // If the high score could not be posted, display the error why it was not possible
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
