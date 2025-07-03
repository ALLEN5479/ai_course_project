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
        Map<String, Object> result = new HashMap<>();

        // 1. 分页查询
        int offset = (page - 1) * size;
        List<CareerAbilityData> list = careerAbilityMapper.select_page(offset, size);

        // 2. 总数统计
        int total = careerAbilityMapper.count_all();

        // 3. 其它统计
        int analyzed_students = total; // 你可以根据实际业务调整
        int report_count = careerAbilityMapper.count_ai_report_not_null();
        Double avg_score = careerAbilityMapper.avg_total_score();
        if (avg_score == null) avg_score = 0.0;

        // 4. 封装统计信息
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_students", total);
        stats.put("analyzed_students", analyzed_students);
        stats.put("report_count", report_count);
        stats.put("avg_score", avg_score);

        // 5. 封装返回
        result.put("list", list);
        result.put("total", total);
        result.put("stats", stats);
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
                AiResult aiResult = aiReportService.getAiReport(prompt, student.getStudentId());

                // 封装能力数据
                CareerAbilityData data = new CareerAbilityData();
                data.setStudent_id(student.getStudentId());
                data.setName(student.getName());
                data.setClass_name(student.getClassName());
                data.setCourse_score(aiResult.getCourseScore());
                data.setPractice_score(aiResult.getPracticeScore());
                data.setInnovation_score(aiResult.getInnovationScore());
                data.setTeamwork_score(aiResult.getTeamworkScore());
                data.setCommunication_score(aiResult.getCommunicationScore());
                data.setQuality_score(aiResult.getQualityScore());
                data.setAi_report(aiResult.getReport());
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

    @Override
    public int deleteCareerAbility(String studentId) {
        return careerAbilityMapper.deleteByStudentId(studentId);
    }
}
