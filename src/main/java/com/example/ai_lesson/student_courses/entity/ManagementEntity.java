package com.example.ai_lesson.student_courses.entity;

public class ManagementEntity {
    private String student_id;
    private String name;
    private String course_name;

    public ManagementEntity(String student_id, String name, String course_name) {
        this.student_id = student_id;
        this.name = name;
        this.course_name = course_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
