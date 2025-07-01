package com.example.ai_lesson.student_courses.controller;

import com.example.ai_lesson.student_courses.entity.StudentInfoEntity;
import com.example.ai_lesson.student_courses.service.StudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studentInfo")
public class StudentInfoController {

    @Autowired
    private StudentInfoService studentInfoService;

    @GetMapping("/get")
    public StudentInfoEntity getStudentInfo(@RequestParam String user_id) {
        return studentInfoService.getStudentInfo(user_id);
    }

    @PostMapping("/update")
    public String updateStudentInfo(@RequestBody StudentInfoEntity studentInfo) {
        studentInfoService.saveOrUpdateStudentInfo(studentInfo);
        return "success";
    }
}

