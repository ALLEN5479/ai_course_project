package com.example.ai_lesson.mission.Domain;

public class ReportResource {
    private String path;
    private String resource_name;

    public ReportResource(String path, String resource_name) {
        this.path = path;
        this.resource_name = resource_name;
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
}
