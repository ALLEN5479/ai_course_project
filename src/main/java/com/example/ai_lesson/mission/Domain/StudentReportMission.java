package com.example.ai_lesson.mission.Domain;

public class StudentReportMission {
    private Integer mission_id;
    private String student_id;
    private String report_name;
    private String report_des;
    private String report_url;

    public Integer getMission_id() { return mission_id; }
    public void setMission_id(Integer mission_id) { this.mission_id = mission_id; }
    public String getStudent_id() { return student_id; }
    public void setStudent_id(String student_id) { this.student_id = student_id; }
    public String getReport_name() { return report_name; }
    public void setReport_name(String report_name) { this.report_name = report_name; }
    public String getReport_des() { return report_des; }
    public void setReport_des(String report_des) { this.report_des = report_des; }
    public String getReport_url() { return report_url; }
    public void setReport_url(String report_url) { this.report_url = report_url; }
} 