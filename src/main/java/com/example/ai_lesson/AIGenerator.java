package com.example.ai_lesson;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import com.example.ai_lesson.aiquestion.service.QuizService;
import io.github.imfangs.dify.client.DifyChatClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

/**
 * AI题目生成器
 * 调用AI生成题目并存入数据库
 */
@SpringBootApplication
public class AIGenerator {
    
    public static void main(String[] args) {
        System.out.println("=== AI题目生成器 ===");
        
        // 启动Spring Boot应用
        ConfigurableApplicationContext context = SpringApplication.run(AIGenerator.class, args);
        
        try {
            // 获取QuizService实例
            QuizService quizService = context.getBean(QuizService.class);
            
            // 测试AI连接
            System.out.println("1. 测试AI服务连接...");
            testAIConnection();
            
            // 生成不同难度的题目并保存到数据库
            System.out.println("\n2. 生成简单难度题目...");
            List<QuizQuestion> simpleQuestions = quizService.generateQuestions(3, "简单", "关于Java基础语法，包括变量、数据类型、运算符");
            List<QuizQuestion> savedSimpleQuestions = quizService.saveQuestions(simpleQuestions);
            printQuestions(savedSimpleQuestions, "简单");
            
            System.out.println("\n3. 生成中等难度题目...");
            List<QuizQuestion> mediumQuestions = quizService.generateQuestions(2, "中等", "关于Java面向对象编程，包括类、对象、继承、多态");
            List<QuizQuestion> savedMediumQuestions = quizService.saveQuestions(mediumQuestions);
            printQuestions(savedMediumQuestions, "中等");
            
            System.out.println("\n4. 生成困难难度题目...");
            List<QuizQuestion> hardQuestions = quizService.generateQuestions(2, "困难", "关于Java多线程编程，包括线程安全、同步机制、并发编程");
            List<QuizQuestion> savedHardQuestions = quizService.saveQuestions(hardQuestions);
            printQuestions(savedHardQuestions, "困难");
            
            System.out.println("\n5. 生成集合框架题目...");
            List<QuizQuestion> collectionQuestions = quizService.generateQuestions(2, "中等", "关于Java集合框架，包括List、Set、Map的使用");
            List<QuizQuestion> savedCollectionQuestions = quizService.saveQuestions(collectionQuestions);
            printQuestions(savedCollectionQuestions, "中等");
            
            System.out.println("\n6. 生成异常处理题目...");
            List<QuizQuestion> exceptionQuestions = quizService.generateQuestions(1, "简单", "关于Java异常处理机制，包括try-catch、throws关键字");
            List<QuizQuestion> savedExceptionQuestions = quizService.saveQuestions(exceptionQuestions);
            printQuestions(savedExceptionQuestions, "简单");
            
            // 验证数据库保存结果
            System.out.println("\n7. 验证数据库保存结果...");
            List<QuizQuestion> allQuestions = quizService.getAllQuestions();
            System.out.println("数据库中总共有 " + allQuestions.size() + " 道题目");
            
            System.out.println("\n=== 题目生成和保存完成 ===");
            System.out.println("总共生成了 " + (savedSimpleQuestions.size() + savedMediumQuestions.size() + savedHardQuestions.size() + savedCollectionQuestions.size() + savedExceptionQuestions.size()) + " 道题目");
            System.out.println("所有题目已成功保存到数据库！");
            
        } catch (IOException e) {
            System.err.println("AI服务调用失败: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("程序执行出错: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭Spring Boot应用
            context.close();
        }
    }
    
    private static void testAIConnection() {
        try {
            DifyChatClient chatClient = DifyClientFactory.createChatClient(
                "http://localhost/v1", 
                "app-DmumztREf6fuaSQQ8xjHetPs"
            );
            
            ChatMessage message = ChatMessage.builder()
                .query("你好，请简单回复'AI服务连接正常'")
                .user("test-user")
                .responseMode(ResponseMode.BLOCKING)
                .build();
            
            ChatMessageResponse response = chatClient.sendChatMessage(message);
            System.out.println("✓ AI服务连接成功！");
            System.out.println("AI回复: " + response.getAnswer());
            
        } catch (Exception e) {
            System.err.println("✗ AI服务连接失败: " + e.getMessage());
            System.err.println("请检查:");
            System.err.println("1. AI服务是否正在运行");
            System.err.println("2. API URL是否正确: http://localhost/v1");
            System.err.println("3. API Key是否有效: app-DmumztREf6fuaSQQ8xjHetPs");
            System.exit(1);
        }
    }
    
    private static void printQuestions(List<QuizQuestion> questions, String difficulty) {
        System.out.println("生成并保存 " + questions.size() + " 道" + difficulty + "难度题目:");
        System.out.println("=" + "=".repeat(50));
        
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            System.out.println("题目 " + (i + 1) + " (ID: " + q.getId() + "):");
            System.out.println("难度: " + q.getDifficulty());
            System.out.println("问题: " + q.getQuestionText());
            System.out.println("A: " + q.getOptionA());
            System.out.println("B: " + q.getOptionB());
            System.out.println("C: " + q.getOptionC());
            System.out.println("D: " + q.getOptionD());
            System.out.println("正确答案: " + q.getCorrectAnswer());
            System.out.println("分类: " + q.getCategory());
            System.out.println("创建时间: " + q.getCreatedAt());
            System.out.println("-".repeat(50));
        }
    }
}