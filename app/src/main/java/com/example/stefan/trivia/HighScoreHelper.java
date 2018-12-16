package com.example.stefan.trivia;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static java.util.Collections.reverse;
import static java.util.Collections.sort;

public class HighScoreHelper implements Response.Listener<JSONArray>, Response.ErrorListener {
    Context context;
    Callback helper;

    public interface Callback {
        // Callback functions
        void gotHighScore(ArrayList<HighScore> highScores);
        void gotHighScoreError(String message);
    }

    public HighScoreHelper (Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // If Volley gets an error return this to the helper
        helper.gotHighScoreError(error.getMessage());
    }

    @Override
    public void onResponse(JSONArray response) {
        // Array list for the high scores
        ArrayList<HighScore> highScores = new ArrayList<>();
        for (int i = 0; i<response.length(); i++) {
            try {
                // Get the json object from the json array
                JSONObject jsonObject = response.getJSONObject(i);

                // Add the values from the json object to a high score object
                HighScore highScore = new HighScore();
                highScore.setScore(Double.valueOf(jsonObject.getString("score")));
                highScore.setTimestamp(jsonObject.getString("timestamp"));

                // Add high score object to array list
                highScores.add(highScore);
            } catch (JSONException e) {
                e.printStackTrace();
                helper.gotHighScoreError(e.getMessage());
            }
        }

        // Sort the high scores so that the list goes from highest score to lowest
        sort(highScores);
        reverse(highScores);

        helper.gotHighScore(highScores);
    }

    void getHighScore(Callback helper, String numberOfQuestions, String difficulty, String type) {
        this.helper = helper;

        // Create Volley queue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Url from which the high scores are retrieved
        String url = "https://ide50-stefanklut.cs50.io:8080/";

        // Add what type of high scores need to be retrieved
        String apiQuery = url + numberOfQuestions + difficulty + type;

        // Create JsonObjectRequest with API url
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiQuery,
                null, this, this);

        // Put request in the queue
        queue.add(jsonArrayRequest);
    }
}
