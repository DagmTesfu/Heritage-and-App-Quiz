package com.discoverethiopia.model;

public class WrongAnswer {
    private int wrongId;
    private int attemptId;
    private String questionText;
    private String userAnswer;
    private String correctAnswer;
    private String explanation;

    public WrongAnswer(int wrongId, int attemptId, String questionText, String userAnswer,
            String correctAnswer, String explanation) {
        this.wrongId = wrongId;
        this.attemptId = attemptId;
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public int getWrongId() {
        return wrongId;
    }

    public void setWrongId(int wrongId) {
        this.wrongId = wrongId;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}

