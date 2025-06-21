package com.example.ai_lesson.knowledgeNode.Domain;

public class Node {
    private int id;
    private String name;
    private String description;
    private int parentId;
    private int delete_flag;//0:正常，1:删除
    private int level;
    public Node(){}
    public Node(int id, String name, String description, int parentId, int delete_flag, int level) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.delete_flag = delete_flag;
        this.level = level;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public int getDelete_flag() {
        return delete_flag;
    }
    public void setDelete_flag(int delete_flag) {
        this.delete_flag = delete_flag;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public String toString() {
        return "node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parentId=" + parentId +
                ", delete_flag=" + delete_flag +
                ", level=" + level +
                '}';
        }

}
