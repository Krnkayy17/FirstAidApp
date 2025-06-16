package com.example.firstaidapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.models.Module;
import com.example.firstaidapp.utils.SessionManager;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class QuizResultActivity extends AppCompatActivity {

    private TextView tvScore, tvPercentage, tvFeedback, tvModuleTitle, tvTimeTaken;
    private Button btnBackToHome, btnRetake, btnReviewAnswers;
    private KonfettiView konfettiView;

    private int score, totalQuestions, moduleId;
    private String moduleName;
    private long timeTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // Bind views
        tvScore = findViewById(R.id.tv_score);
        tvPercentage = findViewById(R.id.tv_percentage);
        tvFeedback = findViewById(R.id.tv_feedback);
        tvModuleTitle = findViewById(R.id.tv_module_title);
        tvTimeTaken = findViewById(R.id.tv_time_taken);
        btnBackToHome = findViewById(R.id.btn_back_home);
        btnRetake = findViewById(R.id.btn_retake);
        btnReviewAnswers = findViewById(R.id.btn_review_answers);
        konfettiView = findViewById(R.id.view_confetti);

        // Get data from Intent
        score = getIntent().getIntExtra("SCORE", 0);
        totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        moduleName = getIntent().getStringExtra("MODULE_NAME");
        timeTaken = getIntent().getLongExtra("TIME_TAKEN_MS", 0);

        // Display score
        tvScore.setText("Score: " + score + "/" + totalQuestions);

        // Display module title
        if (moduleName != null) {
            tvModuleTitle.setText("Results for: " + moduleName);
        }

        // Display time taken
        if (timeTaken > 0) {
            String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                    TimeUnit.MILLISECONDS.toSeconds(timeTaken) % 60);
            tvTimeTaken.setText("Time Taken: " + timeFormatted);
        }

        // Calculate percentage
        double percentage = (score * 100.0) / totalQuestions;
        tvPercentage.setText(String.format("Percentage: %.2f%%", percentage));

        // Get user type and determine threshold
        SessionManager sessionManager = new SessionManager(this);
        String userType = sessionManager.getUserType();
        int threshold = userType.equalsIgnoreCase("volunteer") ? 100 : 80;

        // Show feedback
        if (percentage >= threshold) {
            tvFeedback.setText("🎉 Great job! You passed as a " + userType + "!");
        } else if (percentage >= 50) {
            tvFeedback.setText("⚠️ Almost there! You need " + threshold + "% to pass.");
        } else {
            tvFeedback.setText("❌ Keep trying! You need " + threshold + "% as a " + userType + ".");
        }

        // 🎊 Confetti & dialog if 100%
        if (percentage == 100.0) {
            triggerConfetti();
            showAchievementDialog();
        }

        // Button listeners
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnRetake.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("MODULE_ID", moduleId);
            startActivity(intent);
            finish();
        });

        btnReviewAnswers.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewAnswersActivity.class);
            intent.putExtra("MODULE_ID", moduleId);
            startActivity(intent);
        });
    }

    private void triggerConfetti() {
        konfettiView.setVisibility(View.VISIBLE);

        EmitterConfig emitterConfig = new Emitter(300, TimeUnit.MILLISECONDS).max(100);

        Party party = new PartyFactory(emitterConfig)
                .spread(360)
                .shapes(Shape.Circle.INSTANCE, Shape.Square.INSTANCE)
                .colors(Arrays.asList(Color.YELLOW, Color.GREEN, Color.MAGENTA))  // ✅ Fixed here
                .position(new Position.Relative(0.5, 0.0))
                .build();

        konfettiView.start(party);
    }


    private void showAchievementDialog() {
        new AlertDialog.Builder(this)
                .setTitle("🏅 Achievement Unlocked!")
                .setMessage("You scored 100%! You're a First Aid Pro!")
                .setPositiveButton("Awesome!", null)
                .show();
    }

    @SuppressWarnings("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ModuleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
