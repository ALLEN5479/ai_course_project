package com.example.ai_lesson.student_courses.mapper;

import com.example.ai_lesson.student_courses.entity.StudentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {
    StudentEntity selectById(String userId);
    void updateStudent(StudentEntity student);
    void saveAvatar(String userId, String avatarUrl);
}
