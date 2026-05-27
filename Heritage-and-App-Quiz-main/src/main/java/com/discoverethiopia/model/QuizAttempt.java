package com.discoverethiopia.model;

import java.time.LocalDateTime;

public class QuizAttempt {
    private int attemptId;
    private int userId;
    private int siteId;
    private int score;
    private int totalQuestions;
    private int percentage;
    private LocalDateTime takenAt;

    public QuizAttempt(int attemptId, int userId, int siteId, int score, int totalQuestions,
            int percentage, LocalDateTime takenAt) {
        this.attemptId = attemptId;
        this.userId = userId;
        this.siteId = siteId;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.takenAt = takenAt;
    }

    public String getMessage() {
        if (percentage >= 80) {
            return "You understood the heritage. Great!";
        }
        if (percentage >= 50) {
            return "Good progress. Review the missed answers.";
        }
        return "Keep learning and try the quiz again.";
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }
}

