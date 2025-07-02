package com.example.ai_lesson.career.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.ai_lesson.career.service.CareerAbilityService;
import com.example.ai_lesson.career.entity.CareerAbilityData;
import com.example.ai_lesson.common.AjaxResult;

@RestController
@RequestMapping("/api/career/ability")
public class CareerAbilityController {
    @Autowired
    private CareerAbilityService careerAbilityService;

    @GetMapping("/student/{studentId}")
    public AjaxResult getStudentCareerAbility(@PathVariable String studentId) {
        CareerAbilityData data = careerAbilityService.getStudentCareerAbility(studentId);
        return AjaxResult.success(data);
    }

    @GetMapping("/list")
    public AjaxResult getCareerAbilityList(@RequestParam int page, @RequestParam int size) {
        // 返回分页数据和统计
        return AjaxResult.success(careerAbilityService.getCareerAbilityList(page, size));
    }

    @PostMapping("/upload")
    public AjaxResult uploadCareerAbilityData(@RequestParam("file") MultipartFile file) {
        // 解析Excel并入库
        return careerAbilityService.importFromExcel(file);
    }

    @PostMapping("/ai-report/{studentId}")
    public AjaxResult generateAiReport(@PathVariable String studentId) {
        // 调用AI服务生成报告
        return careerAbilityService.generateAiReport(studentId);
    }
}
