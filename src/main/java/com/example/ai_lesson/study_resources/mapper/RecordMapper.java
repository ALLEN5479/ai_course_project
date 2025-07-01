package com.example.ai_lesson.study_resources.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RecordMapper {

    @Insert("INSERT INTO student_resource_record (student_id, resource_id, actual_learning_time, jump_time) " +
            "VALUES (#{student_id}, #{resource_id}, #{actual_learning_time}, #{jump_time}) " +
            "ON DUPLICATE KEY UPDATE " +
            "actual_learning_time = student_resource_record.actual_learning_time + VALUES(actual_learning_time)," +
            "jump_time = student_resource_record.jump_time + VALUES(jump_time)")
    int updateRecord(String student_id, int resource_id, double actual_learning_time, int jump_time);
}
