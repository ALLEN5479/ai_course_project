package com.example.ai_lesson.aiquestion.mapper;

import com.example.ai_lesson.aiquestion.entity.QuizAnswerRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuizAnswerRecordMapper {
    int insert(QuizAnswerRecord record);
    List<QuizAnswerRecord> selectByStudentId(Long studentId);
    List<QuizAnswerRecord> selectByStudentIdAndAbilityType(Long studentId, String abilityType);
    // 其他需要的查询
}
