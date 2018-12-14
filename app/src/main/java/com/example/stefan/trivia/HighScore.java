package com.example.stefan.trivia;

public class HighScore implements Comparable<HighScore>{
    private double score;
    private String timestamp;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(HighScore o) {
        if (this.score > o.score) return 1;
        if (this.score < o.score) return -1;
        return 0;
    }
}
