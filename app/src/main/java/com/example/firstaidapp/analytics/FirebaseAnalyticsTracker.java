package com.example.firstaidapp.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsTracker {

    private final FirebaseAnalytics analytics;

    public FirebaseAnalyticsTracker(Context context) {
        analytics = FirebaseAnalytics.getInstance(context);
    }

    public void logQuizCompleted(String moduleName, int score, String userType) {
        Bundle bundle = new Bundle();
        bundle.putString("module_name", moduleName);
        bundle.putInt("score", score);
        bundle.putString("user_type", userType);
        analytics.logEvent("quiz_completed", bundle);
    }

    public void logVideoRecommendationShown(String moduleName, int videoCount) {
        Bundle bundle = new Bundle();
        bundle.putString("module_name", moduleName);
        bundle.putInt("video_count", videoCount);
        analytics.logEvent("video_recommendation_shown", bundle);
    }

    public void logVideoClicked(String moduleName, String videoId, String videoTitle) {
        Bundle bundle = new Bundle();
        bundle.putString("module_name", moduleName);
        bundle.putString("video_id", videoId);
        bundle.putString("video_title", videoTitle);
        analytics.logEvent("video_clicked", bundle);
    }

    public void logVideoSeeAllClicked(String source) {
        Bundle bundle = new Bundle();
        bundle.putString("source", source);
        bundle.putString("action", "see_all_clicked");
        analytics.logEvent("video_see_all_clicked", bundle);
    }


    public void logContentViewed(String contentTitle, String moduleName) {
        Bundle bundle = new Bundle();
        bundle.putString("content_title", contentTitle);
        bundle.putString("module_name", moduleName);
        analytics.logEvent("content_viewed", bundle);
    }

    // Log screen view
    public void logProgressSummaryViewed() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "ProgressSummary");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ProgressSummaryActivity");
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    // Log overall module progress breakdown
    public void logProgressBreakdown(int completed, int inProgress, int notStarted) {
        Bundle bundle = new Bundle();
        bundle.putInt("modules_completed", completed);
        bundle.putInt("modules_in_progress", inProgress);
        bundle.putInt("modules_not_started", notStarted);
        analytics.logEvent("progress_summary_breakdown", bundle);
    }

    // Log filter used by user
    public void logProgressFilterSelected(String filterName) {
        Bundle bundle = new Bundle();
        bundle.putString("filter_selected", filterName);
        analytics.logEvent("progress_filter_selected", bundle);
    }

    // Log when user opens module progress detail screen
    public void logModuleProgressDetailViewed(String moduleName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "ModuleProgressDetail");
        bundle.putString("module_name", moduleName);
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    // Log module summary (total attempts and latest score)
    public void logModuleProgressSummary(String moduleName, int attemptCount, int latestScore) {
        Bundle bundle = new Bundle();
        bundle.putString("module_name", moduleName);
        bundle.putInt("attempt_count", attemptCount);
        bundle.putInt("latest_score", latestScore);
        analytics.logEvent("module_progress_summary", bundle);
    }

    // Log when user taps a point in the chart (views attempt detail)
    public void logModuleAttemptTapped(String moduleName, int attemptIndex, int score, int totalQuestions) {
        Bundle bundle = new Bundle();
        bundle.putString("module_name", moduleName);
        bundle.putInt("attempt_index", attemptIndex);
        bundle.putInt("score", score);
        bundle.putInt("total_questions", totalQuestions);
        analytics.logEvent("module_progress_attempt_tapped", bundle);
    }


}
