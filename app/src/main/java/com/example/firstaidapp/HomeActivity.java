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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        Button btnSeeAll = findViewById(R.id.btnSeeAllVideos);
        btnSeeAll.setOnClickListener(v -> {
            new FirebaseAnalyticsTracker(this).logVideoSeeAllClicked("HomeSmartFeed");
            Intent intent = new Intent(HomeActivity.this, RecommendationActivity.class);
            // Optional: don't pass module name to show global recommendations
            startActivity(intent);
        });

    }

    private void setupBannerSlider() {
        int[] images = {
                R.drawable.banner_cpr,
                R.drawable.banner_bleeding,
                R.drawable.banner_emergency
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


    private void setupModuleList() {
        moduleDAO = new ModuleDAO(this);
        recyclerHomeModules.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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

        // Step 1: Try getting the most watched tags
        List<String> topTags = videoDAO.getMostWatchedTags(-1, 2); // -1 = global
        Log.d("SmartFeed", "Most watched tags: " + topTags);

        // Step 2: Fallback to default tags if user hasn't watched anything
        if (topTags == null || topTags.isEmpty()) {
            topTags = Arrays.asList("cpr_basics", "aed", "bleeding_control", "infant_cpr");
            Log.d("SmartFeed", "Fallback tags used: " + topTags);
        }

        // Step 3: Collect recommended videos
        List<VideoRecommendation> smartVideos = new ArrayList<>();
        for (String tag : topTags) {
            List<VideoRecommendation> videosByTag = videoDAO.getRecommendationsByTag(tag, 3);
            for (VideoRecommendation v : videosByTag) {
                if (!smartVideos.contains(v)) {
                    smartVideos.add(v);
                }
            }
        }

        Log.d("SmartFeed", "Total videos loaded: " + smartVideos.size());

        // Step 4: Setup RecyclerView
        RecyclerView rvSmartFeed = findViewById(R.id.recyclerSmartFeed);
        rvSmartFeed.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        VideoRecommendationAdapter adapter = new VideoRecommendationAdapter(smartVideos, this, video -> {
            // Log Firebase event
            new FirebaseAnalyticsTracker(this).logVideoClicked("HomeSmartFeed", video.getYoutubeVideoId(), video.getTitle());

            // Save to local click log
            String timestamp = String.valueOf(System.currentTimeMillis());
            new VideoClickLogDAO(this).insertLog(new VideoClickLog(
                    video.getModuleId(),
                    video.getTitle(),
                    video.getYoutubeVideoId(),
                    timestamp,
                    video.getTag()
            ));

            // Open video in YouTube
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
