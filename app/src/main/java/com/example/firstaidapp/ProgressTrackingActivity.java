package com.example.firstaidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.UserDAO;
import com.example.firstaidapp.models.AssessmentResult;
import com.example.firstaidapp.models.Module;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class ProgressTrackingActivity extends AppCompatActivity {

    private LinearLayout layoutContainer;
    private FirebaseAnalytics firebaseAnalytics;

    private int userId = 1; // Replace with actual logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracking);

        layoutContainer = findViewById(R.id.layoutContainer);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        loadProgress();
    }

    private void loadProgress() {
        ModuleDAO moduleDAO = new ModuleDAO(this);
        AssessmentResultDAO resultDAO = new AssessmentResultDAO(this);
        UserDAO userDAO = new UserDAO(this);

        List<Module> modules = moduleDAO.getAllModules();
        String userType = userDAO.getUserType(userId);

        for (Module module : modules) {
            View item = getLayoutInflater().inflate(R.layout.progress_item, layoutContainer, false);

            TextView title = item.findViewById(R.id.tvProgressModuleTitle);
            TextView status = item.findViewById(R.id.tvProgressStatus);
            TextView score = item.findViewById(R.id.tvProgressScore);
            ProgressBar progressBar = item.findViewById(R.id.progressTrackingBar);
            ImageView badge = item.findViewById(R.id.imgProgressBadge);

            title.setText(module.getModuleName());
            badge.setVisibility(View.GONE); // default

            AssessmentResult result = resultDAO.getResult(userId, module.getModuleID());
            int progress = 0;
            String statusText = "Not Started";

            if (result != null && result.getTotalQuestions() > 0) {
                progress = (int) ((result.getScore() / (float) result.getTotalQuestions()) * 100);
                statusText = progress >= (userType.equals("volunteer") ? 100 : 80) ? "Completed" : "In Progress";
                score.setText("Score: " + progress + "%");

                if (progress == 100) {
                    badge.setVisibility(View.VISIBLE);
                }

                // 🔥 Log event to Firebase
                Bundle bundle = new Bundle();
                bundle.putString("module_name", module.getModuleName());
                bundle.putString("user_type", userType);
                bundle.putDouble("score_percent", progress);
                bundle.putString("status", statusText);
                firebaseAnalytics.logEvent("progress_tracked", bundle);
            } else {
                score.setText("No attempt yet");
            }

            status.setText("Status: " + statusText);
            progressBar.setProgress(progress);
            layoutContainer.addView(item);
        }
    }
}
