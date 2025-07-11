package com.example.ai_lesson.student_courses.entity;

public class StudentEntity {
    private String student_id;
    private String name;

    public StudentEntity(String student_id, String name) {
        this.student_id = student_id;
        this.name = name;
    }

    public StudentEntity() {
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
}
