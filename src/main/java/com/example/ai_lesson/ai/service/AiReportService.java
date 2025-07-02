package com.example.ai_lesson.ai.service;

import com.example.ai_lesson.ai.entity.AiResult;

public interface AiReportService {
    AiResult getAiReport(String prompt);
}
