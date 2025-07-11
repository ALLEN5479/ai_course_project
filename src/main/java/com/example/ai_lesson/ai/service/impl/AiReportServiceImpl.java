package com.example.ai_lesson.ai.service.impl;

import com.example.ai_lesson.ai.entity.AiResult;
import com.example.ai_lesson.ai.service.AiReportService;
import okhttp3.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

@Service
public class AiReportServiceImpl implements AiReportService {

    @Value("${custom.ai.api.url}")
    private String customAiApiUrl;

    private String apiKey = "app-uJlcwDpX2BrN2ko8cjMAOM5f";

    @Override
    public AiResult getAiReport(String prompt, String userId) {
        try {
            OkHttpClient client = new OkHttpClient();
            String apiUrl = "http://localhost:5001/v1/workflows/run";
            String query = prompt + "，" + userId;
            String jsonBody = "{\"workflow_id\": \"a5cee24b-0e8c-4541-a53e-0e2237faada7\", \"inputs\": {\"query\": \"" + query + "\"}, \"user\": \"" + userId + "\"}";

            RequestBody requestBody = RequestBody.create(
                    jsonBody, MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "PostmanRuntime/7.32.3")
                    .post(requestBody)
                    .build();

            System.out.println("AI服务请求URL: " + apiUrl);
            System.out.println("AI服务请求Headers: " + request.headers());
            System.out.println("AI服务请求Body: " + jsonBody);
            System.out.println("后端实际用的API Key: " + apiKey);

            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String resp = response.body().string();
                System.out.println("AI返回: " + resp);
                JSONObject obj = JSON.parseObject(resp);
                JSONObject data = obj.getJSONObject("data");
                JSONObject outputs = data.getJSONObject("outputs");
                String resultJson = outputs.getString("result");

                int start = resultJson.indexOf("{");
                int end = resultJson.lastIndexOf("}");
                if (start != -1 && end != -1 && end > start) {
                    String validJson = resultJson.substring(start, end + 1);
                    JSONObject result = JSON.parseObject(validJson);

                    int courseScore = result.getIntValue("课程能力");
                    int practiceScore = result.getIntValue("实践能力");
                    int innovationScore = result.getIntValue("创新能力");
                    int teamworkScore = result.getIntValue("团队协作");
                    int communicationScore = result.getIntValue("沟通表达");
                    int qualityScore = result.getIntValue("综合素养");

                    // 0分兜底为60分
                    if (courseScore == 0) courseScore = 60;
                    if (practiceScore == 0) practiceScore = 60;
                    if (innovationScore == 0) innovationScore = 60;
                    if (teamworkScore == 0) teamworkScore = 60;
                    if (communicationScore == 0) communicationScore = 60;
                    if (qualityScore == 0) qualityScore = 60;

                    String report = result.getString("分析报告");

                    // 封装到 AiResult 返回
                    AiResult aiResult = new AiResult();
                    aiResult.setCourseScore(courseScore);
                    aiResult.setPracticeScore(practiceScore);
                    aiResult.setInnovationScore(innovationScore);
                    aiResult.setTeamworkScore(teamworkScore);
                    aiResult.setCommunicationScore(communicationScore);
                    aiResult.setQualityScore(qualityScore);
                    aiResult.setReport(report);
                    return aiResult;
                } else {
                    throw new RuntimeException("AI返回内容不是合法JSON: " + resultJson);
                }
            } else {
                throw new RuntimeException("AI服务调用失败: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            AiResult result = new AiResult();
            result.setReport("AI服务调用失败: " + e.getMessage());
            return result;
        }
    }

    public void test() {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "app-WHMIF7r0vaH95YBIrKh4sY94";
        String apiUrl = "http://localhost:5001/v1/workflows/run";
        String jsonBody = "{\"workflow_id\": \"a5cee24b-0e8c-4541-a53e-0e2237faada7\", \"inputs\": {\"query\": \"xxx\"}, \"user\": \"test-user\"}";
        RequestBody requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json"));
        Request request = new Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "PostmanRuntime/7.32.3")
            .post(requestBody)
            .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.code());
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
