package com.example.ai_lesson.mission.Domain;

import java.time.LocalDateTime;

public class MissionSubmission {
    private Integer submissionId;
    private Integer publishedMissionId;
    private Integer studentId;
    private LocalDateTime submitTime;
    private String submitStatus;
    private String gradeStatus;
    private Integer score;
    private String filePath;
    private String fileName;
    private String fileSize;
    private String comment;
    private LocalDateTime gradeTime;
    private Integer graderId;

    // 构造函数
    public MissionSubmission() {}

    public MissionSubmission(Integer publishedMissionId, Integer studentId) {
        this.publishedMissionId = publishedMissionId;
        this.studentId = studentId;
        this.submitStatus = "not_submitted";
        this.gradeStatus = "pending";
    }

    // Getter和Setter方法
    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getPublishedMissionId() {
        return publishedMissionId;
    }

    public void setPublishedMissionId(Integer publishedMissionId) {
        this.publishedMissionId = publishedMissionId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getGradeStatus() {
        return gradeStatus;
    }

    public void setGradeStatus(String gradeStatus) {
        this.gradeStatus = gradeStatus;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getGradeTime() {
        return gradeTime;
    }

    public void setGradeTime(LocalDateTime gradeTime) {
        this.gradeTime = gradeTime;
    }

    public Integer getGraderId() {
        return graderId;
    }

    public void setGraderId(Integer graderId) {
        this.graderId = graderId;
    }
} 