package com.example.ai_lesson.study_resources.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import com.example.ai_lesson.study_resources.entity.StudentResourceRecord;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface RecordMapper {

    @Insert("INSERT INTO student_resource_record (student_id, resource_id, actual_learning_time, jump_time) " +
            "VALUES (#{student_id}, #{resource_id}, #{actual_learning_time}, #{jump_time}) " +
            "ON DUPLICATE KEY UPDATE " +
            "actual_learning_time = student_resource_record.actual_learning_time + VALUES(actual_learning_time)," +
            "jump_time = student_resource_record.jump_time + VALUES(jump_time)")
    int updateRecord(String student_id, int resource_id, double actual_learning_time, int jump_time);

    @Select("SELECT * FROM student_resource_record WHERE student_id = #{studentId}")
    List<StudentResourceRecord> selectByStudentId(String studentId);
}
