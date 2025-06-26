package com.example.ai_lesson.student_courses.entity;

public class StudentCourseEntity {
    private String user_id;
    private String stu_name;
    private int course_id;
    private int study_time;

    public StudentCourseEntity(String user_id, String stu_name, int course_id, int study_time) {
        this.user_id = user_id;
        this.stu_name = stu_name;
        this.course_id = course_id;
        this.study_time = study_time;
    }

    public StudentCourseEntity() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getStudy_time() {
        return study_time;
    }

    public void setStudy_time(int study_time) {
        this.study_time = study_time;
    }
}
