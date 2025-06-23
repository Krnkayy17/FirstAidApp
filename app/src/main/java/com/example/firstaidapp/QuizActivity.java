package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.adapters.ScenarioStepAdapter;
import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.QuestionDAO;
import com.example.firstaidapp.helpers.QuizDataHolder;
import com.example.firstaidapp.helpers.ScenarioItemTouchHelper;
import com.example.firstaidapp.models.AssessmentResult;
import com.example.firstaidapp.models.Question;
import com.example.firstaidapp.utils.SessionManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.*;

public class QuizActivity extends AppCompatActivity {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private FirebaseAnalytics firebaseAnalytics;

    // Views
    private TextView tvQuestionNumber;
    private LinearLayout layoutMcq, layoutScenario;
    private TextView tvMcqQuestion, tvScenarioTitle, tvScenarioInstruction;
    private ImageView imgMcq, imgScenario;
    private RadioGroup rgOptions;
    private RecyclerView recyclerSteps;
    private ScenarioStepAdapter stepAdapter;
    private Button btnNextSubmit, btnPrevious;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        int moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        if (moduleId == -1) {
            Toast.makeText(this, "Module ID missing.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle startBundle = new Bundle();
        startBundle.putInt("module_id", moduleId);
        startBundle.putString("event_type", "quiz_started");
        firebaseAnalytics.logEvent("quiz_event", startBundle);

        questions = new QuestionDAO(this).getQuestionsByModuleId(moduleId);
        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "No questions found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindViews();
        loadCurrentQuestion();
    }

    private void bindViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        layoutMcq = findViewById(R.id.layout_mcq);
        layoutScenario = findViewById(R.id.layout_scenario);
        tvMcqQuestion = findViewById(R.id.tv_mcq_question_text);
        tvScenarioTitle = findViewById(R.id.tv_scenario_title);
        tvScenarioInstruction = findViewById(R.id.tv_scenario_instruction);
        imgMcq = findViewById(R.id.img_mcq_question);
        imgScenario = findViewById(R.id.img_scenario);
        rgOptions = findViewById(R.id.rg_mcq_options);
        recyclerSteps = findViewById(R.id.recycler_scenario_steps);
        recyclerSteps.setLayoutManager(new LinearLayoutManager(this));
        btnNextSubmit = findViewById(R.id.btn_next_submit);
        btnPrevious = findViewById(R.id.btn_previous_question);

        btnNextSubmit.setOnClickListener(v -> {
            checkAnswer();
            if (currentQuestionIndex == questions.size() - 1) {
                saveAssessmentResult();
                Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
                intent.putExtra("SCORE", score);
                intent.putExtra("TOTAL_QUESTIONS", questions.size());
                intent.putExtra("MODULE_ID", questions.get(0).getModuleId());
                QuizDataHolder.setQuestions(questions);
                startActivity(intent);
                finish();
            } else {
                currentQuestionIndex++;
                loadCurrentQuestion();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                loadCurrentQuestion();
            }
        });
    }

    private void loadCurrentQuestion() {
        Question q = questions.get(currentQuestionIndex);
        tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());

        if ("mcq".equalsIgnoreCase(q.getQuestionType())) {
            showMcqQuestion(q);
        } else {
            showScenarioQuestion(q);
        }

