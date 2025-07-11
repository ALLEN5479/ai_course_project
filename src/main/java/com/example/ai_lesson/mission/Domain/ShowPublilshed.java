package com.example.ai_lesson.mission.Domain;

import java.time.LocalDateTime;

public class ShowPublilshed {
    private int mission_id;//任务id
    private String mission_name;//任务名称
    private String mission_description;//任务描述
    private String mission_type;//任务类型
    private LocalDateTime start_time;//任务开始时间
    private LocalDateTime end_time;//任务结束时间

    public ShowPublilshed(int mission_id, String mission_name, String mission_description, String mission_type, LocalDateTime start_time, LocalDateTime end_time) {
        this.mission_id = mission_id;
        this.mission_name = mission_name;
        this.mission_description = mission_description;
        this.mission_type = mission_type;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public String getMission_name() {
        return mission_name;
    }

    public void setMission_name(String mission_name) {
        this.mission_name = mission_name;
    }

    public String getMission_description() {
        return mission_description;
    }

    public void setMission_description(String mission_description) {
        this.mission_description = mission_description;
    }

    public String getMission_type() {
        return mission_type;
    }

    public void setMission_type(String mission_type) {
        this.mission_type = mission_type;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }
}
