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
    public AiResult getAiReport(@RequestBody AiReportRequest req) {
        return aiReportService.getAiReport(req.getPrompt(), req.getUserId());
    }

   
}

class AiReportRequest {
    private String prompt;
    private String userId;
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
} 