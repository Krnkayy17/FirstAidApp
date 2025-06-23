package com.example.firstaidapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.AttemptHistoryAdapter;
import com.example.firstaidapp.analytics.FirebaseAnalyticsTracker;
import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.models.AssessmentResult;
import com.example.firstaidapp.utils.SessionManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class ModuleProgressDetailActivity extends AppCompatActivity {

    private LineChart lineChart;
    private int moduleId;
    private String moduleName;
    private FirebaseAnalyticsTracker analyticsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_progress_detail);

        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        moduleName = getIntent().getStringExtra("MODULE_NAME");

        analyticsTracker = new FirebaseAnalyticsTracker(this);
        analyticsTracker.logModuleProgressDetailViewed(moduleName);

        lineChart = findViewById(R.id.lineChart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(moduleName + " Progress");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        loadProgressData();
    }

    private void loadProgressData() {
        AssessmentResultDAO dao = new AssessmentResultDAO(this);
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        String userType = sessionManager.getUserType();
        List<AssessmentResult> results = dao.getAllAttemptsForUserAndModule(userId, moduleId);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            entries.add(new Entry(i + 1, results.get(i).getScore()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Score per Attempt");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setCircleColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setValueTextSize(12f);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        float threshold = userType.equalsIgnoreCase("VAD") ? 100f : 80f;
        LimitLine passLine = new LimitLine(threshold, "Pass Threshold (" + (int) threshold + "%)");
        passLine.setLineColor(getResources().getColor(android.R.color.holo_green_dark));
        passLine.setLineWidth(1.5f);
        passLine.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        passLine.setTextSize(12f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.removeAllLimitLines();
        yAxis.addLimitLine(passLine);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        Description desc = new Description();
        desc.setText("Assessment Progress Over Time");
        desc.setTextSize(12f);
        lineChart.setDescription(desc);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index = (int) e.getX() - 1;
                if (index >= 0 && index < results.size()) {
                    AssessmentResult r = results.get(index);
                    String msg = "Attempt " + (index + 1) + ": " + r.getScore() + "/" + r.getTotalQuestions();
                    Toast.makeText(ModuleProgressDetailActivity.this, msg, Toast.LENGTH_SHORT).show();

                    // 🔹 Log attempt tapped
                    analyticsTracker.logModuleAttemptTapped(
                            moduleName,
                            index + 1,
                            r.getScore(),
                            r.getTotalQuestions()
                    );
                }
            }

            @Override
            public void onNothingSelected() {}
        });

        lineChart.animateY(1000);
        lineChart.invalidate();

        // RecyclerView for attempt history
        RecyclerView recyclerView = findViewById(R.id.recyclerAttempts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AttemptHistoryAdapter(this, results, userType));

        // Log summary
        int attemptCount = results.size();
        int latestScore = attemptCount > 0 ? results.get(attemptCount - 1).getScore() : 0;
        analyticsTracker.logModuleProgressSummary(moduleName, attemptCount, latestScore);
    }
}
