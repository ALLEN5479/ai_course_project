package com.example.ai_lesson.ability.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ai_lesson.ability.Domain.StudentAbility;
import com.example.ai_lesson.aiquestion.entity.QuizAnswerRecord;
import com.example.ai_lesson.ability.Mapper.StudentAbilityMapper;
import com.example.ai_lesson.ability.Service.StudentAbilityService;

import java.util.List;
import java.util.Date;

@Service
public class StudentAbilityServiceImpl implements StudentAbilityService {
    @Autowired
    private StudentAbilityMapper studentAbilityMapper;

    @Override
    public List<StudentAbility> getByStudentId(Long studentId) {
        return studentAbilityMapper.selectStudentAbilityByStudentId(studentId);
    }

    @Override
    public Double getGrammarEloByStudentId(Long studentId) {
        List<QuizAnswerRecord> records = studentAbilityMapper.selectGrammarRecordsByStudentId(studentId);

        System.out.println("records: " + records);

        double theta = 1500; // 初始分
        int k = 16; // 语法题K值

        for (QuizAnswerRecord r : records) {
            if (r == null) {
                System.out.println("记录为null，已跳过");
                continue;
            }
            Double beta = r.getDifficulty();
            if (beta == null) {
                System.out.println("题目难度为null，questionId=" + r.getQuestionId());
                continue;
            }
            Boolean correct = r.getIsCorrect();
            double S = (correct != null && correct) ? 1.0 : 0.0;
            double E = 1.0 / (1 + Math.pow(10, (beta - theta) / 400.0));
            System.out.println("theta=" + theta + ", beta=" + beta + ", S=" + S + ", E=" + E);
            theta = theta + k * (S - E);
            theta = Math.min(theta, 2500);
        }
        System.out.println("最终返回theta=" + theta);
        return theta;
    }

    @Override
    public void addQuestionRecord(QuizAnswerRecord record) {
        studentAbilityMapper.insertQuestionRecord(record);
    }

    @Override
    public StudentAbility getStudentAbility(Long studentId) {
        return studentAbilityMapper.selectStudentAbilityById(studentId);
    }

    @Override
    public List<StudentAbility> getAllStudentsAbility() {
        return studentAbilityMapper.selectAllStudentsAbility();
    }

    @Override
    public void updateStudentAbility(StudentAbility ability) {
        ability.setUpdateTime(new Date());
        studentAbilityMapper.updateStudentAbility(ability);
    }

    @Override
    public void createStudentAbility(StudentAbility ability) {
        ability.setCreateTime(new Date());
        ability.setUpdateTime(new Date());
        studentAbilityMapper.insertStudentAbility(ability);
    }

    @Override
    public void deleteStudentAbility(Long studentId) {
        studentAbilityMapper.deleteStudentAbilityByStudentId(studentId);
    }

    @Override
    public void batchUpdateStudentAbility(List<StudentAbility> abilities) {
        for (StudentAbility ability : abilities) {
            ability.setUpdateTime(new Date());
            studentAbilityMapper.updateStudentAbility(ability);
        }
    }

    @Override
    public Double getEloByStudentIdAndAbilityType(Long studentId, String abilityType) {
        List<QuizAnswerRecord> records = studentAbilityMapper.selectRecordsByStudentIdAndAbilityType(studentId, abilityType);
        double theta = 1500;
        int k = 16;
        for (QuizAnswerRecord r : records) {
            Double beta = r.getDifficulty();
            Boolean correct = r.getIsCorrect();
            double S = (correct != null && correct) ? 1.0 : 0.0;
            double E = 1.0 / (1 + Math.pow(10, (beta - theta) / 400.0));
            theta = theta + k * (S - E);
            theta = Math.min(theta, 2500);
        }
        return theta;
    }
}