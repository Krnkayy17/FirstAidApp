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
    private String completionStatus;  // New Field

    public Module(int moduleID, String moduleName, String description, String difficultyLevel, int estimatedDuration, int totalAssessments, String completionCriteria, String accessedDate, String completionStatus) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.estimatedDuration = estimatedDuration;
        this.totalAssessments = totalAssessments;
        this.completionCriteria = completionCriteria;
        this.accessedDate = accessedDate;
        this.completionStatus = completionStatus;
    }

    // Getters
    public int getModuleID() { return moduleID; }
    public String getModuleName() { return moduleName; }
    public String getDescription() { return description; }
    public String getDifficultyLevel() { return difficultyLevel; }
    public int getEstimatedDuration() { return estimatedDuration; }
    public int getTotalAssessments() { return totalAssessments; }
    public String getCompletionCriteria() { return completionCriteria; }
    public String getAccessedDate() { return accessedDate; }
    public String getCompletionStatus() { return completionStatus; }

    // Setters
    public void setCompletionStatus(String completionStatus) { this.completionStatus = completionStatus; }
}


