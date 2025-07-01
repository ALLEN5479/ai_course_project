package com.example.ai_lesson.login.controller;

import com.example.ai_lesson.login.entity.LoginEntity;
import com.example.ai_lesson.login.mapper.LoginMapper;
import com.example.ai_lesson.common.AjaxResult;
import com.example.ai_lesson.student_courses.mapper.StudentCourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @RequestMapping("/login")
    public AjaxResult login(@RequestParam String user_id, @RequestParam String password){
        LoginEntity entity = loginMapper.login(user_id, password);
        if (entity != null) {
            return AjaxResult.success(entity);
        } else {
            return AjaxResult.error("用户名或密码错误");
        }
    }

    @RequestMapping("/register")
    public boolean register(@RequestBody LoginEntity loginEntity){
        return loginMapper.register(loginEntity)==1;
    }
}
