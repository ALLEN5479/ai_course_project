package com.example.ai_lesson.student_courses.mapper;

import com.example.ai_lesson.student_courses.entity.StudentCourseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface StudentCourseMapper {
    //查询一个学生名下的所有课程
    @Select("select * from student_link_course where user_id=#{user_id}")
    List<StudentCourseEntity> getStudentCoursesByUserId(String user_id);
}
