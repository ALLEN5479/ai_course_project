package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.MissionTemplate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MissionTemplateMapper {
    
    @Select("SELECT mission_id as missionId, mission_name as missionName, mission_description as missionDescription, " +
            "content, create_time as createTime, teacher_id as teacherId, mission_type as missionType, status " +
            "FROM mission_template WHERE status = 1 ORDER BY create_time DESC")
    List<MissionTemplate> findAllActive();
    
    @Select("SELECT mission_id as missionId, mission_name as missionName, mission_description as missionDescription, " +
            "content, create_time as createTime, teacher_id as teacherId, mission_type as missionType, status " +
            "FROM mission_template WHERE mission_id = #{missionId}")
    MissionTemplate findById(Integer missionId);
    
    @Select("SELECT mission_id as missionId, mission_name as missionName, mission_description as missionDescription, " +
            "content, create_time as createTime, teacher_id as teacherId, mission_type as missionType, status " +
            "FROM mission_template WHERE mission_type = #{missionType} AND status = 1 ORDER BY create_time DESC")
    List<MissionTemplate> findByType(String missionType);
    
    @Select("SELECT mission_id as missionId, mission_name as missionName, mission_description as missionDescription, " +
            "content, create_time as createTime, teacher_id as teacherId, mission_type as missionType, status " +
            "FROM mission_template WHERE teacher_id = #{teacherId} AND status = 1 ORDER BY create_time DESC")
    List<MissionTemplate> findByTeacherId(Integer teacherId);
    
    @Insert("INSERT INTO mission_template (mission_name, mission_description, content, create_time, teacher_id, mission_type, status) " +
            "VALUES (#{missionName}, #{missionDescription}, #{content}, #{createTime}, #{teacherId}, #{missionType}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "missionId")
    int insert(MissionTemplate missionTemplate);
    
    @Update("UPDATE mission_template SET mission_name = #{missionName}, mission_description = #{missionDescription}, " +
            "content = #{content}, teacher_id = #{teacherId}, mission_type = #{missionType} " +
            "WHERE mission_id = #{missionId}")
    int update(MissionTemplate missionTemplate);
    
    @Update("UPDATE mission_template SET status = #{status} WHERE mission_id = #{missionId}")
    int updateStatus(@Param("missionId") Integer missionId, @Param("status") Integer status);
    
    @Delete("DELETE FROM mission_template WHERE mission_id = #{missionId}")
    int deleteById(Integer missionId);
} 