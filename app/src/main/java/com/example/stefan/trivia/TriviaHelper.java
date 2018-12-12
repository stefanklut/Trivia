package com.example.stefan.trivia;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Stack;

public class TriviaHelper implements Serializable, QuestionsRequest.Callback {
    private Stack<Question> questions;
    Context context;
    PassThrough activity;
    String numberOfQuestions;
    String difficulty;
    String type;

    public interface PassThrough {
        void questionsReady();
    }

    public TriviaHelper(Context context, PassThrough activity, String numberOfQuestions, String difficulty, String type) {
        this.context = context;
        this.activity = activity;
        QuestionsRequest questionsRequest = new QuestionsRequest(context);
        questionsRequest.getQuestions(this, numberOfQuestions.toLowerCase(),
                difficulty.toLowerCase(), type.toLowerCase());

        this.numberOfQuestions = numberOfQuestions;
        this.difficulty = difficulty;
        this.type = type;
    }

    public String getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getType() {
        return type;
    }

    public Question getQuestion() {
         return questions.pop();
    }

    public int getQuestionsLeft() {
        return questions.size();
    }

    public boolean gameFinished() {
        return questions.empty();
    }

    @Override
    public void gotQuestions(Stack<Question> questions) {
        this.questions = questions;
        activity.questionsReady();
    }

    @Override
    public void gotQuestionsError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
