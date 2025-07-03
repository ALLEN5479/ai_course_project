package com.example.ai_lesson.ai.entity;

public class AiResult {
    private int courseScore;
    private int practiceScore;
    private int innovationScore;
    private int teamworkScore;
    private int communicationScore;
    private int qualityScore;
    private String report;

    // Getter and Setter
    public int getCourseScore() { return courseScore; }
    public void setCourseScore(int courseScore) { this.courseScore = courseScore; }

    public int getPracticeScore() { return practiceScore; }
    public void setPracticeScore(int practiceScore) { this.practiceScore = practiceScore; }

    public int getInnovationScore() { return innovationScore; }
    public void setInnovationScore(int innovationScore) { this.innovationScore = innovationScore; }

    public int getTeamworkScore() { return teamworkScore; }
    public void setTeamworkScore(int teamworkScore) { this.teamworkScore = teamworkScore; }

    public int getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(int communicationScore) { this.communicationScore = communicationScore; }

    public int getQualityScore() { return qualityScore; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }

    public String getReport() { return report; }
    public void setReport(String report) { this.report = report; }
}
