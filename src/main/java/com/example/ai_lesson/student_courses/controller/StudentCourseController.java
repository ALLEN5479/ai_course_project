package com.example.ai_lesson.student_courses.controller;

import org.springframework.web.bind.annotation.*;
import com.example.ai_lesson.student_courses.entity.StudentCourseEntity;
import com.example.ai_lesson.student_courses.entity.CourseEntity;
import com.example.ai_lesson.student_courses.mapper.StudentCourseMapper;
import com.example.ai_lesson.student_courses.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class StudentCourseController {

    @Autowired
    private StudentCourseMapper studentCourseMapper;
    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/student/courses")
    public List<CourseEntity> getStudentCourses(@RequestParam String user_id) {
        List<StudentCourseEntity> studentCourses = studentCourseMapper.getStudentCoursesByUserId(user_id);
        List<Integer> courseIds = studentCourses.stream().map(StudentCourseEntity::getCourse_id).collect(Collectors.toList());
        if (courseIds.isEmpty()) return List.of();
        return courseMapper.getCoursesByIds(courseIds);
    }

    @RequestMapping( "/student/course/count")
    public int getCourseStudentCount(String teacher_id) {
        return studentCourseMapper.getCourseStudentCount(teacher_id);
    }
}
