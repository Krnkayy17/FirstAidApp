package com.example.firstaidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.firstaidapp.adapters.FunFactAdapter;
import com.example.firstaidapp.adapters.ImageSliderAdapter;
import com.example.firstaidapp.adapters.ModuleHomeAdapter;
import com.example.firstaidapp.adapters.VideoRecommendationAdapter;
import com.example.firstaidapp.analytics.FirebaseAnalyticsTracker;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.VideoClickLogDAO;
import com.example.firstaidapp.database.VideoRecommendationDAO;
import com.example.firstaidapp.models.VideoClickLog;
import com.example.firstaidapp.models.VideoRecommendation;
import com.example.firstaidapp.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView bannerSliderRecycler, funFactRecycler, recyclerHomeModules;
    private BottomNavigationView bottomNavigationView;
    private ModuleDAO moduleDAO;
    private Handler handler = new Handler();

    private final Runnable autoScrollBanners = new Runnable() {
        @Override
        public void run() {
            if (bannerSliderRecycler.getAdapter() != null && bannerSliderRecycler.getAdapter().getItemCount() > 0) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) bannerSliderRecycler.getLayoutManager();
                if (layoutManager != null) {
                    int next = (layoutManager.findFirstVisibleItemPosition() + 1) % bannerSliderRecycler.getAdapter().getItemCount();
                    bannerSliderRecycler.smoothScrollToPosition(next);
                    handler.postDelayed(this, 4000); // scroll every 4 seconds
                }
            }
        }
    };

    private final Runnable autoScrollFunFacts = new Runnable() {
        @Override
        public void run() {
            if (funFactRecycler != null && funFactRecycler.getAdapter() != null) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) funFactRecycler.getLayoutManager();
                if (layoutManager != null) {
                    int next = (layoutManager.findFirstVisibleItemPosition() + 1) % funFactRecycler.getAdapter().getItemCount();
                    funFactRecycler.smoothScrollToPosition(next);
                    handler.postDelayed(this, 5000);
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Views
        bannerSliderRecycler = findViewById(R.id.imageSliderRecycler);
        funFactRecycler = findViewById(R.id.funFactRecycler);
        recyclerHomeModules = findViewById(R.id.recyclerHomeModules);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupBannerSlider();
        setupFunFacts();
        setupModuleList();
        setupBottomNavigation();

        loadSmartVideoFeed();

        // Set up "See All Videos" button
        Button btnSeeAll = findViewById(R.id.btnSeeAllVideos);
        btnSeeAll.setOnClickListener(v -> {
            new FirebaseAnalyticsTracker(this).logVideoSeeAllClicked("HomeSmartFeed");
            Intent intent = new Intent(HomeActivity.this, RecommendationActivity.class);
            startActivity(intent);
        });

    }

    private void setupBannerSlider() {
        int[] images = {
                R.drawable.banner_cpr,
                R.drawable.banner_bleeding,
                R.drawable.banner_emergency,
                R.drawable.cpr_banner
        };

        bannerSliderRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bannerSliderRecycler.setAdapter(new ImageSliderAdapter(this, images));
        new LinearSnapHelper().attachToRecyclerView(bannerSliderRecycler);

        // Start auto-scroll
        handler.postDelayed(autoScrollBanners, 4000);
    }


    private void setupFunFacts() {
        List<String> facts = Arrays.asList(
                "💡 CPR can double survival chances.",
                "💡 Lean forward for nosebleeds.",
                "💡 Direct pressure stops bleeding.",
                "💡 AEDs are voice-guided."
        );

        funFactRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        funFactRecycler.setAdapter(new FunFactAdapter(facts));
        new LinearSnapHelper().attachToRecyclerView(funFactRecycler);

        handler.postDelayed(autoScrollFunFacts, 5000);
    }

    // Loads module data
    private void setupModuleList() {
        moduleDAO = new ModuleDAO(this);
        recyclerHomeModules.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerHomeModules.setAdapter(new ModuleHomeAdapter(this, moduleDAO.getAllModules()));
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true; // already on HomeActivity
            } else if (itemId == R.id.nav_modules) {
                startActivity(new Intent(this, ModuleActivity.class));
                return true;
            } else if (itemId == R.id.nav_assessments) {
                startActivity(new Intent(this, AssessmentActivity.class));
                return true;
            } else if (itemId == R.id.nav_progress) {
                startActivity(new Intent(this, ProgressSummaryActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, UserProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    private void loadSmartVideoFeed() {
        VideoRecommendationDAO videoDAO = new VideoRecommendationDAO(this);
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        String userIdStr = String.valueOf(userId);

        // Step 1: Get personalized videos by user watch count
        List<VideoRecommendation> smartVideos = videoDAO.getRecommendationsByUserWatchCount(userIdStr, 3);
        Set<String> videoIdsAdded = new HashSet<>();
        for (VideoRecommendation video : smartVideos) {
            videoIdsAdded.add(video.getYoutubeVideoId());
        }

        // Step 2: Supplement with videos from key tags (e.g., CPR, Bleeding)
        List<String> keyTags = Arrays.asList("cpr_basics", "bleeding_control");
        for (String tag : keyTags) {
            List<VideoRecommendation> tagVideos = videoDAO.getRecommendationsByTag(tag, 3);
            for (VideoRecommendation video : tagVideos) {
                if (!videoIdsAdded.contains(video.getYoutubeVideoId())) {
                    smartVideos.add(video);
                    videoIdsAdded.add(video.getYoutubeVideoId());
                }
            }
        }

        // Shuffle to randomize the display
        Collections.shuffle(smartVideos);

        // Step 3: Display the recommendations
        RecyclerView rvSmartFeed = findViewById(R.id.recyclerSmartFeed);
        rvSmartFeed.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        VideoRecommendationAdapter adapter = new VideoRecommendationAdapter(smartVideos, this, video -> {
            // Log to Firebase Analytics
            new FirebaseAnalyticsTracker(this).logVideoClicked("HomeSmartFeed", video.getYoutubeVideoId(), video.getTitle());

            // Log to local DB
            String timestamp = String.valueOf(System.currentTimeMillis());
            new VideoClickLogDAO(this).insertLog(new VideoClickLog(
                    userId,
                    video.getModuleId(),
                    video.getTitle(),
                    video.getYoutubeVideoId(),
                    timestamp,
                    video.getTag()
            ));

            // Open YouTube link
            String url = "https://www.youtube.com/watch?v=" + video.getYoutubeVideoId();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        rvSmartFeed.setAdapter(adapter);
    }



    @Override
    protected void onDestroy() {
        handler.removeCallbacks(autoScrollFunFacts);
        handler.removeCallbacks(autoScrollBanners); // ⬅ stop banner auto-scroll
        super.onDestroy();
    }

}
