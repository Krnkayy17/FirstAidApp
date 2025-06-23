package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.ProgressModuleAdapter;
import com.example.firstaidapp.analytics.FirebaseAnalyticsTracker;
import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.database.ContentDAO;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.UserContentViewDAO;
import com.example.firstaidapp.models.AssessmentResult;
import com.example.firstaidapp.models.Module;
import com.example.firstaidapp.utils.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.*;

public class ProgressSummaryActivity extends AppCompatActivity {

    private PieChart pieChart;
    private TextView tvBreakdown;
    private Spinner filterSpinner;
    private RecyclerView recyclerModules;

    private List<Module> allModules = new ArrayList<>();
    private ModuleDAO moduleDAO;
    private ContentDAO contentDAO;
    private UserContentViewDAO userContentViewDAO;
    private AssessmentResultDAO resultDAO;
    private SessionManager sessionManager;

    private int userId;
    private String userType;
    private int threshold;

    private FirebaseAnalyticsTracker analyticsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_summary);

        analyticsTracker = new FirebaseAnalyticsTracker(this);
        analyticsTracker.logProgressSummaryViewed();


        // UI References
        pieChart = findViewById(R.id.pieChart);
        tvBreakdown = findViewById(R.id.tvBreakdown);
        filterSpinner = findViewById(R.id.filterSpinner);
        recyclerModules = findViewById(R.id.recyclerModules);
        recyclerModules.setLayoutManager(new LinearLayoutManager(this));

        // DAO & Session setup
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();
        userType = sessionManager.getUserType();
        threshold = userType.equals("volunteer") ? 100 : 80;

        moduleDAO = new ModuleDAO(this);
        contentDAO = new ContentDAO(this);
        userContentViewDAO = new UserContentViewDAO(this);
        resultDAO = new AssessmentResultDAO(this);

        allModules = moduleDAO.getAllModules();

        renderPieChart();
        setupFilter();
        setupBottomNavigation();
    }

    private void renderPieChart() {
        int completed = 0, inProgress = 0, notStarted = 0;

        for (Module module : allModules) {
            int contentProgress = getModuleContentProgress(module);
            if (isModuleCompleted(module)) {
                completed++;
            } else if (contentProgress > 0) {
                inProgress++;
            } else {
                notStarted++;
            }
        }

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        if (completed > 0) {
            entries.add(new PieEntry(completed, "Completed"));
            colors.add(ContextCompat.getColor(this, R.color.completed_color));
        }

        if (inProgress > 0) {
            entries.add(new PieEntry(inProgress, "In Progress"));
            colors.add(ContextCompat.getColor(this, R.color.in_progress_color));
        }

        if (notStarted > 0) {
            entries.add(new PieEntry(notStarted, "Not Started"));
            colors.add(ContextCompat.getColor(this, R.color.not_started_color));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        Description desc = new Description();
        desc.setText("Module Completion Status");
        pieChart.setDescription(desc);
        pieChart.animateY(1000);
        pieChart.invalidate();

        tvBreakdown.setText("Completed: " + completed +
                "\nIn Progress: " + inProgress +
                "\nNot Started: " + notStarted);

        analyticsTracker.logProgressBreakdown(completed, inProgress, notStarted);

    }

    private void setupFilter() {
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = parent.getItemAtPosition(pos).toString();
                List<Module> filtered = new ArrayList<>();

                for (Module module : allModules) {
                    int contentProgress = getModuleContentProgress(module);
                    boolean completed = isModuleCompleted(module);

                    if (selected.equals("All")
                            || (selected.equals("Completed") && completed)
                            || (selected.equals("In Progress") && contentProgress > 0 && !completed)
                            || (selected.equals("Not Started") && contentProgress == 0)) {
                        filtered.add(module);
                    }
                }

                recyclerModules.setAdapter(new ProgressModuleAdapter(ProgressSummaryActivity.this, filtered));
                analyticsTracker.logProgressFilterSelected(selected);

            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        filterSpinner.setSelection(0); // Default to "All"
    }

    private int getModuleContentProgress(Module module) {
        int totalContent = contentDAO.getContentByModule(module.getModuleID()).size();
        int viewedContent = userContentViewDAO.getViewedCountForModule(userId, module.getModuleID());
        if (totalContent > 0) {
            return (int) ((viewedContent / (float) totalContent) * 100);
        }
        return 0;
    }

    private boolean isModuleCompleted(Module module) {
        int contentProgress = getModuleContentProgress(module);
        if (contentProgress < 100) return false;

        AssessmentResult result = resultDAO.getLatestResult(userId, module.getModuleID());
        if (result == null || result.getTotalQuestions() == 0) return false;

        int scorePercent = (int) ((result.getScore() / (float) result.getTotalQuestions()) * 100);
        return scorePercent >= threshold;
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_progress);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) startActivity(new Intent(this, HomeActivity.class));
            else if (id == R.id.nav_modules) startActivity(new Intent(this, ModuleActivity.class));
            else if (id == R.id.nav_assessments) startActivity(new Intent(this, AssessmentActivity.class));
            else if (id == R.id.nav_progress) return true;
            else if (id == R.id.nav_profile) startActivity(new Intent(this, UserProfileActivity.class));
            return true;
        });
    }
}
