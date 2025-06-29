package com.example.ai_lesson.student_courses.mapper;

import com.example.ai_lesson.student_courses.entity.ManagementEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ManagementMapper {

    @Select("select slc.user_id,um.name,c.course_name " +
            "from course c " +
            "join student_link_course slc " +
            "on c.course_id=slc.course_id " +
            "join user_msg um on slc.user_id=um.user_id " +
            "where c.teacher_id=#{teacher_id}")
    List<ManagementEntity> getStudentNameAndId(String teacher_id);
}
