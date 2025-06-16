package com.example.firstaidapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.AttemptHistoryAdapter;
import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.models.AssessmentResult;
import com.example.firstaidapp.utils.SessionManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ModuleProgressDetailActivity extends AppCompatActivity {

    private LineChart lineChart;
    private int moduleId;
    private String moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_progress_detail);

        lineChart = findViewById(R.id.lineChart);
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        moduleName = getIntent().getStringExtra("MODULE_NAME");

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(moduleName + " Progress");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Load progress chart and past attempts
        loadProgressData();
    }

    private void loadProgressData() {
        AssessmentResultDAO dao = new AssessmentResultDAO(this);
        List<AssessmentResult> results = dao.getAllAttemptsForModule(moduleId);

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

        Description desc = new Description();
        desc.setText("Assessment Progress Over Time");
        desc.setTextSize(12f);
        lineChart.setDescription(desc);
        lineChart.animateY(1000);
        lineChart.invalidate();

        RecyclerView recyclerView = findViewById(R.id.recyclerAttempts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SessionManager sessionManager = new SessionManager(this);
        String userType = sessionManager.getUserType();

        recyclerView.setAdapter(new AttemptHistoryAdapter(this, results, userType));
    }
}
