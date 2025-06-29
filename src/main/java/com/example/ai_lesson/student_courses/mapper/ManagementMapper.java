package com.example.ai_lesson.student_courses.mapper;

import com.example.ai_lesson.student_courses.entity.ManagementEntity;
import com.example.ai_lesson.student_courses.entity.StudentEntity;
import org.apache.ibatis.annotations.Delete;
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

    @Delete("delete from student_link_course " +
            "where course_id=(select course_id from course where course_name=#{course_name}) " +
            "and user_id=#{user_id}")
    int deleteStudentCourse(String user_id, String course_name);

    @Select("select * from user_msg where type=1")
    List<StudentEntity> getstuMsg();
}
