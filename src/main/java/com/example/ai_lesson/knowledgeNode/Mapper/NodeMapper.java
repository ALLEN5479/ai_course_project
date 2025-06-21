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

    @Select("SELECT * FROM knowledge_node WHERE id=#{id} AND delete_flag=0")
    Node selectById(int id);

    @Select("SELECT * FROM knowledge_node WHERE parent_id=#{parentId} AND delete_flag=0")
    List<Node> selectByParentId(int parentId);

    @Select("SELECT * FROM knowledge_node WHERE delete_flag=0")
    List<Node> selectAll();

    @Select("WITH RECURSIVE node_tree AS (" +
            "  SELECT * FROM knowledge_node WHERE id=#{rootId} AND delete_flag=0 " +
            "  UNION ALL " +
            "  SELECT n.* FROM knowledge_node n " +
            "  JOIN node_tree nt ON n.parent_id=nt.id WHERE n.delete_flag=0" +
            ") SELECT * FROM node_tree")
    List<Node> selectSubtree(int rootId);
}
