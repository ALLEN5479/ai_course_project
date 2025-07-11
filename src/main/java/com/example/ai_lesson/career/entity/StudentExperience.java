package com.example.ai_lesson.career.entity;

public class StudentExperience {
    private String studentId;    // 学号
    private String name;         // 姓名
    private String className;    // 班级
    private String experience1;  // 经历1
    private String experience2;  // 经历2
    private String experience3;  // 经历3

    public StudentExperience() {}

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExperience1() {
        return experience1;
    }

    public void setExperience1(String experience1) {
        this.experience1 = experience1;
    }

    public String getExperience2() {
        return experience2;
    }

    public void setExperience2(String experience2) {
        this.experience2 = experience2;
    }

    public String getExperience3() {
        return experience3;
    }

    public void setExperience3(String experience3) {
        this.experience3 = experience3;
    }
}
