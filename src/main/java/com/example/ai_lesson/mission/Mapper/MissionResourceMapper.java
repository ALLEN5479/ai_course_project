package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.MissionResource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MissionResourceMapper {
    @Insert("INSERT INTO mission_resource (path, resource_name, resource_description) VALUES (#{path}, #{resource_name}, #{resource_description})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "resource_id")
    int insertMissionResource(MissionResource resource);
} 