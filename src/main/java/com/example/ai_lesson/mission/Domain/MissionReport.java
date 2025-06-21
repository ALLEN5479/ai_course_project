package com.example.ai_lesson.mission.Domain;

public class MissionReport {
    private Integer id;         // 对应数据库的int类型
    private Integer missionId;  // 对应数据库的int类型
    private String path;       // 对应varchar(255) not null
    private String fileName;   // 对应varchar(255) not null

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public String toString() {
        return "MissionReport{" +
                "id=" + id +
                ", missionId=" + missionId +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}