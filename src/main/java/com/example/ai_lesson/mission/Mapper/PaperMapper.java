package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.Paper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaperMapper {
    
    @Select("SELECT paper_id as paperId, paper_name as paperName, paper_description as paperDescription, total_score as totalScore, course_id as courseId FROM papers")
    List<Paper> findAll();
    
    @Select("SELECT paper_id as paperId, paper_name as paperName, paper_description as paperDescription, total_score as totalScore, course_id as courseId FROM papers WHERE paper_id = #{paperId}")
    Paper findById(Integer paperId);
    
    @Insert("INSERT INTO papers (paper_name, paper_description, total_score, course_id) " +
            "VALUES (#{paperName}, #{paperDescription}, #{totalScore}, #{courseId})")
    @Options(useGeneratedKeys = true, keyProperty = "paperId")
    int insert(Paper paper);
    
    @Update("UPDATE papers SET paper_name = #{paperName}, paper_description = #{paperDescription}, " +
            "total_score = #{totalScore}, course_id = #{courseId} WHERE paper_id = #{paperId}")
    int update(Paper paper);
    
    @Delete("DELETE FROM papers WHERE paper_id = #{paperId}")
    int deleteById(Integer paperId);
    
    @Select("SELECT paper_id as paperId, paper_name as paperName, paper_description as paperDescription, total_score as totalScore, course_id as courseId FROM papers WHERE course_id = #{courseId}")
    List<Paper> findByCourseId(Integer courseId);
}
