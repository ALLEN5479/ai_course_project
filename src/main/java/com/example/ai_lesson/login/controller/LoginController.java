package com.example.ai_lesson.login.controller;

import com.example.ai_lesson.login.entity.LoginEntity;
import com.example.ai_lesson.login.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginMapper loginMapper;

    @RequestMapping("/login")
    public LoginEntity login(int user_id, String password){
        return loginMapper.login(user_id, password);
    }

    @RequestMapping("/register")
    public boolean register(@RequestBody LoginEntity loginEntity){
        return loginMapper.register(loginEntity)==1;
    }
}
