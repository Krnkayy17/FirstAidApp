package com.example.firstaidapp.database;

import android.content.Context;
import android.os.Bundle;
import com.example.firstaidapp.models.Module;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ModuleProgressDAO {

    private final UserContentViewDAO userContentViewDAO;
    private final ContentDAO contentDAO;
    private final ModuleDAO moduleDAO;
    private final FirebaseAnalytics firebaseAnalytics;
    private final int userId;

    public ModuleProgressDAO(Context context, int userId) {
        this.userContentViewDAO = new UserContentViewDAO(context);
        this.contentDAO = new ContentDAO(context);
        this.moduleDAO = new ModuleDAO(context);
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.userId = userId;
    }

    public void updateProgressAndCompletion(int moduleId) {
        int viewedCount = userContentViewDAO.getViewedCountForModule(userId, moduleId);
        int totalCount = contentDAO.getAllContentsByModule(moduleId).size();

        int percent = (int) ((viewedCount / (float) totalCount) * 100);
        moduleDAO.updateProgressPercentage(moduleId, percent);

        // Log progress update
        Bundle progressBundle = new Bundle();
        progressBundle.putInt("user_id", userId);
        progressBundle.putInt("module_id", moduleId);
        progressBundle.putInt("progress_percent", percent);
        firebaseAnalytics.logEvent("module_progress_updated", progressBundle);

        if (viewedCount >= totalCount) {
            moduleDAO.updateCompletionStatus(moduleId, "Completed");

            // Log module completed
            Bundle completeBundle = new Bundle();
            completeBundle.putInt("user_id", userId);
            completeBundle.putInt("module_id", moduleId);
            firebaseAnalytics.logEvent("module_completed", completeBundle);

            // Log quiz availability
            Bundle quizBundle = new Bundle();
            quizBundle.putInt("user_id", userId);
            quizBundle.putInt("module_id", moduleId);
            firebaseAnalytics.logEvent("quiz_unlocked", quizBundle);
        }
    }
}
