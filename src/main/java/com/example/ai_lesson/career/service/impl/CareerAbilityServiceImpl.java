package com.example.ai_lesson.career.service.impl;

import com.example.ai_lesson.career.service.CareerAbilityService;
import com.example.ai_lesson.common.AjaxResult;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import com.example.ai_lesson.career.entity.CareerAbilityData;
import org.springframework.stereotype.Service;
import com.alibaba.excel.EasyExcel;
import com.example.ai_lesson.career.entity.StudentExperience;
import com.example.ai_lesson.career.mapper.CareerAbilityMapper;
import com.example.ai_lesson.ai.entity.AiResult;
import com.example.ai_lesson.ai.service.AiReportService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CareerAbilityServiceImpl implements CareerAbilityService {

    private final CareerAbilityMapper careerAbilityMapper;
    @Autowired
    private AiReportService aiReportService;

    public CareerAbilityServiceImpl(CareerAbilityMapper careerAbilityMapper) {
        this.careerAbilityMapper = careerAbilityMapper;
    }

    @Override
    public AjaxResult generateAiReport(String studentId) {
        // 实现AI报告生成逻辑
        // 示例返回
        return AjaxResult.success("AI报告生成成功");
    }

    @Override
    public Map<String, Object> getCareerAbilityList(int page, int size) {
        // 实现分页查询逻辑，返回数据
        Map<String, Object> result = new HashMap<>();
        // result.put("list", ...);
        // result.put("total", ...);
        // result.put("stats", ...);
        return result;
    }

    @Override
    public AjaxResult importFromExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<StudentExperience> studentList = EasyExcel.read(is)
                .head(StudentExperience.class)
                .sheet()
                .doReadSync();

            List<CareerAbilityData> saveList = new ArrayList<>();
            for (StudentExperience student : studentList) {
                // 拼接所有经历文本
                StringBuilder sb = new StringBuilder();
                if (student.getExperience1() != null) sb.append(student.getExperience1()).append(" ");
                if (student.getExperience2() != null) sb.append(student.getExperience2()).append(" ");
                if (student.getExperience3() != null) sb.append(student.getExperience3());
                String allExperience = sb.toString().trim();

                // 调用大模型API
                String prompt = allExperience;
                AiResult aiResult = aiReportService.getAiReport(prompt);

                // 封装能力数据
                CareerAbilityData data = new CareerAbilityData();
                data.setStudentId(student.getStudentId());
                data.setName(student.getName());
                data.setClassName(student.getClassName());
                data.setCourseScore(aiResult.getCourseScore());
                data.setPracticeScore(aiResult.getPracticeScore());
                data.setInnovationScore(aiResult.getInnovationScore());
                data.setTeamworkScore(aiResult.getTeamworkScore());
                data.setCommunicationScore(aiResult.getCommunicationScore());
                data.setQualityScore(aiResult.getQualityScore());
                data.setAiReport(aiResult.getReport());
                saveList.add(data);
            }
            // 批量入库
            for (CareerAbilityData data : saveList) {
                careerAbilityMapper.insertOrUpdate(data);
            }
            return AjaxResult.success("导入并分析成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败: " + e.getMessage());
        }
    }

    @Override
    public CareerAbilityData getStudentCareerAbility(String studentId) {
        return careerAbilityMapper.selectByStudentId(studentId);
    }
}
