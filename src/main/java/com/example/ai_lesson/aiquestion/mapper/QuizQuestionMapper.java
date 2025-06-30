package com.example.ai_lesson.aiquestion.mapper;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuizQuestionMapper {
    /**
     * 插入新问题
     */
    @Insert("INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer, category, difficulty, created_at) " +
            "VALUES (#{question_text}, #{option_a}, #{option_b}, #{option_c}, #{option_d}, #{correct_answer}, #{category}, #{difficulty}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(QuizQuestion question);

    /**
     * 批量插入问题
     */
    @Insert("<script>" +
            "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer, category, difficulty, created_at) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.question_text}, #{item.option_a}, #{item.option_b}, #{item.option_c}, #{item.option_d}, " +
            "#{item.correct_answer}, #{item.category}, #{item.difficulty}, NOW())" +
            "</foreach>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int batchInsert(List<QuizQuestion> questions);

    /**
     * 根据ID查询问题
     */
    @Select("SELECT * FROM questions WHERE id = #{id}")
    QuizQuestion selectById(Integer id);

    /**
     * 查询所有问题
     */
    @Select("SELECT * FROM questions ORDER BY id DESC")
    List<QuizQuestion> selectAll();

    /**
     * 根据难度查询问题
     */
    @Select("SELECT * FROM questions WHERE difficulty = #{difficulty} ORDER BY created_at DESC")
    List<QuizQuestion> selectByDifficulty(String difficulty);

    /**
     * 更新问题
     */
    @Update("UPDATE questions SET " +
            "question_text = #{question_text}, " +
            "option_a = #{option_a}, " +
            "option_b = #{option_b}, " +
            "option_c = #{option_c}, " +
            "option_d = #{option_d}, " +
            "correct_answer = #{correct_answer}, " +
            "category = #{category}, " +
            "difficulty = #{difficulty} " +
            "WHERE id = #{id}")
    int update(QuizQuestion question);

    /**
     * 根据ID删除问题
     */
    @Delete("DELETE FROM questions WHERE id = #{id}")
    int deleteById(Integer id);
}