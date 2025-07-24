package com.example.firstaidapp.models;

public class Module {
    private int moduleID;
    private String moduleName;
    private String description;
    private String difficultyLevel;
    private int estimatedDuration;
    private int totalAssessments;
    private String completionCriteria;
    private String accessedDate;
    private String completionStatus;
    private int progressPercentage;
    private boolean isLocked;

    public Module() {
        // set default values if needed
        this.completionStatus = "Not Started";
        this.progressPercentage = 0;
        this.isLocked = false;
    }

    public Module(
            int moduleID,
            String moduleName,
            String description,
            String difficultyLevel,
            int estimatedDuration,
            int totalAssessments,
            String completionCriteria,
            String accessedDate,
            String completionStatus,
            int progressPercentage,
            boolean isLocked
    ) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.estimatedDuration = estimatedDuration;
        this.totalAssessments = totalAssessments;
        this.completionCriteria = completionCriteria;
        this.accessedDate = accessedDate;
        this.completionStatus = completionStatus;
        this.progressPercentage = progressPercentage;
        this.isLocked = isLocked;
    }

    // Getters
    public int getModuleID() {
        return moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public int getTotalAssessments() {
        return totalAssessments;
    }

    public String getCompletionCriteria() {
        return completionCriteria;
    }

    public String getAccessedDate() {
        return accessedDate;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public boolean isLocked() {
        return isLocked;
    }

    // Setters
    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public void setTotalAssessments(int totalAssessments) {
        this.totalAssessments = totalAssessments;
    }

    public void setCompletionCriteria(String completionCriteria) {
        this.completionCriteria = completionCriteria;
    }

    public void setAccessedDate(String accessedDate) {
        this.accessedDate = accessedDate;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
