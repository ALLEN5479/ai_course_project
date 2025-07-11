package com.example.ai_lesson.study_resources.entity;

public class ResourceEntity {

    private int resource_id;
    private String resource_name;
    private String resource_url;
    private String res_description;
    private String res_type;

    public ResourceEntity() {
    }

    public ResourceEntity(int resource_id, String resource_name, String resource_url, String res_description, String res_type) {
        this.resource_id = resource_id;
        this.resource_name = resource_name;
        this.resource_url = resource_url;
        this.res_description = res_description;
        this.res_type = res_type;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public String getRes_description() {
        return res_description;
    }

    public void setRes_description(String res_description) {
        this.res_description = res_description;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }
}
