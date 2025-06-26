package com.example.ai_lesson.mission.Domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Mission {
    @JsonProperty("missionId")
    private int missionId;
    @JsonProperty("missionName")
    private String missionName;
    @JsonProperty("missionDescription")
    private String missionDescription;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String start_time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String end_time;
    @JsonProperty("content")
    private String content;
    @JsonProperty("missionType")
    private String missionType;
    @JsonProperty("teachingClass")
    private String teachingClass;
   /*@JsonProperty("completedNumber")
    private Integer completedNumber;*/
    @JsonProperty("state")
    private Integer state;//0未发布 1未批改 2已批改
    @JsonProperty("teacherId")
    private Integer teacherId;

    public Mission() {}
    public Mission (int missionId, String missionName, String missionDescription, String start_time,  String end_time, String content, String missionType, String teachingClass) {
        this.missionId = missionId;
        this.missionName = missionName;
        this.missionDescription = missionDescription;
        this.start_time = start_time;
        this.end_time = end_time;
        this.content = content;
        this.missionType = missionType;
        this.teachingClass = teachingClass;
    }
    public int getMissionId() {
        return missionId;
    }

    public String getContent() {
        return content;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getMissionDescription() {
        return missionDescription;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getMissionType() {
        return missionType;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getTeachingClass() {
        return teachingClass;
    }

    public void setMissionDescription(String missionDescription) {
        this.missionDescription = missionDescription;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setTeachingClass(String teachingClass) {
        this.teachingClass = teachingClass;
    }
    /*public Integer getCompletedNumber() {
        return completedNumber;
    }*/
    /*public void setCompletedNumber(Integer completedNumber) {
        this.completedNumber = completedNumber;
    }*/
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public Integer getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
    public static Mission getInstance() {
        return new Mission();
    }

}
