package com.example.ai_lesson.ability.Domain;

import java.util.Date;

public class StudentAbility {
    private Long id;
    private Long studentId;
    private String studentName;
    
    // 各项能力分数
    private Double programmingScore;    // 编程能力
    private Double aiScore;             // AI理解
    private Double mathScore;           // 数学基础
    private Double creativityScore;     // 创新能力
    private Double communicationScore;  // 沟通能力
    
    // ELO评分系统
    private String abilityType;
    private Double eloScore;
    private Integer kFactor;
    
    // 时间戳
    private Date createTime;
    private Date updateTime;

    // 构造函数
    public StudentAbility() {}
    
    public StudentAbility(Long studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    // getter 和 setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public Double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(Double programmingScore) { this.programmingScore = programmingScore; }

    public Double getAiScore() { return aiScore; }
    public void setAiScore(Double aiScore) { this.aiScore = aiScore; }

    public Double getMathScore() { return mathScore; }
    public void setMathScore(Double mathScore) { this.mathScore = mathScore; }

    public Double getCreativityScore() { return creativityScore; }
    public void setCreativityScore(Double creativityScore) { this.creativityScore = creativityScore; }

    public Double getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(Double communicationScore) { this.communicationScore = communicationScore; }

    public String getAbilityType() { return abilityType; }
    public void setAbilityType(String abilityType) { this.abilityType = abilityType; }

    public Double getEloScore() { return eloScore; }
    public void setEloScore(Double eloScore) { this.eloScore = eloScore; }

    public Integer getKFactor() { return kFactor; }
    public void setKFactor(Integer kFactor) { this.kFactor = kFactor; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    /**
     * 计算平均分
     */
    public Double getAverageScore() {
        if (programmingScore == null || aiScore == null || mathScore == null || 
            creativityScore == null || communicationScore == null) {
            return null;
        }
        return (programmingScore + aiScore + mathScore + creativityScore + communicationScore) / 5.0;
    }

    @Override
    public String toString() {
        return "StudentAbility{" +
                "id=" + id +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", programmingScore=" + programmingScore +
                ", aiScore=" + aiScore +
                ", mathScore=" + mathScore +
                ", creativityScore=" + creativityScore +
                ", communicationScore=" + communicationScore +
                ", averageScore=" + getAverageScore() +
                '}';
    }
}