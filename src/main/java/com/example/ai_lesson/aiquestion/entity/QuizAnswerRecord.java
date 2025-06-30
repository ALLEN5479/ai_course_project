package com.example.ai_lesson.aiquestion.entity;

public class QuizAnswerRecord {
    private Long id;
    private Long studentId;
    private Integer questionId;
    private Boolean isCorrect;
    private Double difficulty;
    private String abilityType; // 或 category
    private Double score;
    private Long usedTime;
    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Integer getQuestionId() { return questionId; }
    public void setQuestionId(Integer questionId) { this.questionId = questionId; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }

    public Double getDifficulty() { return difficulty; }
    public void setDifficulty(Double difficulty) { this.difficulty = difficulty; }

    public String getAbilityType() { return abilityType; }
    public void setAbilityType(String abilityType) { this.abilityType = abilityType; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Long getUsedTime() { return usedTime; }
    public void setUsedTime(Long usedTime) { this.usedTime = usedTime; }
}
