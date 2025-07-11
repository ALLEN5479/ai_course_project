package com.example.ai_lesson.career.service;

import com.example.ai_lesson.common.AjaxResult;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import com.example.ai_lesson.career.entity.CareerAbilityData;

public interface CareerAbilityService {
    AjaxResult generateAiReport(String studentId);
    Map<String, Object> getCareerAbilityList(int page, int size);
    AjaxResult importFromExcel(MultipartFile file);
    CareerAbilityData getStudentCareerAbility(String studentId);
    int deleteCareerAbility(String studentId);
}
