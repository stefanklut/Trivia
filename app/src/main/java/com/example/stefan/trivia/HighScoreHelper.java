package com.example.stefan.trivia;


import android.content.Context;
import android.util.Log;

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
        ArrayList<HighScore> highScores = new ArrayList<>();
        for (int i = 0; i<response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                HighScore highScore = new HighScore();
                highScore.setScore(Double.valueOf(jsonObject.getString("score")));
                highScore.setTimestamp(jsonObject.getString("timestamp"));
                highScores.add(highScore);
            } catch (JSONException e) {
                e.printStackTrace();
                helper.gotHighScoreError(e.getMessage());
            }
        }
        sort(highScores);
        reverse(highScores);
        helper.gotHighScore(highScores);
    }

    void getHighScore(Callback helper, String numberOfQuestions, String difficulty, String type) {
        this.helper = helper;

        // Create Volley queue
        RequestQueue queue = Volley.newRequestQueue(context);

        String apiQuery = "https://ide50-stefanklut.cs50.io:8080/" + numberOfQuestions + difficulty + type;

        Log.d("Trivia", "getHighScore: " + apiQuery);

        // Create JsonObjectRequest with API url
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiQuery, null, this, this);

        // Put request in the queue
        queue.add(jsonArrayRequest);
    }
}
