package com.example.ai_lesson.student_courses.mapper;

import com.example.ai_lesson.student_courses.entity.CourseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestMapping;

@Mapper
public interface CourseMapper {

    @Select("select * from course where course_id=#{course_id}")
    CourseEntity getCourseById(int course_id);
}
