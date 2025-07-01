package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.PaperQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaperQuestionMapper {
    
    @Select("SELECT * FROM paper_question WHERE paper_id = #{paperId} ORDER BY sort_order")
    List<PaperQuestion> findByPaperId(Integer paperId);
    
    @Select("SELECT * FROM paper_question WHERE id = #{id}")
    PaperQuestion findById(Integer id);
    
    @Insert("INSERT INTO paper_question (paper_id, question_id, sort_order, score, question_type) " +
            "VALUES (#{paperId}, #{questionId}, #{sortOrder}, #{score}, #{questionType})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PaperQuestion paperQuestion);
    
    @Update("UPDATE paper_question SET sort_order = #{sortOrder}, score = #{score}, question_type = #{questionType} " +
            "WHERE id = #{id}")
    int update(PaperQuestion paperQuestion);
    
    @Delete("DELETE FROM paper_question WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Delete("DELETE FROM paper_question WHERE paper_id = #{paperId}")
    int deleteByPaperId(Integer paperId);
    
    @Insert("<script>" +
            "INSERT INTO paper_question (paper_id, question_id, sort_order, score, question_type) VALUES " +
            "<foreach collection='paperQuestions' item='item' separator=','>" +
            "(#{item.paperId}, #{item.questionId}, #{item.sortOrder}, #{item.score}, #{item.questionType})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("paperQuestions") List<PaperQuestion> paperQuestions);
    
    @Select("SELECT q.id, q.question_text as content, q.option_a, q.option_b, q.option_c, q.option_d, q.category, q.difficulty, pq.score, pq.question_type as type FROM questions q JOIN paper_question pq ON q.id = pq.question_id WHERE q.id = #{questionId} LIMIT 1")
    Map<String, Object> findQuestionDetailById(Integer questionId);
    
    @Select({
      "<script>",
      "SELECT q.id, q.question_text as content, q.option_a, q.option_b, q.option_c, q.option_d, q.category, q.difficulty, pq.score, pq.question_type as type",
      "FROM questions q",
      "JOIN paper_question pq ON q.id = pq.question_id",
      "WHERE pq.paper_id = #{paperId}",
      "ORDER BY pq.sort_order",
      "</script>"
    })
    List<Map<String, Object>> findQuestionDetailsByPaperId(@Param("paperId") Integer paperId);
} 