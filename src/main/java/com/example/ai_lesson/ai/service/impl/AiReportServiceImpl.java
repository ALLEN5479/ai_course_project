package com.example.ai_lesson.ai.service.impl;

import com.example.ai_lesson.ai.service.AiReportService;
import org.springframework.stereotype.Service;

@Service
public class AiReportServiceImpl implements AiReportService {
    
    @Override
    public String getAiReport(String prompt) {
        // TODO: 实现AI报告生成逻辑
        return "AI报告: " + prompt;
    }
}
