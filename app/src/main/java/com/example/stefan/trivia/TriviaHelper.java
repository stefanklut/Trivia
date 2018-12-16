package com.example.stefan.trivia;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Stack;

public class TriviaHelper implements Serializable, QuestionsRequest.Callback {
    private Stack<Question> questions;
    Context context;
    PassThrough activity;

    public interface PassThrough {
        void questionsReady();
    }

    public TriviaHelper(Context context, PassThrough activity, String numberOfQuestions, String difficulty, String type) {
        this.context = context;
        this.activity = activity;

        // Make a request for the questions
        QuestionsRequest questionsRequest = new QuestionsRequest(context);
        questionsRequest.getQuestions(this, numberOfQuestions.toLowerCase(),
                difficulty.toLowerCase(), type.toLowerCase());
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
        // Once the questions are retrieved add the stack
        this.questions = questions;
        activity.questionsReady();
    }

    @Override
    public void gotQuestionsError(String message) {
        // If the questions could not be retrieved, display an error message
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
