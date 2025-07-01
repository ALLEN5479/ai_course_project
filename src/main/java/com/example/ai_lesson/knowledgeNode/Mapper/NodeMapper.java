package com.example.ai_lesson.knowledgeNode.Mapper;


import com.example.ai_lesson.knowledgeNode.Domain.Node;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NodeMapper {
    @Insert("INSERT INTO knowledge_node(name, description, parent_id, delete_flag, level) " +
            "VALUES(#{name}, #{description}, #{parentId}, #{delete_flag}, #{level})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Node node);

    @Update("UPDATE knowledge_node SET name=#{name}, description=#{description}, " +
            "parent_id=#{parentId}, delete_flag=#{delete_flag}, level=#{level} WHERE id=#{id}")
    int update(Node node);

    @Update("UPDATE knowledge_node SET delete_flag=1 WHERE id=#{id}")
    int deleteById(int id);

    @Select("SELECT kg_id as id, kg_name as name, parent_id as parentId, kg_description as description, course_id, level, sort_order, delete_flag, create_time, update_time FROM knowledge_node WHERE kg_id=#{id} AND delete_flag=0")
    Node selectById(int id);

    @Select("SELECT kg_id as id, kg_name as name, parent_id as parentId, kg_description as description, course_id, level, sort_order, delete_flag, create_time, update_time FROM knowledge_node WHERE parent_id=#{parentId} AND delete_flag=0")
    List<Node> selectByParentId(int parentId);

    @Select("SELECT kg_id as id, kg_name as name, parent_id as parentId, kg_description as description, course_id, level, sort_order, delete_flag, create_time, update_time FROM knowledge_node WHERE delete_flag=0")
    List<Node> selectAll();

    @Select("WITH RECURSIVE node_tree AS (" +
            "  SELECT kg_id as id, kg_name as name, parent_id as parentId, kg_description as description, course_id, level, sort_order, delete_flag, create_time, update_time FROM knowledge_node WHERE kg_id=#{rootId} AND delete_flag=0 " +
            "  UNION ALL " +
            "  SELECT n.kg_id as id, n.kg_name as name, n.parent_id as parentId, n.kg_description as description, n.course_id, n.level, n.sort_order, n.delete_flag, n.create_time, n.update_time FROM knowledge_node n " +
            "  JOIN node_tree nt ON n.parent_id=nt.id WHERE n.delete_flag=0" +
            ") SELECT * FROM node_tree")
    List<Node> selectSubtree(int rootId);
}
