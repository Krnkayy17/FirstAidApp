package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.ProgressModuleAdapter;
import com.example.firstaidapp.database.ContentDAO;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.UserContentViewDAO;
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
    private SessionManager sessionManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_summary);

        // UI References
        pieChart = findViewById(R.id.pieChart);
        tvBreakdown = findViewById(R.id.tvBreakdown);
        filterSpinner = findViewById(R.id.filterSpinner);
        recyclerModules = findViewById(R.id.recyclerModules);
        recyclerModules.setLayoutManager(new LinearLayoutManager(this));

        // DAO Initialization
        moduleDAO = new ModuleDAO(this);
        contentDAO = new ContentDAO(this);
        userContentViewDAO = new UserContentViewDAO(this);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        // Load and visualize module data
        allModules = moduleDAO.getAllModules();
        renderPieChart();
        setupFilter();

        setupBottomNavigation();
    }

    private void renderPieChart() {
        int completed = 0, inProgress = 0, notStarted = 0;

        for (Module module : allModules) {
            int progress = getModuleProgress(module);
            if (progress >= 100) completed++;
            else if (progress > 0) inProgress++;
            else notStarted++;
        }

        List<PieEntry> entries = new ArrayList<>();
        if (completed > 0) entries.add(new PieEntry(completed, "Completed"));
        if (inProgress > 0) entries.add(new PieEntry(inProgress, "In Progress"));
        if (notStarted > 0) entries.add(new PieEntry(notStarted, "Not Started"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(
                getResources().getColor(R.color.completed_color),
                getResources().getColor(R.color.in_progress_color),
                getResources().getColor(R.color.not_started_color)
        );

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
    }

    private void setupFilter() {
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = parent.getItemAtPosition(pos).toString();
                List<Module> filtered = new ArrayList<>();

                for (Module module : allModules) {
                    int progress = getModuleProgress(module);
                    if (selected.equals("All")
                            || (selected.equals("Completed") && progress >= 100)
                            || (selected.equals("In Progress") && progress > 0 && progress < 100)
                            || (selected.equals("Not Started") && progress == 0)) {
                        filtered.add(module);
                    }
                }

                recyclerModules.setAdapter(new ProgressModuleAdapter(ProgressSummaryActivity.this, filtered));
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        filterSpinner.setSelection(0); // Default to "All"
    }

    private int getModuleProgress(Module module) {
        int totalContent = contentDAO.getContentByModule(module.getModuleID()).size();
        int viewedContent = userContentViewDAO.getViewedCountForModule(userId, module.getModuleID());

        if (totalContent > 0) {
            return (int) ((viewedContent / (float) totalContent) * 100);
        }
        return 0;
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
