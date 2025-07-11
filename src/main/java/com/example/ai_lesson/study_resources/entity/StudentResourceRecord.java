package com.example.ai_lesson.study_resources.entity;

public class StudentResourceRecord {
    private String student_id;
    private int resource_id;
    private Double actual_learning_time;
    private Integer jump_time;

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public Double getActual_learning_time() {
        return actual_learning_time;
    }

    public void setActual_learning_time(Double actual_learning_time) {
        this.actual_learning_time = actual_learning_time;
    }

    public Integer getJump_time() {
        return jump_time;
    }

    public void setJump_time(Integer jump_time) {
        this.jump_time = jump_time;
    }
}