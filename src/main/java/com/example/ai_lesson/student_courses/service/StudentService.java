package com.example.ai_lesson.student_courses.service;

import com.example.ai_lesson.student_courses.entity.StudentEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    StudentEntity getProfile(String userId);
    void updateProfile(StudentEntity student);
    String saveAvatar(MultipartFile file);
}
