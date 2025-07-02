package com.example.ai_lesson.career.entity;

public class CareerAbilityData {
    private String studentId;
    private String name;
    private String className;
    private Integer courseScore;
    private Integer practiceScore;
    private Integer qualityScore;
    private Integer innovationScore;
    private Integer teamworkScore;
    private Integer communicationScore;
    private Integer totalScore;
    private Integer rank;
    private String aiReport; // AI能力报告文本
    // getter/setter
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Integer getCourseScore() { return courseScore; }
    public void setCourseScore(Integer courseScore) { this.courseScore = courseScore; }

    public Integer getPracticeScore() { return practiceScore; }
    public void setPracticeScore(Integer practiceScore) { this.practiceScore = practiceScore; }

    public Integer getQualityScore() { return qualityScore; }
    public void setQualityScore(Integer qualityScore) { this.qualityScore = qualityScore; }

    public Integer getInnovationScore() { return innovationScore; }
    public void setInnovationScore(Integer innovationScore) { this.innovationScore = innovationScore; }

    public Integer getTeamworkScore() { return teamworkScore; }
    public void setTeamworkScore(Integer teamworkScore) { this.teamworkScore = teamworkScore; }

    public Integer getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(Integer communicationScore) { this.communicationScore = communicationScore; }

    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public String getAiReport() { return aiReport; }
    public void setAiReport(String aiReport) { this.aiReport = aiReport; }
}
