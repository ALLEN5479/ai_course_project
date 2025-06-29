package com.example.ai_lesson.student_courses.controller;

import com.example.ai_lesson.student_courses.entity.ManagementEntity;
import com.example.ai_lesson.student_courses.mapper.ManagementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ManagementController {

    @Autowired
    private ManagementMapper managementMapper;

    @RequestMapping("/getStudentNameAndId")
    public List<ManagementEntity> getStudentNameAndId(String teacher_id) {
        return managementMapper.getStudentNameAndId(teacher_id);
    }


}
