package com.example.ai_lesson.ai.service.impl;

import com.example.ai_lesson.ai.entity.AiResult;
import com.example.ai_lesson.ai.service.AiReportService;
import okhttp3.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class AiReportServiceImpl implements AiReportService {
    
    @Override
    public AiResult getAiReport(String prompt, String userId) {
        try {
            String apiUrl = "http://localhost/v1";
            String apiKey = "app-U4I9n3gdpnt9cXg5jB93v1Vw";

            OkHttpClient client = new OkHttpClient();
            String jsonBody = "{"
                + "\"query\": \"" + prompt.replace("\"", "\\\"") + "\","
                + "\"response_mode\": \"blocking\","
                + "\"user\": \"" + userId + "\""
                + "}";

            RequestBody body = RequestBody.create(
                jsonBody, MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String resp = response.body().string();
                JSONObject obj = JSON.parseObject(resp);
                String answerJson = obj.getString("answer");
                return JSON.parseObject(answerJson, AiResult.class);
            } else {
                throw new RuntimeException("AI服务调用失败: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 失败时返回默认对象或抛异常
            AiResult result = new AiResult();
            result.setReport("AI服务调用失败: " + e.getMessage());
            return result;
        }
    }


}
