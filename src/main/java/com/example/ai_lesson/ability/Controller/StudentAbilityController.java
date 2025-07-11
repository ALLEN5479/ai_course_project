package com.example.ai_lesson.ability.Controller;

import com.example.ai_lesson.ability.Domain.StudentAbility;
import com.example.ai_lesson.aiquestion.entity.QuizAnswerRecord;
import com.example.ai_lesson.common.AjaxResult;
import com.example.ai_lesson.ability.Service.StudentAbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/student/ability")
@CrossOrigin(origins = "*")
public class StudentAbilityController {

    @Autowired
    private StudentAbilityService studentAbilityService;

    /**
     * 获取学生语法能力分数
     */
    @GetMapping("/grammar/{studentId}")
    public AjaxResult getGrammarAbility(@PathVariable Long studentId) {
        try {
            Double score = studentAbilityService.getGrammarEloByStudentId(studentId);
            return AjaxResult.success(score);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("服务端异常: " + e.getMessage());
        }
    }

    /**
     * 添加能力问题记录
     */
    @PostMapping("/record")
    public AjaxResult addAbilityQuestionRecord(@RequestBody QuizAnswerRecord record) {
        try {
            studentAbilityService.addQuestionRecord(record);
            return AjaxResult.success("记录添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("添加记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取学生能力图谱数据
     */
    @GetMapping("/capability-map/{studentId}")
    public AjaxResult getCapabilityMap(@PathVariable Long studentId) {
        try {
            List<StudentAbility> abilities = studentAbilityService.getByStudentId(studentId);
            return AjaxResult.success(abilities);
        } catch (Exception e) {
            return AjaxResult.error("获取能力图谱失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有学生的能力数据（用于对比分析）
     */
    @GetMapping("/all-students")
    public AjaxResult getAllStudentsAbility() {
        try {
            List<StudentAbility> abilities = studentAbilityService.getAllStudentsAbility();
            return AjaxResult.success(abilities);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取学生能力数据失败: " + e.getMessage());
        }
    }

    /**
     * 更新学生能力数据
     */
    @PutMapping("/update")
    public AjaxResult updateStudentAbility(@RequestBody StudentAbility ability) {
        try {
            studentAbilityService.updateStudentAbility(ability);
            return AjaxResult.success("能力数据更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 更新学习主动性分数
     */
    @PostMapping("/update-initiative/{studentId}")
    public AjaxResult updateInitiative(@PathVariable Long studentId) {
        try {
            studentAbilityService.updateInitiativeAbility(studentId);
            return AjaxResult.success("学习主动性分数已更新");
        } catch (Exception e) {
            return AjaxResult.error("更新失败: " + e.getMessage());
        }
    }
}