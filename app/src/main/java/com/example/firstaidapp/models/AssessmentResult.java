package com.example.firstaidapp.models;

import java.util.Date;

public class AssessmentResult {
    private int resultId;
    private int userId;
    private int moduleId;
    private int score;
    private int totalQuestions;
    private int retakeCount;
    private Date dateTaken;

    // Getters and Setters
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getRetakeCount() { return retakeCount; }
    public void setRetakeCount(int retakeCount) { this.retakeCount = retakeCount; }

    public Date getDateTaken() { return dateTaken; }
    public void setDateTaken(Date dateTaken) { this.dateTaken = dateTaken; }
}
