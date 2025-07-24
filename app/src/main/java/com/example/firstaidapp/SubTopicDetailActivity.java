package com.example.firstaidapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.firstaidapp.database.ContentDAO;
import com.example.firstaidapp.database.ModuleDAO;
import com.example.firstaidapp.database.ModuleProgressDAO;
import com.example.firstaidapp.models.Content;
import com.example.firstaidapp.database.UserContentViewDAO;
import com.example.firstaidapp.utils.SessionManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class SubTopicDetailActivity extends AppCompatActivity {

    private TextView detailTitle, tvContentProgress;
    private Button nextButton, previousButton;
    private LinearLayout contentContainer;
    private ProgressBar contentProgressBar;
    private FloatingActionButton btnTakeQuiz;

    private int moduleId;
    private int contentOrder;
    private ContentDAO contentDAO;
    private UserContentViewDAO userContentViewDAO;
    private ModuleDAO moduleDAO;
    private int userId;
    private ModuleProgressDAO moduleProgressDAO;


    private FirebaseAnalytics firebaseAnalytics;

    private int totalContentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_topic_detail);

        // Session and DAOs
        SessionManager sessionManager = new SessionManager(this);
        userId = sessionManager.getUserId();

        userContentViewDAO = new UserContentViewDAO(this);
        moduleDAO = new ModuleDAO(this);
        contentDAO = new ContentDAO(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // UI binding
        detailTitle = findViewById(R.id.detailTitle);
        tvContentProgress = findViewById(R.id.tvContentProgress);
        contentProgressBar = findViewById(R.id.contentProgressBar);
        contentContainer = findViewById(R.id.contentContainer);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        btnTakeQuiz = findViewById(R.id.btnTakeQuiz);
        moduleProgressDAO = new ModuleProgressDAO(this, userId);

        // Get module ID and total content count
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        totalContentCount = contentDAO.getAllContentsByModule(moduleId).size();

        // Prompt user to resume from last viewed content
        int lastViewed = userContentViewDAO.getLastViewedOrder(userId, moduleId);
        if (lastViewed > 1) {
            new AlertDialog.Builder(this)
                    .setTitle("Resume Learning?")
                    .setMessage("Would you like to continue from where you left off?")
                    .setPositiveButton("Yes", (dialog, which) -> displaySection(lastViewed))
                    .setNegativeButton("No", (dialog, which) -> displaySection(1))
                    .show();
        } else {
            displaySection(1);
        }

        // Quiz button navigation
        btnTakeQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(SubTopicDetailActivity.this, QuizActivity.class);
            intent.putExtra("MODULE_ID", moduleId);
            startActivity(intent);
        });
    }

    private void displaySection(int order) {
        contentOrder = order;
        contentContainer.removeAllViews();

        List<Content> sectionContent = contentDAO.getContentsByModuleAndOrder(moduleId, contentOrder);
        if (sectionContent == null || sectionContent.isEmpty()) {
            Toast.makeText(this, "No content for this section", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set section title
        for (Content content : sectionContent) {
            if (content.getContentTitle() != null && !content.getContentTitle().isEmpty()) {
                detailTitle.setText(content.getContentTitle());
                break;
            }
        }

        // Render content
        for (Content content : sectionContent) {
            userContentViewDAO.markContentAsViewed(userId, content.getContentId(), moduleId);

            logFirebaseContentView(content);

            switch (content.getContentType()) {
                case "text":
                    if (content.getContentText() != null) addTextView(content.getContentText());
                    break;
                case "image":
                    if (content.getContentImage() != null) addImageView(content.getContentImage());
                    break;
                case "video":
                    if (content.getContentURL() != null) addVideoLink(content.getContentURL());
                    break;
            }
        }

        updateProgressUI();
        handleNavigation();
    }

    // Updates progress UI and quiz button visibility
    private void updateProgressUI() {
        ModuleProgressDAO moduleProgressDAO = new ModuleProgressDAO(this, userId);
        moduleProgressDAO.updateProgressAndCompletion(moduleId);

        // Update progress module
        int viewedCount = userContentViewDAO.getViewedCountForModule(userId, moduleId);
        int totalCount = contentDAO.getAllContentsByModule(moduleId).size();
        int percent = (int) ((viewedCount / (float) totalCount) * 100);

        tvContentProgress.setText("Progress: " + viewedCount + " / " + totalCount);
        contentProgressBar.setProgress(percent);

        btnTakeQuiz.setVisibility(viewedCount >= totalCount ? View.VISIBLE : View.GONE);


    }

    private void handleNavigation() {
        nextButton.setOnClickListener(view -> {
            int nextOrder = getNextOrder(contentOrder);
            if (nextOrder != -1) {
                displaySection(nextOrder);
            } else {
                showStartQuizDialog();
            }
        });

        previousButton.setOnClickListener(view -> {
            int prevOrder = getPreviousOrder(contentOrder);
            if (prevOrder != -1) {
                displaySection(prevOrder);
            } else {
                Toast.makeText(this, "You're already at the first section.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Shows dialog to prompt user to start quiz after finishing content
    private void showStartQuizDialog() {
        int viewedCount = userContentViewDAO.getViewedCountForModule(userId, moduleId);
        boolean moduleCompleted = viewedCount >= totalContentCount;

        moduleProgressDAO.updateProgressAndCompletion(moduleId);

        new AlertDialog.Builder(this)
                .setTitle("Module Complete!")
                .setMessage("You've finished the module. Would you like to take the quiz now?")
                .setPositiveButton("Start Quiz", (dialog, which) -> {
                    Intent intent = new Intent(SubTopicDetailActivity.this, QuizActivity.class);
                    intent.putExtra("MODULE_ID", moduleId);
                    startActivity(intent);
                })
                .setNegativeButton("Later", (dialog, which) -> {
                    Toast.makeText(this, "You can take the quiz anytime from the assessment page.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubTopicDetailActivity.this, ModuleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private int getNextOrder(int currentOrder) {
        List<Integer> orders = contentDAO.getAllOrdersForModule(moduleId);
        int index = orders.indexOf(currentOrder);
        return (index != -1 && index + 1 < orders.size()) ? orders.get(index + 1) : -1;
    }

    private int getPreviousOrder(int currentOrder) {
        List<Integer> orders = contentDAO.getAllOrdersForModule(moduleId);
        int index = orders.indexOf(currentOrder);
        return (index > 0) ? orders.get(index - 1) : -1;
    }

    private void addTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        textView.setLayoutParams(getDefaultLayoutParams());
        contentContainer.addView(textView);
    }

    private void addImageView(String imageName) {
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        if (resId != 0) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resId);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(getDefaultLayoutParams());
            contentContainer.addView(imageView);
        }
    }

    // Add a clickable video thumbnail
    private void addVideoLink(String url) {
        String videoId = extractYouTubeVideoId(url);
        if (videoId == null || videoId.isEmpty()) return;

        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";

        ImageView thumbnail = new ImageView(this);
        thumbnail.setLayoutParams(getDefaultLayoutParams());
        thumbnail.setAdjustViewBounds(true);
        thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(this)
                .load(thumbnailUrl)
                .placeholder(R.drawable.video_placeholder) // Add a placeholder image in drawable
                .into(thumbnail);

        thumbnail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url));
            startActivity(intent);
        });

        contentContainer.addView(thumbnail);
    }

    // Extract video ID from YouTube URL
    private String extractYouTubeVideoId(String url) {
        if (url == null) return null;
        String pattern = "(?<=youtu.be/|watch\\?v=|embed/)[^&#\\n]+";
        java.util.regex.Pattern compiledPattern = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            String videoId = matcher.group();
            // Remove anything after "?" in case of additional query parameters
            int questionMarkIndex = videoId.indexOf("?");
            if (questionMarkIndex != -1) {
                videoId = videoId.substring(0, questionMarkIndex);
            }
            return videoId;
        }
        return null;
    }

    // Default layout parameters for dynamic views
    private LinearLayout.LayoutParams getDefaultLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 16);
        return params;
    }

    // Log content view event to Firebase Analytics
    private void logFirebaseContentView(Content content) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "content_" + content.getContentId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, content.getContentTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content.getContentType());
        firebaseAnalytics.logEvent("content_viewed", bundle);
    }
}