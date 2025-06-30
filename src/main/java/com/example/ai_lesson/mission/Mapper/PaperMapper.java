package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.Paper;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PaperMapper {
    
    @Select("SELECT * FROM papers")
    List<Paper> findAll();
    
    @Select("SELECT * FROM papers WHERE paper_id = #{paperId}")
    Paper findById(Integer paperId);
    
    @Insert("INSERT INTO papers (paper_name, paper_description, total_score) " +
            "VALUES (#{paperName}, #{paperDescription}, #{totalScore})")
    @Options(useGeneratedKeys = true, keyProperty = "paperId")
    int insert(Paper paper);
    
    @Update("UPDATE papers SET paper_name = #{paperName}, paper_description = #{paperDescription}, " +
            "total_score = #{totalScore} WHERE paper_id = #{paperId}")
    int update(Paper paper);
    
    @Delete("DELETE FROM papers WHERE paper_id = #{paperId}")
    int deleteById(Integer paperId);
}
