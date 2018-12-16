package com.example.stefan.trivia;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class PostRequestHelper implements Response.Listener, Response.ErrorListener {

    private double score;
    private String numberOfQuestions;
    private String difficulty;
    private String type;
    Context context;
    Callback callback;

    public interface Callback {
        void postedHighScore();
        void postError(String message);
    }

    public PostRequestHelper(Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // If Volley gets an error return this to the helper
        callback.postError(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        // Return that the post was successful
        callback.postedHighScore();
    }

    public class PostRequest extends StringRequest {

        // Constructor
        public PostRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        // Method to supply parameters to the request
        @Override
        protected Map<String, String> getParams() {

            Map<String, String> params = new HashMap<>();
            // Add score as parameter
            params.put("score", String.valueOf(score));

            // Format the way the timestamp should be displayed
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Add timestamp as parameter
            params.put("timestamp", simpleDateFormat.format(new Date()));
            return params;
        }
    }

    void postScore(Callback callback, double score, String numberOfQuestions, String difficulty, String type) {
        this.score = score;
        this.numberOfQuestions = numberOfQuestions;
        this.difficulty = difficulty;
        this.type = type;
        this.callback = callback;

        // Url where the high score will be added
        String url = "https://ide50-stefanklut.cs50.io:8080/";

        // Specify what type of high score is being posted
        String extendedUrl = url + numberOfQuestions + difficulty + type;

        // Create Volley queue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Create PostRequest with url
        PostRequest request = new PostRequest(Request.Method.POST, extendedUrl, this, this);

        // Put request in the queue
        queue.add(request);
    }
}
