package com.example.firstaidapp.utils;

import android.content.Context;
import android.util.Log;

import com.example.firstaidapp.database.AssessmentResultDAO;
import com.example.firstaidapp.database.QuestionDAO;
import com.example.firstaidapp.database.UserContentViewDAO;
import com.example.firstaidapp.models.Content;
import com.example.firstaidapp.models.ModuleProgressStatus;
import com.example.firstaidapp.models.Question;

import java.util.List;

public class ModuleProgressUtil {

    private static final String TAG = "ModuleProgressUtil";

    /**
     * Calculates overall module progress as a weighted percentage.
     * Weight: 30% content viewed + 70% quiz score.
     */
    public static int calculateModuleProgress(Context context, int userId, int moduleId) {
        int contentProgress = calculateContentProgress(context, userId, moduleId);
        int assessmentProgress = calculateAssessmentProgress(context, userId, moduleId);
        return (int) ((0.3 * contentProgress) + (0.7 * assessmentProgress));
    }

    /**
     * Calculates content viewing progress.
     */
    public static int calculateContentProgress(Context context, int userId, int moduleId) {
        UserContentViewDAO contentViewDAO = new UserContentViewDAO(context);
        List<Content> contents = contentViewDAO.getAllContentForModule(moduleId);

        int total = contents.size();
        int viewed = contentViewDAO.getViewedContentCount(userId, moduleId);

        return total > 0 ? (int) ((viewed / (float) total) * 100) : 0;
    }

    /**
     * Calculates assessment progress as percentage of latest quiz score.
     */
    private static int calculateAssessmentProgress(Context context, int userId, int moduleId) {
        QuestionDAO questionDAO = new QuestionDAO(context);
        AssessmentResultDAO resultDAO = new AssessmentResultDAO(context);

        int totalQuestions = questionDAO.getQuestionsByModuleId(moduleId).size();
        if (totalQuestions == 0) {
            Log.w(TAG, "No questions found for moduleId=" + moduleId);
            return 0;
        }

        int latestScore = resultDAO.getLatestScoreForModule(userId, moduleId);
        return (int) ((latestScore / (float) totalQuestions) * 100);
    }

    /**
     * Determines if the user has passed the quiz for the module.
     * General: 80% or more. VAD: 100%.
     */
    public static boolean isAssessmentPassed(Context context, int userId, int moduleId) {
        SessionManager sessionManager = new SessionManager(context);
        String userType = sessionManager.getUserType();

        QuestionDAO questionDAO = new QuestionDAO(context);
        AssessmentResultDAO resultDAO = new AssessmentResultDAO(context);

        int totalQuestions = questionDAO.getQuestionsByModuleId(moduleId).size();
        if (totalQuestions == 0) {
            Log.w(TAG, "Attempted to check pass status but no quiz available for moduleId=" + moduleId);
            return false;
        }

        int latestScore = resultDAO.getLatestScoreForModule(userId, moduleId);
        float percentage = (latestScore / (float) totalQuestions) * 100;

        return "VAD".equalsIgnoreCase(userType) ? percentage == 100 : percentage >= 80;
    }

    /**
     * Convenience method using session userId.
     */
    public static boolean hasPassedAssessment(Context context, int moduleId) {
        SessionManager sessionManager = new SessionManager(context);
        int userId = sessionManager.getUserId();
        return isAssessmentPassed(context, userId, moduleId);
    }

    /**
     * Returns detailed progress status object.
     */
    public static ModuleProgressStatus getModuleProgressStatus(Context context, int userId, int moduleId) {
        int contentProgress = calculateContentProgress(context, userId, moduleId);
        int assessmentProgress = calculateAssessmentProgress(context, userId, moduleId);
        int overallProgress = (int) ((0.3 * contentProgress) + (0.7 * assessmentProgress));
        boolean isPassed = isAssessmentPassed(context, userId, moduleId);

        return new ModuleProgressStatus(contentProgress, assessmentProgress, overallProgress, isPassed);
    }
}
