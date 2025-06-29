package com.example.ai_lesson.aiquestion.service;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试类 - 直接测试AI生成功能
 * 注意：这个测试需要真实的AI API连接
 */
@SpringBootTest
@TestPropertySource(properties = {
    "custom.ai.api.url=http://localhost/v1",
    "custom.ai.api.key=app-DmumztREf6fuaSQQ8xjHetPs"
})
class QuizServiceIntegrationTest {

    @Test
    void testAIGeneration() {
        // 创建QuizService实例（不依赖Spring容器）
        QuizService quizService = new QuizService(
            null, // 不使用数据库
            "http://localhost/v1", 
            "app-DmumztREf6fuaSQQ8xjHetPs"
        );

        try {
            System.out.println("=== 开始测试AI生成功能 ===");
            
            // 测试1：生成简单题目
            System.out.println("\n1. 测试生成简单题目:");
            List<QuizQuestion> simpleQuestions = quizService.generateQuestions(1, "简单", "关于Java变量和数据类型");
            printQuestions(simpleQuestions);
            
            // 测试2：生成中等难度题目
            System.out.println("\n2. 测试生成中等难度题目:");
            List<QuizQuestion> mediumQuestions = quizService.generateQuestions(1, "中等", "关于Java面向对象编程");
            printQuestions(mediumQuestions);
            
            // 测试3：生成困难题目
            System.out.println("\n3. 测试生成困难题目:");
            List<QuizQuestion> hardQuestions = quizService.generateQuestions(1, "困难", "关于Java多线程和并发编程");
            printQuestions(hardQuestions);
            
            // 测试4：生成多道题目
            System.out.println("\n4. 测试生成多道题目:");
            List<QuizQuestion> multipleQuestions = quizService.generateQuestions(2, "中等", "关于Java集合框架");
            printQuestions(multipleQuestions);
            
            // 测试5：测试特殊要求
            System.out.println("\n5. 测试特殊要求:");
            List<QuizQuestion> specialQuestions = quizService.generateQuestions(1, "简单", "关于Java异常处理机制");
            printQuestions(specialQuestions);
            
            System.out.println("\n=== AI生成功能测试完成 ===");
            
        } catch (IOException e) {
            System.err.println("AI服务连接失败: " + e.getMessage());
            fail("AI服务连接失败: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("测试过程中发生错误: " + e.getMessage());
            e.printStackTrace();
            fail("测试过程中发生错误: " + e.getMessage());
        }
    }

    @Test
    void testAIConnection() {
        System.out.println("=== 测试AI服务连接 ===");
        
        QuizService quizService = new QuizService(
            null,
            "http://localhost/v1", 
            "app-DmumztREf6fuaSQQ8xjHetPs"
        );

        try {
            // 测试最简单的生成请求
            List<QuizQuestion> questions = quizService.generateQuestions(1, "简单", "测试");
            
            assertNotNull(questions);
            assertFalse(questions.isEmpty());
            
            QuizQuestion question = questions.get(0);
            assertNotNull(question.getQuestion_text());
            assertNotNull(question.getOption_a());
            assertNotNull(question.getOption_b());
            assertNotNull(question.getOption_c());
            assertNotNull(question.getOption_d());
            assertNotNull(question.getCorrect_answer());
            
            System.out.println("AI服务连接成功！");
            System.out.println("生成的问题: " + question.getQuestion_text());
            
        } catch (IOException e) {
            System.err.println("AI服务连接失败: " + e.getMessage());
            System.err.println("请检查:");
            System.err.println("1. AI服务是否正在运行");
            System.err.println("2. API URL是否正确");
            System.err.println("3. API Key是否有效");
            fail("AI服务连接失败: " + e.getMessage());
        }
    }

    private void printQuestions(List<QuizQuestion> questions) {
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            System.out.println("题目 " + (i + 1) + ":");
            System.out.println("  难度: " + q.getDifficulty());
            System.out.println("  问题: " + q.getQuestion_text());
            System.out.println("  A: " + q.getOption_a());
            System.out.println("  B: " + q.getOption_b());
            System.out.println("  C: " + q.getOption_c());
            System.out.println("  D: " + q.getOption_d());
            System.out.println("  正确答案: " + q.getCorrect_answer());
            System.out.println("  ---");
        }
    }
} 