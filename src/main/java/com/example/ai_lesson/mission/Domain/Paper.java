package com.example.ai_lesson.mission.Domain;

public class Paper {
    private Integer paperId;
    private String paperName;
    private String paperDescription;
    private Integer totalScore;
    private Integer courseId;

    // 构造函数
    public Paper() {}

    public Paper(String paperName, String paperDescription, Integer totalScore) {
        this.paperName = paperName;
        this.paperDescription = paperDescription;
        this.totalScore = totalScore;
    }

    // Getter和Setter方法
    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperDescription() {
        return paperDescription;
    }

    public void setPaperDescription(String paperDescription) {
        this.paperDescription = paperDescription;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Paper{" +
               "paperId=" + paperId +
               ", paperName='" + paperName + '\'' +
               ", paperDescription='" + paperDescription + '\'' +
               ", totalScore=" + totalScore +
               ", courseId=" + courseId +
               '}';
    }
}

