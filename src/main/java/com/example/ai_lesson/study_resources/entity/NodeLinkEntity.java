package com.example.ai_lesson.study_resources.entity;

public class NodeLinkEntity {
    private int node_id;
    private int resource_id;

    public NodeLinkEntity(int node_id, int resource_id) {
        this.node_id = node_id;
        this.resource_id = resource_id;
    }

    public int getNode_id() {
        return node_id;
    }

    public void setNode_id(int node_id) {
        this.node_id = node_id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }
}
