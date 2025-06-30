package com.example.ai_lesson.ability.Mapper;

import com.example.ai_lesson.ability.Domain.StudentAbility;
import com.example.ai_lesson.aiquestion.entity.QuizAnswerRecord;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentAbilityMapper {
    // 查询某个学生的所有能力分
    List<StudentAbility> selectStudentAbilityByStudentId(Long studentId);
    List<QuizAnswerRecord> selectGrammarRecordsByStudentId(Long studentId);
    int insertQuestionRecord(QuizAnswerRecord record);
    StudentAbility selectStudentAbilityById(Long studentId);
    List<StudentAbility> selectAllStudentsAbility();
    int updateStudentAbility(StudentAbility ability);
    int insertStudentAbility(StudentAbility ability);
    int deleteStudentAbilityByStudentId(Long studentId);
    List<QuizAnswerRecord> selectRecordsByStudentIdAndAbilityType(Long studentId, String abilityType);
    StudentAbility selectStudentAbilityByStudentIdAndAbilityType(Long studentId, String abilityType);
}