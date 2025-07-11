package com.example.ai_lesson.student_courses.service;

import com.example.ai_lesson.student_courses.entity.StudentInfoEntity;
import com.example.ai_lesson.student_courses.mapper.StudentInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentInfoService {

    @Autowired
    private StudentInfoMapper studentInfoMapper;

    public StudentInfoEntity getStudentInfo(String user_id) {
        return studentInfoMapper.selectByUserId(user_id);
    }

    public void saveOrUpdateStudentInfo(StudentInfoEntity studentInfo) {
        if (studentInfo == null) {
            throw new IllegalArgumentException("studentInfo不能为null");
        }
        // 如果已存在则更新，否则插入
        if (studentInfoMapper.selectByUserId(studentInfo.getUser_id()) != null) {
            studentInfoMapper.updateStudentInfo(studentInfo);
        } else {
            studentInfoMapper.insertStudentInfo(studentInfo);
        }
    }
}
