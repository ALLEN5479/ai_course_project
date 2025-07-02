package com.example.ai_lesson.student_courses.mapper;

import com.example.ai_lesson.student_courses.entity.StudentInfoEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentInfoMapper {

    @Select("SELECT * FROM student_info WHERE user_id = #{user_id}")
    StudentInfoEntity selectByUserId(String user_id);

    @Insert("INSERT INTO student_info (user_id, gender, grade, class_name, major, school) " +
            "VALUES (#{user_id}, #{gender}, #{grade}, #{class_name}, #{major}, #{school})")
    void insertStudentInfo(StudentInfoEntity studentInfo);

    @Update("UPDATE student_info SET gender=#{gender}, grade=#{grade}, class_name=#{class_name}, major=#{major}, school=#{school} WHERE user_id=#{user_id}")
    void updateStudentInfo(StudentInfoEntity studentInfo);
}