        btnPrevious.setEnabled(currentQuestionIndex > 0);
        btnNextSubmit.setText(currentQuestionIndex == questions.size() - 1 ? "Submit" : "Next");
    }

    private void showMcqQuestion(Question q) {
        layoutMcq.setVisibility(View.VISIBLE);
        layoutScenario.setVisibility(View.GONE);

        tvMcqQuestion.setText(q.getQuestionText());
        rgOptions.removeAllViews();

        addOption(q.getOptionA(), "A");
        addOption(q.getOptionB(), "B");
        addOption(q.getOptionC(), "C");
        addOption(q.getOptionD(), "D");

        if (q.getQuestionImage() != null && !q.getQuestionImage().isEmpty()) {
            imgMcq.setVisibility(View.VISIBLE);
            Glide.with(this).load(getImageResourceId(q.getQuestionImage())).into(imgMcq);
        } else {
            imgMcq.setVisibility(View.GONE);
        }
    }

    private void addOption(String text, String tag) {
        if (text != null && !text.trim().isEmpty()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(text);
            rb.setTag(tag);
            rgOptions.addView(rb);
        }
    }

    private void showScenarioQuestion(Question q) {
        layoutMcq.setVisibility(View.GONE);
        layoutScenario.setVisibility(View.VISIBLE);

        tvScenarioTitle.setText(q.getQuestionText());
        tvScenarioInstruction.setText("Arrange the steps in the correct order");

        if (q.getQuestionImage() != null && !q.getQuestionImage().isEmpty()) {
            imgScenario.setVisibility(View.VISIBLE);
            Glide.with(this).load(getImageResourceId(q.getQuestionImage())).into(imgScenario);
        } else {
            imgScenario.setVisibility(View.GONE);
        }

        List<String> steps = new ArrayList<>(Arrays.asList(q.getCorrectSequence().split(",")));
        Collections.shuffle(steps);

        stepAdapter = new ScenarioStepAdapter(steps, viewHolder -> itemTouchHelper.startDrag(viewHolder));
        recyclerSteps.setAdapter(stepAdapter);

        ItemTouchHelper.Callback callback = new ScenarioItemTouchHelper(stepAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerSteps);
    }

    private void checkAnswer() {
        if (questions == null || questions.isEmpty() || currentQuestionIndex >= questions.size()) return;

        Question q = questions.get(currentQuestionIndex);

        if ("mcq".equalsIgnoreCase(q.getQuestionType())) {
            int selectedId = rgOptions.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selected = findViewById(selectedId);
            if (selected == null || selected.getTag() == null) {
                Toast.makeText(this, "Invalid selection. Please try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedTag = selected.getTag().toString();
            boolean isCorrect = selectedTag.equalsIgnoreCase(q.getCorrectAnswer());

            if (isCorrect) score++;

            q.setUserAnswer(selectedTag);

            QuizDataHolder.setUserAnswer(
                    currentQuestionIndex,
                    selectedTag,
                    isCorrect,
                    q.getCorrectAnswer(),
                    q.getExplanation()
            );

        } else if ("scenario".equalsIgnoreCase(q.getQuestionType())) {
            if (stepAdapter == null) return;

            List<String> currentSteps = stepAdapter.getCurrentSteps();
            String userSequence = String.join(",", currentSteps);
            q.setUserSequence(userSequence);

            QuizDataHolder.setUserAnswer(currentQuestionIndex, userSequence);

            String[] correct = q.getCorrectSequence().split(",");
            boolean isCorrect = currentSteps.size() == correct.length;

            for (int i = 0; i < correct.length && isCorrect; i++) {
                if (!currentSteps.get(i).trim().equalsIgnoreCase(correct[i].trim())) {
                    isCorrect = false;
                }
            }

            if (isCorrect) score++;
        }
    }

    private void saveAssessmentResult() {
        int moduleId = questions.get(0).getModuleId();

        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        String userType = sessionManager.getUserType();

        AssessmentResultDAO resultDAO = new AssessmentResultDAO(this);
        ModuleDAO moduleDAO = new ModuleDAO(this);

        int totalQuestions = questions.size();
        float percentage = (score / (float) totalQuestions) * 100f;
        int threshold = userType.equals("volunteer") ? 100 : 80;

        AssessmentResult result = new AssessmentResult();
        result.setUserId(userId);
        result.setModuleId(moduleId);
        result.setScore(score);
        result.setTotalQuestions(totalQuestions);
        result.setDateTaken(new Date());

        // ✅ Set retake count based on last attempt (if any)
        AssessmentResult existing = resultDAO.getLatestResult(userId, moduleId); // ← renamed version of getResult()
        int retakeCount = (existing != null ? existing.getRetakeCount() + 1 : 0);
        result.setRetakeCount(retakeCount);

        // ✅ Always insert a new result
        resultDAO.insertResult(result);

        // Update progress and completion status
        moduleDAO.updateProgressPercentage(moduleId, (int) percentage);
        if (percentage >= threshold) {
            moduleDAO.updateCompletionStatus(moduleId, "Completed");
        } else {
            moduleDAO.updateCompletionStatus(moduleId, "In Progress");
        }

        Toast.makeText(this,
                "You scored " + (int) percentage + "%. (" + userType + " requirement: " + threshold + "%)\nStatus: " +
                        (percentage >= threshold ? "✅ Completed!" : "🕓 In Progress."),
                Toast.LENGTH_LONG).show();

        // Firebase Analytics log
        Bundle resultBundle = new Bundle();
        resultBundle.putInt("module_id", moduleId);
        resultBundle.putInt("score", score);
        resultBundle.putInt("total_questions", totalQuestions);
        resultBundle.putFloat("percentage", percentage);
        resultBundle.putString("status", (percentage >= threshold) ? "completed" : "in_progress");
        resultBundle.putString("user_type", userType);
        resultBundle.putInt("retake_count", retakeCount);
        resultBundle.putString("date_taken", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        firebaseAnalytics.logEvent("quiz_completed", resultBundle);
    }

    private int getImageResourceId(String name) {
        return getResources().getIdentifier(name.replace(".png", ""), "drawable", getPackageName());
    }
}
