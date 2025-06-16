package com.example.firstaidapp.models;

public class ModuleProgressStatus {
    private int contentProgress;
    private int assessmentProgress;
    private int overallProgress;
    private boolean isPassed;

    public ModuleProgressStatus(int contentProgress, int assessmentProgress, int overallProgress, boolean isPassed) {
        this.contentProgress = contentProgress;
        this.assessmentProgress = assessmentProgress;
        this.overallProgress = overallProgress;
        this.isPassed = isPassed;
    }

    public int getContentProgress() {
        return contentProgress;
    }

    public void setContentProgress(int contentProgress) {
        this.contentProgress = contentProgress;
    }

    public int getAssessmentProgress() {
        return assessmentProgress;
    }

    public void setAssessmentProgress(int assessmentProgress) {
        this.assessmentProgress = assessmentProgress;
    }

    public int getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(int overallProgress) {
        this.overallProgress = overallProgress;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }
}
