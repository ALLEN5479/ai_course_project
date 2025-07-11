package com.example.ai_lesson.study_resources.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NodeLinkMapper {

    @Select("SELECT resource_id FROM node_resource WHERE node_id = #{nodeId}")
    List<Integer> getResourceId(int nodeId);
    
    @Insert("INSERT INTO node_resource (node_id, resource_id) VALUES (#{node_id}, #{resource_id})")
    int insertNodeResource(int node_id, int resource_id);
}
