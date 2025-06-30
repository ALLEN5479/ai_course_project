package com.example.ai_lesson.mission.Domain;

import java.time.LocalDateTime;

public class MissionTemplate {
    private Integer missionId;
    private String missionName;
    private String missionDescription;
    private String content;
    private LocalDateTime createTime;
    private Integer teacherId;
    private String missionType;
    private Integer status;

    // 构造函数
    public MissionTemplate() {}

    public MissionTemplate(String missionName, String missionDescription, String content,
                           Integer teacherId, String missionType) {
        this.missionName = missionName;
        this.missionDescription = missionDescription;
        this.content = content;
        this.teacherId = teacherId;
        this.missionType = missionType;
        this.status = 1;
    }

    // Getter和Setter方法
    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getMissionDescription() {
        return missionDescription;
    }

    public void setMissionDescription(String missionDescription) {
        this.missionDescription = missionDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}