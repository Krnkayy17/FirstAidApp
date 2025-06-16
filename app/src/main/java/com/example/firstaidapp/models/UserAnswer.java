package com.example.firstaidapp.models;

public class UserAnswer {
    private String userAnswer;
    private boolean isCorrect;
    private String correctAnswer;
    private String explanation;

    public UserAnswer(String userAnswer, boolean isCorrect, String correctAnswer, String explanation) {
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
