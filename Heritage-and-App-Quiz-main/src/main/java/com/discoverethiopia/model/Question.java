package com.discoverethiopia.model;

import java.util.Map;

public class Question {
    private int questionId;
    private int siteId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private char correctOption;
    private String explanation;

    public Question(int questionId, int siteId, String questionText, String optionA, String optionB,
            String optionC, String optionD, char correctOption, String explanation) {
        this.questionId = questionId;
        this.siteId = siteId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        setCorrectOption(correctOption);
        this.explanation = explanation;
    }

    public boolean isCorrect(char answer) {
        return Character.toUpperCase(answer) == correctOption;
    }

    public String getAnswerText(char option) {
        return getOptions().get(Character.toUpperCase(option));
    }

    public String getCorrectAnswerText() {
        return getAnswerText(correctOption);
    }

    public Map<Character, String> getOptions() {
        return Map.of(
                'A', optionA,
                'B', optionB,
                'C', optionC,
                'D', optionD);
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public char getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(char correctOption) {
        char normalized = Character.toUpperCase(correctOption);
        if ("ABCD".indexOf(normalized) < 0) {
            throw new IllegalArgumentException("Correct option must be A, B, C, or D.");
        }
        this.correctOption = normalized;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}

