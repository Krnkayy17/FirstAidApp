package com.example.firstaidapp.helpers;

import com.example.firstaidapp.models.Question;
import com.example.firstaidapp.models.UserAnswer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizDataHolder {

    private static List<Question> questions;
    private static final Map<Integer, UserAnswer> userAnswers = new HashMap<>();

    // Store full user answer object
    public static void setUserAnswer(int index, String answer, boolean isCorrect, String correctAnswer, String explanation) {
        userAnswers.put(index, new UserAnswer(answer, isCorrect, correctAnswer, explanation));
    }

    public static UserAnswer getUserAnswer(int index) {
        return userAnswers.get(index);
    }

    public static Map<Integer, UserAnswer> getAllUserAnswers() {
        return userAnswers;
    }

    // Store and clear questions
    public static void setQuestions(List<Question> questionList) {
        questions = questionList;
        userAnswers.clear(); // Clear previous answers on new quiz
    }

    public static List<Question> getQuestions() {
        return questions;
    }

    // Clear everything after result screen
    public static void clear() {
        questions = null;
        userAnswers.clear();
    }

    public static void setUserAnswer(int currentQuestionIndex, String selectedTag) {
    }
}
