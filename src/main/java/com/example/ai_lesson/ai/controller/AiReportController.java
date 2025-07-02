package com.example.ai_lesson.ai.controller;

import com.example.ai_lesson.ai.service.AiReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/report")
public class AiReportController {

    @Autowired
    private AiReportService aiReportService;

    @PostMapping
    public String getAiReport(@RequestBody String prompt) {
        return aiReportService.getAiReport(prompt);
    }
} 