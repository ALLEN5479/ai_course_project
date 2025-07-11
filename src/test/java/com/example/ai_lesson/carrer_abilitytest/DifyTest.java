package com.example.ai_lesson.carrer_abilitytest;

import okhttp3.*;

public class DifyTest {
    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "app-WHMIF7r0vaH95YBlrKh4sY94";
        String apiUrl = "http://localhost:5001/v1/workflows/run";
        String jsonBody = "{\"workflow_id\": \"a5cee24b-0e8c-4541-a53e-0e2237faada7\", \"inputs\": {\"query\": \"马英杰是一名大三学生，主修计算机科学与技术。在校期间，他积极参与各类学科竞赛...\"}, \"user\": \"test-user\"}";

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

        Response response = client.newCall(request).execute();
        System.out.println("Code: " + response.code());
        System.out.println("Body: " + response.body().string());
    }
}
