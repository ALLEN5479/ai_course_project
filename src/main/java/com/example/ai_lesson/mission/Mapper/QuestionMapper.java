package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    // 根据ID获取单个题目
    @Select("SELECT * FROM question WHERE question_id = #{questionId}")
    Question getQuestionById(int questionId);

    // 根据试卷ID获取所有题目（组卷核心逻辑）
    @Select("SELECT q.question_id as questionId,q.selection_A as sectionA,q.selection_B as sectionB,q.selection_C as sectionC,q.selection_D as sectionD,answer,question_name as questionName FROM question q JOIN paper_question pq ON q.question_id = pq.question_id WHERE pq.paper_id = #{paperId}")
    List<Question> getQuestionsByPaperId(int paperId);

    // 新增题目
    @Insert("INSERT INTO question(question_name, section_A, section_B, section_C, section_D, answer) " +
            "VALUES(#{questionName}, #{sectionA}, #{sectionB}, #{sectionC}, #{sectionD}, #{answer})")
    @Options(useGeneratedKeys = true, keyProperty = "question_id")
    int insertQuestion(Question question);

    // 更新题目
    @Update("UPDATE question SET question_name=#{questionName}, section_A=#{sectionA}, section_B=#{sectionB}, " +
            "section_C=#{sectionC}, section_D=#{sectionD}, answer=#{answer} WHERE question_id=#{questionId}")
    int updateQuestion(Question question);

    // 删除题目
    @Delete("DELETE FROM question WHERE question_id=#{questionId}")
    int deleteQuestion(int questionId);

    // 添加题目到试卷
    @Insert("INSERT INTO paper_question(paper_id, question_id) VALUES(#{paperId}, #{questionId})")
    int addQuestionToPaper(@Param("paperId") int paperId, @Param("questionId") int questionId);

    // 从试卷移除题目
    @Delete("DELETE FROM paper_question WHERE paper_id=#{paperId} AND question_id=#{questionId}")
    int removeQuestionFromPaper(@Param("paperId") int paperId, @Param("questionId") int questionId);
}