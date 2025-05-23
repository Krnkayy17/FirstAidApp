package com.example.firstaidapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.R;
import com.example.firstaidapp.database.QuestionDAO;
import com.example.firstaidapp.models.Question;

import java.util.List;

public class QuizMcqActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private TextView tvQuestionText, tvQuestionNumber;
    private ImageView imgQuestion;
    private RadioGroup rgOptions;
    private Button btnSubmit;

    private Question currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mcq);

        // UI Hooks
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestionText = findViewById(R.id.tv_question_text);
        imgQuestion = findViewById(R.id.img_question);
        rgOptions = findViewById(R.id.rg_options);
        btnSubmit = findViewById(R.id.btn_submit);

        int moduleId = getIntent().getIntExtra("MODULE_ID", 1); // default to 1 if not passed
        QuestionDAO questionDAO = new QuestionDAO(this);
        questions = questionDAO.getQuestionsByModuleId(moduleId);

        loadNextQuestion();

        btnSubmit.setOnClickListener(view -> checkAnswer());
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            Toast.makeText(this, "Quiz complete!", Toast.LENGTH_SHORT).show();
            finish(); // Or move to result screen
            return;
        }

        currentQuestion = questions.get(currentQuestionIndex);
        if (!currentQuestion.getQuestionType().equals("mcq")) {
            currentQuestionIndex++;
            loadNextQuestion(); // Skip non-MCQ
            return;
        }

        // Display question
        String questionText = getString(R.string.quiz_question_format, currentQuestionIndex + 1, questions.size());
        tvQuestionNumber.setText(questionText);

        rgOptions.removeAllViews();
        addOption(currentQuestion.getOptionA(), "A");
        addOption(currentQuestion.getOptionB(), "B");
        addOption(currentQuestion.getOptionC(), "C");
        addOption(currentQuestion.getOptionD(), "D");

        // Load image
        if (currentQuestion.getQuestionImage() != null && !currentQuestion.getQuestionImage().isEmpty()) {
            imgQuestion.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(getImageResourceId(currentQuestion.getQuestionImage()))
                    .placeholder(R.drawable.image_placeholder)
                    .into(imgQuestion);
        } else {
            imgQuestion.setVisibility(View.GONE);
        }
    }

    private void addOption(String text, String tag) {
        RadioButton rb = new RadioButton(this);
        rb.setText(text);
        rb.setTag(tag);
        rgOptions.addView(rb);
    }

    private void checkAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selected = findViewById(selectedId);
        String selectedTag = (String) selected.getTag();

        if (selectedTag.equals(currentQuestion.getCorrectAnswer())) {
            Toast.makeText(this, "✅ Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "❌ Incorrect. Correct answer: " + currentQuestion.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
        loadNextQuestion();
    }

    private int getImageResourceId(String imageName) {
        return getResources().getIdentifier(imageName.replace(".png", ""), "drawable", getPackageName());
    }
}
