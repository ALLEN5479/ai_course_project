package com.example.ai_lesson.mission.Domain;

import java.util.List;

public class ClassInfo {
    private int classId;
    private int teacherId;
    private String className;
    private int totalStudents;
    private List<Mission> missions;
    private List<Student> students;
    /*@JsonProperty("completedNumber")
    private int completedNumber;

    @JsonProperty("uncompletedCount")
    private int uncompletedCount;*/
    public ClassInfo() {
        // 无参构造函数
    }
    // getters and setters
    public ClassInfo(int classId, String className, int totalStudents, List<Mission> missions) {
        this.classId = classId;
        this.className = className;
        this.totalStudents = totalStudents;
        this.missions = missions;
    }
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }
   /* public int getCompletedNumber() {
        return completedNumber;
    }

    public void setCompletedNumber(int completedNumber) {
        this.completedNumber = completedNumber;
    }

    public int getUncompletedCount() {
        return uncompletedCount;
    }

    public void setUncompletedCount(int uncompletedNumber) {
        this.uncompletedCount = uncompletedCount;
    }*/
    @Override
    public String toString() {
        return "ClassInfo{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", totalStudents=" + totalStudents +
                ", missions=" + (missions != null ? missions.size() : 0) + " missions" +
                //", completedNumber=" + completedNumber +
                //", uncompletedCount=" + uncompletedCount +
                '}';
    }
}