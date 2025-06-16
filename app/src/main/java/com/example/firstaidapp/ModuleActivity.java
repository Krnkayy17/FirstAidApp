package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.ModuleAdapter;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.models.Module;
import com.example.firstaidapp.utils.ModuleProgressUtil;
import com.example.firstaidapp.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class ModuleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ModuleAdapter moduleAdapter;
    private List<Module> moduleList;
    private ModuleDAO moduleDAO;
    private FirebaseAnalytics firebaseAnalytics;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        logScreenViewEvent();

        // SessionManager to get userId
        SessionManager sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        initViews();
        setupBottomNav();

        moduleDAO = new ModuleDAO(this);
        seedDefaultModulesIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadModulesWithUpdatedProgress();
    }

    private void initViews() {
        ImageView imgModuleBanner = findViewById(R.id.imgModuleBanner);
        imgModuleBanner.setImageResource(R.drawable.module_img);

        recyclerView = findViewById(R.id.recyclerViewModules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void seedDefaultModulesIfNeeded() {
        moduleList = moduleDAO.getAllModules();
        if (moduleList.isEmpty()) {
            moduleList = new ArrayList<>();
            moduleList.add(new Module(1, "CPR", "Learn CPR techniques", "Intermediate", 15, 10,
                    "Complete all assessments", "Not Accessed Yet", "Not Started", 0, false));
            moduleList.add(new Module(2, "Bleeding Management", "Control bleeding in emergencies", "Beginner", 10, 10,
                    "Pass all assessments", "Not Accessed Yet", "Not Started", 0, true));
            for (Module module : moduleList) {
                moduleDAO.addModule(module);
            }
        }
    }

    private void loadModulesWithUpdatedProgress() {
        moduleList = moduleDAO.getAllModules();

        for (int i = 0; i < moduleList.size(); i++) {
            Module module = moduleList.get(i);
            int moduleId = module.getModuleID();

            // Update progress
            int percentage = ModuleProgressUtil.calculateModuleProgress(this, userId, moduleId);
            module.setProgressPercentage(percentage);

            String status = (percentage == 100) ? "Completed" :
                    (percentage > 0) ? "In Progress" : "Not Started";
            module.setCompletionStatus(status);

            moduleDAO.updateProgressPercentage(moduleId, percentage);
            moduleDAO.updateCompletionStatus(moduleId, status);

            // Unlock logic
            if (i == 0) {
                module.setLocked(false); // CPR always unlocked
            } else {
                Module prevModule = moduleList.get(i - 1);
                int contentProgress = ModuleProgressUtil.calculateContentProgress(this, userId, prevModule.getModuleID());
                boolean quizPassed = ModuleProgressUtil.hasPassedAssessment(this, prevModule.getModuleID());
                boolean shouldUnlock = contentProgress >= 100 && quizPassed;

                module.setLocked(!shouldUnlock);

                Log.d("ModuleUnlock", "Module " + module.getModuleName() + " unlocked: " + shouldUnlock);

                if (shouldUnlock) {
                    logModuleUnlockedEvent(module.getModuleName());
                }
            }

            moduleDAO.updateLockStatus(moduleId, module.isLocked());
        }

        if (moduleAdapter == null) {
            moduleAdapter = new ModuleAdapter(this, moduleList);
            recyclerView.setAdapter(moduleAdapter);
        } else {
            moduleAdapter.setModules(moduleList);
            moduleAdapter.notifyDataSetChanged();
        }

        updateModuleCompletionSummary();
    }

    private void updateModuleCompletionSummary() {
        int completedCount = 0;
        int total = moduleList != null ? moduleList.size() : 0;

        if (moduleList != null) {
            for (Module m : moduleList) {
                if ("Completed".equalsIgnoreCase(m.getCompletionStatus())) completedCount++;
            }
        }

        TextView tvModuleSummary = findViewById(R.id.tvModuleSummary);
        tvModuleSummary.setText("Modules Completed: " + completedCount + " / " + total);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_modules);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (id == R.id.nav_modules) {
                return true;
            } else if (id == R.id.nav_assessments) {
                startActivity(new Intent(this, AssessmentActivity.class));
            } else if (id == R.id.nav_progress) {
                startActivity(new Intent(this, ProgressSummaryActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, UserProfileActivity.class));
            }
            return true;
        });
    }

    private void logScreenViewEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "ModuleActivity");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ModuleActivity");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    private void logModuleUnlockedEvent(String moduleTitle) {
        Bundle bundle = new Bundle();
        bundle.putString("module_unlocked", moduleTitle);
        firebaseAnalytics.logEvent("module_unlocked_event", bundle);
    }
}
