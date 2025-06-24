package com.example.ai_lesson.student_courses.entity;

public class CourseEntity {
    private int course_id;
    private String course_name;
    private int teacher_id;
    private String description;

    public CourseEntity(int course_id, String course_name, int teacher_id, String description) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.teacher_id = teacher_id;
        this.description = description;
    }

    public CourseEntity() {
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
