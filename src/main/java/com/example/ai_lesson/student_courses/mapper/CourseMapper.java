package com.example.ai_lesson.student_courses.mapper;

import com.example.ai_lesson.student_courses.entity.CourseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseMapper {

    // 单个查询
    @Select("select * from course where course_id=#{course_id}")
    CourseEntity getCourseById(int course_id);

    // 批量查询
    @Select({
        "<script>",
        "select * from course where course_id in",
        "<foreach item='item' index='index' collection='courseIds' open='(' separator=',' close=')'>",
        "#{item}",
        "</foreach>",
        "</script>"
    })
    List<CourseEntity> getCoursesByIds(@Param("courseIds") List<Integer> courseIds);

    @Select("select name from user_msg where user_id=#{teacher_id}")
    String getTeacherNameById(@Param("teacher_id") String teacher_id);
}
