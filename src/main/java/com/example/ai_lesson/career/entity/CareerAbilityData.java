package com.example.ai_lesson.career.entity;

public class CareerAbilityData {
    private String student_id;
    private String name;
    private String class_name;
    private Integer course_score;
    private Integer practice_score;
    private Integer quality_score;
    private Integer innovation_score;
    private Integer teamwork_score;
    private Integer communication_score;
    private Integer total_score;
    private Integer rank;
    private String ai_report; // AI能力报告文本
    // getter/setter
    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClass_name() { return class_name; }
    public void setClass_name(String class_name) { this.class_name = class_name; }

    public Integer getCourse_score() { return course_score; }
    public void setCourse_score(Integer course_score) { this.course_score = course_score; }

    public Integer getPractice_score() { return practice_score; }
    public void setPractice_score(Integer practice_score) { this.practice_score = practice_score; }

    public Integer getQuality_score() { return quality_score; }
    public void setQuality_score(Integer quality_score) { this.quality_score = quality_score; }

    public Integer getInnovation_score() { return innovation_score; }
    public void setInnovation_score(Integer innovation_score) { this.innovation_score = innovation_score; }

    public Integer getTeamwork_score() { return teamwork_score; }
    public void setTeamwork_score(Integer teamwork_score) { this.teamwork_score = teamwork_score; }

    public Integer getCommunication_score() { return communication_score; }
    public void setCommunication_score(Integer communication_score) { this.communication_score = communication_score; }

    public Integer getTotal_score() { return total_score; }
    public void setTotal_score(Integer total_score) { this.total_score = total_score; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public String getAi_report() { return ai_report; }
    public void setAi_report(String ai_report) { this.ai_report = ai_report; }
}
