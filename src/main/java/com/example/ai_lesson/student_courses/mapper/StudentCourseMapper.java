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

    @Select("SELECT COUNT(DISTINCT slc.user_id) AS unique_student_count " +
            "FROM student_link_course slc " +
            "JOIN course c ON slc.course_id = c.course_id " +
            "WHERE c.teacher_id = #{teacher_id};")
    int getCourseStudentCount(String teacher_id);
}
