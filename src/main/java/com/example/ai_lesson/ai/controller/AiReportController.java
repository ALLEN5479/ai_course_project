package com.example.ai_lesson.ai.controller;

import com.example.ai_lesson.ai.service.AiReportService;
import com.example.ai_lesson.ai.entity.AiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/report")
public class AiReportController {

    @Autowired
    private AiReportService aiReportService;

    @PostMapping
    public AiResult getAiReport(@RequestBody String prompt) {
        return aiReportService.getAiReport(prompt);
    }
} 