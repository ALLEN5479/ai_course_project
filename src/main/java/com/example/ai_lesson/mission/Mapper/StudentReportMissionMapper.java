package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.StudentReportMission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentReportMissionMapper {
    @Insert("INSERT INTO student_report_mission (mission_id, student_id, report_name, report_des, report_url) VALUES (#{mission_id}, #{student_id}, #{report_name}, #{report_des}, #{report_url})")
    void insert(StudentReportMission report);
} 