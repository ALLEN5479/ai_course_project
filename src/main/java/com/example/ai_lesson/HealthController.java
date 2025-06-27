package com.example.ai_lesson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "AI Lesson API is running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "AI Lesson API");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
            "health", "/health",
            "quiz", "/api/quiz",
            "mission", "/api/mission",
            "login", "/login"
        ));
        return response;
    }
} 