package com.example.ai_lesson.mission.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
 
@Mapper
public interface MissionStudentScoreMapper {
    @Insert("INSERT INTO mission_student_score (mission_id, student_id, score) VALUES (#{missionId}, #{studentId}, #{score}) ON DUPLICATE KEY UPDATE score = #{score}")
    int insertScore(@Param("missionId") Integer missionId, @Param("studentId") String studentId, @Param("score") Integer score);
} 