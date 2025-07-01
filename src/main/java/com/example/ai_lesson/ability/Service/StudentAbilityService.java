package com.example.ai_lesson.ability.Service;

import com.example.ai_lesson.ability.Domain.StudentAbility;
import com.example.ai_lesson.aiquestion.entity.QuizAnswerRecord;
import java.util.List;

public interface StudentAbilityService {
    
    /**
     * 根据学生ID获取能力数据列表
     */
    List<StudentAbility> getByStudentId(Long studentId);
    
    /**
     * 获取学生语法ELO分数
     */
    Double getGrammarEloByStudentId(Long studentId);
    
    /**
     * 添加问题记录
     */
    void addQuestionRecord(QuizAnswerRecord record);
    
    /**
     * 获取学生能力图谱数据
     */
    StudentAbility getStudentAbility(Long studentId);
    
    /**
     * 获取所有学生的能力数据
     */
    List<StudentAbility> getAllStudentsAbility();
    
    /**
     * 更新学生能力数据
     */
    void updateStudentAbility(StudentAbility ability);
    
    /**
     * 创建学生能力数据
     */
    void createStudentAbility(StudentAbility ability);
    
    /**
     * 根据学生ID删除能力数据
     */
    void deleteStudentAbility(Long studentId);
    
    /**
     * 批量更新学生能力数据
     */
    void batchUpdateStudentAbility(List<StudentAbility> abilities);
    
    /**
     * 获取某学生某能力点的ELO分数
     */
    Double getEloByStudentIdAndAbilityType(Long studentId, String abilityType);
}