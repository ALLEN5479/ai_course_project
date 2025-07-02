package com.example.ai_lesson.mission.Domain;

public class MissionResource {
    private int resource_id;
    private String path;
    private String resource_name;
    private String resource_description;

    public int getResource_id() {
        return resource_id;
    }
    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getResource_name() {
        return resource_name;
    }
    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }
    public String getResource_description() {
        return resource_description;
    }
    public void setResource_description(String resource_description) {
        this.resource_description = resource_description;
    }
} 