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
        callback.postError(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
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
            params.put("score", String.valueOf(score));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

        String url = "https://ide50-stefanklut.cs50.io:8080/" + numberOfQuestions + difficulty + type;
        RequestQueue queue = Volley.newRequestQueue(context);
        PostRequest request = new PostRequest(Request.Method.POST, url, this, this);
        queue.add(request);
    }
}
