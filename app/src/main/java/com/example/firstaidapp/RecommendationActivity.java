package com.example.firstaidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstaidapp.adapters.VideoRecommendationAdapter;
import com.example.firstaidapp.analytics.FirebaseAnalyticsTracker;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.VideoClickLogDAO;
import com.example.firstaidapp.database.VideoRecommendationDAO;
import com.example.firstaidapp.models.VideoClickLog;
import com.example.firstaidapp.models.VideoRecommendation;

import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner spinnerModuleFilter;

    private VideoRecommendationAdapter adapter;
    private FirebaseAnalyticsTracker tracker;
    private ModuleDAO moduleDAO;
    private VideoRecommendationDAO videoDAO;

    private List<String> moduleNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        recyclerView = findViewById(R.id.recyclerRecommendation);
        spinnerModuleFilter = findViewById(R.id.spinner_module_filter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tracker = new FirebaseAnalyticsTracker(this);
        moduleDAO = new ModuleDAO(this);
        videoDAO = new VideoRecommendationDAO(this);

        setupModuleFilter();

        String moduleNameFromIntent = getIntent().getStringExtra("MODULE_NAME");
        if (moduleNameFromIntent != null) {
            int moduleId = moduleDAO.getModuleIdByName(moduleNameFromIntent);
            if (moduleId != -1) {
                loadRecommendationsByModule(moduleId, moduleNameFromIntent);
            } else {
                Toast.makeText(this, "Module not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            loadGlobalRecommendations();
        }
    }

    private void setupModuleFilter() {
        moduleNames = moduleDAO.getAllModuleNames();
        moduleNames.add(0, "Select Module");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, moduleNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModuleFilter.setAdapter(spinnerAdapter);

        spinnerModuleFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = moduleNames.get(position);
                if (selected.equals("Select Module")) {
                    loadGlobalRecommendations();
                } else {
                    int moduleId = moduleDAO.getModuleIdByName(selected);
                    if (moduleId != -1) {
                        loadRecommendationsByModule(moduleId, selected);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadRecommendationsByModule(int moduleId, String moduleName) {
        List<VideoRecommendation> allVideos = videoDAO.getRecommendationsByModule(moduleId);
        List<String> topTags = videoDAO.getMostWatchedTags(moduleId, 2);

        List<VideoRecommendation> sorted = new ArrayList<>();

        for (String tag : topTags) {
            for (VideoRecommendation video : allVideos) {
                if (video.getTag() != null && video.getTag().equalsIgnoreCase(tag) && !sorted.contains(video)) {
                    sorted.add(video);
                }
            }
        }

        for (VideoRecommendation video : allVideos) {
            if (!sorted.contains(video)) {
                sorted.add(video);
            }
        }

        tracker.logVideoRecommendationShown(moduleName, sorted.size());

        adapter = new VideoRecommendationAdapter(sorted, this, video -> {
            tracker.logVideoClicked(moduleName, video.getYoutubeVideoId(), video.getTitle());

            String timestamp = String.valueOf(System.currentTimeMillis());
            new VideoClickLogDAO(this).insertLog(new VideoClickLog(
                    video.getModuleId(),
                    video.getTitle(),
                    video.getYoutubeVideoId(),
                    timestamp,
                    video.getTag()
            ));

            String url = "https://www.youtube.com/watch?v=" + video.getYoutubeVideoId();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        recyclerView.setAdapter(adapter);
    }

    private void loadGlobalRecommendations() {
        List<VideoRecommendation> global = videoDAO.getAllRecommendationsSortedByClickCount();

        if (global.isEmpty()) {
            Toast.makeText(this, "No video recommendations available.", Toast.LENGTH_SHORT).show();
            return;
        }

        tracker.logVideoRecommendationShown("AllModules", global.size());

        adapter = new VideoRecommendationAdapter(global, this, video -> {
            tracker.logVideoClicked("AllModules", video.getYoutubeVideoId(), video.getTitle());

            String timestamp = String.valueOf(System.currentTimeMillis());
            new VideoClickLogDAO(this).insertLog(new VideoClickLog(
                    video.getModuleId(),
                    video.getTitle(),
                    video.getYoutubeVideoId(),
                    timestamp,
                    video.getTag()
            ));

            String url = "https://www.youtube.com/watch?v=" + video.getYoutubeVideoId();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        recyclerView.setAdapter(adapter);
    }
}
