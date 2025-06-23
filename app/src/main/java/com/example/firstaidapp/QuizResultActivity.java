package com.example.firstaidapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.VideoRecommendationAdapter;
import com.example.firstaidapp.database.VideoClickLogDAO;
import com.example.firstaidapp.database.VideoRecommendationDAO;
import com.example.firstaidapp.models.VideoClickLog;
import com.example.firstaidapp.models.VideoRecommendation;
import com.example.firstaidapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private RecyclerView recyclerRecommendations;

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
        recyclerRecommendations = findViewById(R.id.rv_recommendations);

        // Get data from Intent
        score = getIntent().getIntExtra("SCORE", 0);
        totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        moduleName = getIntent().getStringExtra("MODULE_NAME");
        timeTaken = getIntent().getLongExtra("TIME_TAKEN_MS", 0);

        // Display score and module info
        tvScore.setText("Score: " + score + "/" + totalQuestions);
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

        // Calculate and show percentage
        double percentage = totalQuestions > 0 ? (score * 100.0) / totalQuestions : 0;
        tvPercentage.setText(String.format("Percentage: %.2f%%", percentage));

        // Feedback based on user type and threshold
        SessionManager sessionManager = new SessionManager(this);
        String userType = sessionManager.getUserType();
        int threshold = userType.equalsIgnoreCase("volunteer") ? 100 : 80;

        if (percentage >= threshold) {
            tvFeedback.setText("🎉 Great job! You passed as a " + userType + "!");
        } else if (percentage >= 50) {
            tvFeedback.setText("⚠️ Almost there! You need " + threshold + "% to pass.");
        } else {
            tvFeedback.setText("❌ Keep trying! You need " + threshold + "% as a " + userType + ".");
        }

        // 🎊 Confetti for 100%
        if (percentage == 100.0) {
            triggerConfetti();
            showAchievementDialog();
        }

        // Load video recommendations
        loadRecommendations(moduleId);

        // Button listeners
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModuleActivity.class);
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

    private void loadRecommendations(int moduleId) {
        VideoRecommendationDAO dao = new VideoRecommendationDAO(this);
        List<String> topTags = dao.getMostWatchedTags(moduleId, 2);
        List<VideoRecommendation> allVideos = dao.getRecommendationsByModule(moduleId);
        List<VideoRecommendation> sorted = new ArrayList<>();

        for (String tag : topTags) {
            for (VideoRecommendation video : allVideos) {
                if (tag.equalsIgnoreCase(video.getTag()) && !sorted.contains(video)) {
                    sorted.add(video);
                }
            }
        }
        for (VideoRecommendation video : allVideos) {
            if (!sorted.contains(video)) {
                sorted.add(video);
            }
        }
        List<VideoRecommendation> videos = sorted.subList(0, Math.min(3, sorted.size()));


        Log.d("QuizResult", "Module ID: " + moduleId);
        Log.d("QuizResult", "Recommended videos found: " + videos.size());

        if (videos != null && !videos.isEmpty()) {
            recyclerRecommendations.setLayoutManager(new LinearLayoutManager(this));
            recyclerRecommendations.setAdapter(new VideoRecommendationAdapter(videos, this, video -> {
                if (moduleName != null) {
                    new com.example.firstaidapp.analytics.FirebaseAnalyticsTracker(this)
                            .logVideoClicked(moduleName, video.getYoutubeVideoId(), video.getTitle());
                }

                String timestamp = String.valueOf(System.currentTimeMillis());

                VideoClickLog log = new VideoClickLog(
                        video.getModuleId(),
                        video.getTitle(),
                        video.getYoutubeVideoId(),
                        timestamp,
                        video.getTag()
                );
                new VideoClickLogDAO(this).insertLog(log);


                String url = "https://www.youtube.com/watch?v=" + video.getYoutubeVideoId();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }));
            recyclerRecommendations.setVisibility(View.VISIBLE);
        } else {
            Log.w("QuizResult", "No video recommendations available.");
            recyclerRecommendations.setVisibility(View.GONE);
        }
    }

    private void triggerConfetti() {
        konfettiView.setVisibility(View.VISIBLE);

        EmitterConfig emitterConfig = new Emitter(300, TimeUnit.MILLISECONDS).max(100);
        Party party = new PartyFactory(emitterConfig)
                .spread(360)
                .shapes(Shape.Circle.INSTANCE, Shape.Square.INSTANCE)
                .colors(Arrays.asList(Color.YELLOW, Color.GREEN, Color.MAGENTA))
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
