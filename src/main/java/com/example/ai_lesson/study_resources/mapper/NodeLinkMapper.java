package com.example.ai_lesson.study_resources.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NodeLinkMapper {

    @Select("SELECT resource_id FROM node_resource WHERE node_id = #{nodeId}")
    List<Integer> getResourceId(int nodeId);
}
