package com.example.ai_lesson.student_courses.controller;

import com.example.ai_lesson.student_courses.entity.CourseEntity;
import com.example.ai_lesson.student_courses.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CourseController {

    @Autowired
    private CourseMapper courseMapper;

    @RequestMapping("/getCourse")
    public CourseEntity getCourse(int course_id){
        return courseMapper.getCourseById(course_id);
    }

}
