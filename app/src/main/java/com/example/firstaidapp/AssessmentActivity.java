package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.AssessmentAdapter;
import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.QuestionDAO;
import com.example.firstaidapp.models.Module;
import com.example.firstaidapp.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AssessmentActivity extends AppCompatActivity {

    private RecyclerView recyclerAssessment;
    private TextView tvPassInfo;

    private ModuleDAO moduleDAO;
    private QuestionDAO questionDAO;
    private AssessmentResultDAO resultDAO;
    private SessionManager sessionManager;
    private ImageView imgAssessmentBanner;

    private int userId;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        initViews();
        initData();
        showPassInfo();
        loadAssessmentModules();
        setupBottomNavigation();
    }

    private void initViews() {

        imgAssessmentBanner = findViewById(R.id.imgAssessmentBanner);
        imgAssessmentBanner.setImageResource(R.drawable.quiz_image);

        recyclerAssessment = findViewById(R.id.recyclerAssessments);
        recyclerAssessment.setLayoutManager(new LinearLayoutManager(this));
        tvPassInfo = findViewById(R.id.tvPassInfo);
    }

    private void initData() {
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();
        userType = sessionManager.getUserType();

        moduleDAO = new ModuleDAO(this);
        questionDAO = new QuestionDAO(this);
        resultDAO = new AssessmentResultDAO(this);
    }

    private void showPassInfo() {
        if ("volunteer".equalsIgnoreCase(userType)) {
            tvPassInfo.setText("📌 Volunteers must score 100% to pass each assessment.");
        } else {
            tvPassInfo.setText("📌 General public must score at least 80% to pass.");
        }
    }

    private void loadAssessmentModules() {
        List<Module> modules = moduleDAO.getAllModules();

        if (modules.isEmpty()) {
            Toast.makeText(this, "No modules found.", Toast.LENGTH_SHORT).show();
        } else {
            AssessmentAdapter adapter = new AssessmentAdapter(this, modules, questionDAO, resultDAO, userType, userId);
            recyclerAssessment.setAdapter(adapter);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_assessments);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.nav_modules) startActivity(new Intent(this, ModuleActivity.class));
            else if (id == R.id.nav_progress) startActivity(new Intent(this, ProgressSummaryActivity.class));
            else if (id == R.id.nav_profile) startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        });
    }
}
