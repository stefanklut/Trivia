package com.example.stefan.trivia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Stack;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class QuestionsRequest implements Response.Listener<JSONObject>, Response.ErrorListener {
    Context context;
    Callback helper;

    public interface Callback {
        // Callback functions
        void gotQuestions(Stack<Question> questions);
        void gotQuestionsError(String message);
    }

    public QuestionsRequest (Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // If Volley gets an error return this to the helper
        helper.gotQuestionsError(error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        // Check if the JSONArray exists
        JSONArray jsonArray = null;
        try {
            jsonArray = response.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
            helper.gotQuestionsError(e.getMessage());
        }

        // If it does put the items from the JSONArray into a MenuItem Object in an ArrayList
        if (jsonArray != null) {
            Stack<Question> questions = new Stack<>();
            for (int i = 0; i<jsonArray.length(); i++) {
                try {
                    Question question = new Question();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    question.setCategory(jsonObject.getString("category"));
                    question.setType(jsonObject.getString("type"));
                    question.setDifficulty(jsonObject.getString("difficulty"));
                    question.setQuestion(jsonObject.getString("question"));
                    question.setCorrectAnswer(jsonObject.getString("correct_answer"));
                    ArrayList<String> incorrectAnswers = new ArrayList<>();
                    JSONArray jsonArrayIncorrectAnswers = jsonObject.getJSONArray("incorrect_answers");
                    for (int j =0; j<jsonArrayIncorrectAnswers.length(); j++) {
                        incorrectAnswers.add(jsonArrayIncorrectAnswers.getString(j));
                    }
                    question.setIncorrectAnswers(incorrectAnswers);
                    questions.add(question);
                } catch (JSONException e) {
                    e.printStackTrace();
                    helper.gotQuestionsError(e.getMessage());
                }
            }
            helper.gotQuestions(questions);
        }
    }

    void getQuestions(Callback helper, String questions, String difficulty, String type) {
        this.helper = helper;

        // Create Volley queue
        RequestQueue queue = Volley.newRequestQueue(context);

        String apiQuery = "https://opentdb.com/api.php";

        apiQuery += "?amount=" + questions;

        if (!difficulty.equals("random")) {
            apiQuery += "&difficuly=" + difficulty;
        }

        if (!type.equals("random")) {
            apiQuery += "&type=";
            Log.d("Trivia", "getQuestions: "+ type);
            if (type.equals("multiple choice")) {
                apiQuery += "multiple";
            }
            if (type.equals("true-false")) {
                apiQuery += "boolean";
            }
        }

        Log.d("Trivia", "getQuestions: " + apiQuery);

        // Create JsonObjectRequest with API url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                apiQuery,
                null, this, this);

        // Put request in the queue
        queue.add(jsonObjectRequest);
    }
}
