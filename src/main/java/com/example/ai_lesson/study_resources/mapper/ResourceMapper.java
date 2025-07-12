package com.example.ai_lesson.study_resources.mapper;

import com.example.ai_lesson.study_resources.entity.ResourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResourceMapper {

    @Select("select * from study_resources where resource_id=#{resource_id}")
    List<ResourceEntity> getResources(int resource_id);

    @Select("select * from study_resources")
    List<ResourceEntity> getAllResources();
}
