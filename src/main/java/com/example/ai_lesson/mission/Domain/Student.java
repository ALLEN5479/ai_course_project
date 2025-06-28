package com.example.ai_lesson.mission.Domain;

public class Student {
    private int studentId;
    private String studentName;

    public Student() {
    }
    public Student(int studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }
    public static Student getInstance() {
        return new Student();
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
