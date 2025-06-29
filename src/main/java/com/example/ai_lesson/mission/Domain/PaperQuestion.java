package com.example.ai_lesson.mission.Domain;

public class PaperQuestion {
    private Integer id;
    private Integer paperId;
    private Integer questionId;
    private Integer sortOrder;
    private Integer score;
    private String questionType;

    // 构造函数
    public PaperQuestion() {}

    public PaperQuestion(Integer paperId, Integer questionId, Integer sortOrder, Integer score, String questionType) {
        this.paperId = paperId;
        this.questionId = questionId;
        this.sortOrder = sortOrder;
        this.score = score;
        this.questionType = questionType;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
} 