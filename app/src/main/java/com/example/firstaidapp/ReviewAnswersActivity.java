package com.example.firstaidapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.helpers.QuizDataHolder;
import com.example.firstaidapp.models.Question;

import java.util.List;

public class ReviewAnswersActivity extends AppCompatActivity {

    private LinearLayout reviewContainer;
    private CheckBox checkShowWrongOnly;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_answers);

        reviewContainer = findViewById(R.id.reviewContainer);
        checkShowWrongOnly = findViewById(R.id.checkShowWrongOnly);

        questions = QuizDataHolder.getQuestions();

        checkShowWrongOnly.setOnCheckedChangeListener((buttonView, isChecked) -> displayAnswers());

        displayAnswers();
    }

    private void displayAnswers() {
        reviewContainer.removeAllViews();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            boolean isCorrect = isAnswerCorrect(q);

            // Apply filter if toggled
            if (checkShowWrongOnly.isChecked() && isCorrect) continue;

            // Question title with ✅ or ❌
            String symbol = isCorrect ? "✅" : "❌";
            addTextView(symbol + " " + (i + 1) + ". " + q.getQuestionText(), 18, Color.BLACK, 24, 8);

            if ("mcq".equalsIgnoreCase(q.getQuestionType())) {
                showMcqReview(q, isCorrect);
            } else if ("scenario".equalsIgnoreCase(q.getQuestionType())) {
                showScenarioReview(q, isCorrect);
            }

            // Explanation
            if (q.getExplanation() != null && !q.getExplanation().isEmpty()) {
                addTextView("Explanation: " + q.getExplanation(), 14, Color.DKGRAY, 8, 24);
            }

            addDivider();
        }
    }

    private void showMcqReview(Question q, boolean isCorrect) {
        addTextView("Your Answer: " + (q.getUserAnswer() != null ? q.getUserAnswer() : "No answer"),
                16, isCorrect ? Color.parseColor("#2E7D32") : Color.RED, 0, 4);

        addTextView("Correct Answer: " + q.getCorrectAnswer(),
                16, Color.parseColor("#2E7D32"), 0, 4);
    }

    private void showScenarioReview(Question q, boolean isCorrect) {
        addTextView("Your Sequence:\n" + (q.getUserSequence() != null ? formatSteps(q.getUserSequence()) : "No answer"),
                16, isCorrect ? Color.parseColor("#2E7D32") : Color.RED, 0, 4);

        addTextView("Correct Sequence:\n" + formatSteps(q.getCorrectSequence()),
                16, Color.parseColor("#2E7D32"), 0, 4);
    }

    private boolean isAnswerCorrect(Question q) {
        if ("mcq".equalsIgnoreCase(q.getQuestionType())) {
            return q.getUserAnswer() != null &&
                    q.getUserAnswer().equalsIgnoreCase(q.getCorrectAnswer());
        } else if ("scenario".equalsIgnoreCase(q.getQuestionType())) {
            return q.getUserSequence() != null &&
                    q.getUserSequence().equalsIgnoreCase(q.getCorrectSequence());
        }
        return false;
    }

    private void addTextView(String text, int textSize, int textColor, int topPadding, int bottomPadding) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(textSize);
        tv.setTextColor(textColor);
        tv.setPadding(0, topPadding, 0, bottomPadding);
        reviewContainer.addView(tv);
    }

    private void addDivider() {
        TextView divider = new TextView(this);
        divider.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 2
        ));
        divider.setBackgroundColor(Color.LTGRAY);
        reviewContainer.addView(divider);
    }

    private String formatSteps(String sequence) {
        if (sequence == null || sequence.trim().isEmpty()) return "No steps provided.";
        String[] steps = sequence.split(",");
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < steps.length; i++) {
            formatted.append((i + 1)).append(". ").append(steps[i].trim()).append("\n");
        }
        return formatted.toString().trim();
    }
}
