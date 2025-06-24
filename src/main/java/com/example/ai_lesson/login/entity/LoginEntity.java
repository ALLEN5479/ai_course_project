package com.example.ai_lesson.login.entity;

public class LoginEntity {
    private int user_id;
    private String name;
    private String password;
    private int type;

    public LoginEntity(int user_id, String name, int type, String password) {
        this.user_id = user_id;
        this.name = name;
        this.type = type;
        this.password = password;
    }

    public LoginEntity() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
