package com.example.ai_lesson.aiquestion.service;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import com.example.ai_lesson.aiquestion.mapper.QuizQuestionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库集成测试类
 * 测试AI生成题目并保存到数据库的功能
 */
@SpringBootTest
@TestPropertySource(properties = {
    "custom.ai.api.url=http://localhost/v1",
    "custom.ai.api.key=app-DmumztREf6fuaSQQ8xjHetPs"
})
@Transactional
class QuizServiceDatabaseTest {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizQuestionMapper questionMapper;

    @Test
    void testGenerateAndSaveQuestions() throws IOException {
        System.out.println("=== 测试AI生成题目并保存到数据库 ===");
        
        // 1. 生成题目
        System.out.println("\n1. 生成2道简单题目...");
        List<QuizQuestion> generatedQuestions = quizService.generateQuestions(2, "简单", "关于Java变量和数据类型");
        
        assertNotNull(generatedQuestions);
        assertEquals(2, generatedQuestions.size());
        
        // 打印生成的题目
        for (int i = 0; i < generatedQuestions.size(); i++) {
            QuizQuestion q = generatedQuestions.get(i);
            System.out.println("生成的题目 " + (i + 1) + ":");
            System.out.println("  问题: " + q.getQuestion_text());
            System.out.println("  难度: " + q.getDifficulty());
            System.out.println("  ---");
        }
        
        // 2. 保存到数据库
        System.out.println("\n2. 保存题目到数据库...");
        List<QuizQuestion> savedQuestions = quizService.saveQuestions(generatedQuestions);
        
        assertNotNull(savedQuestions);
        assertEquals(2, savedQuestions.size());
        
        // 3. 验证数据库中的数据
        System.out.println("\n3. 从数据库查询所有题目...");
        List<QuizQuestion> allQuestions = quizService.getAllQuestions();
        
        assertNotNull(allQuestions);
        assertTrue(allQuestions.size() >= 2);
        
        // 查找刚保存的题目
        boolean foundSavedQuestions = false;
        for (QuizQuestion question : allQuestions) {
            if (question.getQuestion_text().equals(generatedQuestions.get(0).getQuestion_text()) ||
                question.getQuestion_text().equals(generatedQuestions.get(1).getQuestion_text())) {
                foundSavedQuestions = true;
                System.out.println("找到保存的题目: " + question.getQuestion_text());
                System.out.println("  ID: " + question.getId());
                System.out.println("  难度: " + question.getDifficulty());
                System.out.println("  分类: " + question.getCategory());
                System.out.println("  ---");
            }
        }
        
        assertTrue(foundSavedQuestions, "未找到保存的题目");
        
        System.out.println("\n=== 数据库保存测试完成 ===");
    }

    @Test
    void testGenerateAndSaveDifferentDifficulties() throws IOException {
        System.out.println("=== 测试不同难度题目的生成和保存 ===");
        
        String[] difficulties = {"简单", "中等", "困难"};
        
        for (String difficulty : difficulties) {
            System.out.println("\n生成" + difficulty + "难度题目...");
            
            // 生成题目
            List<QuizQuestion> questions = quizService.generateQuestions(1, difficulty, "关于Java编程");
            assertNotNull(questions);
            assertEquals(1, questions.size());
            
            // 保存到数据库
            List<QuizQuestion> savedQuestions = quizService.saveQuestions(questions);
            assertNotNull(savedQuestions);
            
            // 验证保存的题目
            QuizQuestion savedQuestion = savedQuestions.get(0);
            assertEquals(difficulty, savedQuestion.getDifficulty());
            
            System.out.println("保存成功: " + savedQuestion.getQuestion_text());
            System.out.println("难度: " + savedQuestion.getDifficulty());
            System.out.println("---");
        }
        
        // 验证所有难度都有题目
        for (String difficulty : difficulties) {
            List<QuizQuestion> questionsByDifficulty = quizService.getQuestionsByDifficulty(difficulty);
            assertNotNull(questionsByDifficulty);
            assertTrue(questionsByDifficulty.size() > 0, difficulty + "难度没有题目");
            System.out.println(difficulty + "难度题目数量: " + questionsByDifficulty.size());
        }
        
        System.out.println("\n=== 不同难度测试完成 ===");
    }

    @Test
    void testQuestionCRUD() throws IOException {
        System.out.println("=== 测试题目的增删改查 ===");
        
        // 1. 生成并保存题目
        System.out.println("\n1. 生成并保存题目...");
        List<QuizQuestion> questions = quizService.generateQuestions(1, "简单", "测试CRUD功能");
        List<QuizQuestion> savedQuestions = quizService.saveQuestions(questions);
        
        QuizQuestion originalQuestion = savedQuestions.get(0);
        assertNotNull(originalQuestion.getId());
        
        System.out.println("保存的题目ID: " + originalQuestion.getId());
        System.out.println("题目内容: " + originalQuestion.getQuestion_text());
        
        // 2. 根据ID查询题目
        System.out.println("\n2. 根据ID查询题目...");
        var foundQuestion = quizService.getQuestionById(originalQuestion.getId());
        assertTrue(foundQuestion.isPresent());
        assertEquals(originalQuestion.getQuestion_text(), foundQuestion.get().getQuestion_text());
        
        System.out.println("查询成功: " + foundQuestion.get().getQuestion_text());
        
        // 3. 更新题目
        System.out.println("\n3. 更新题目...");
        QuizQuestion updateData = new QuizQuestion();
        updateData.setQuestion_text("更新后的测试题目");
        updateData.setOption_a("更新选项A");
        updateData.setOption_b("更新选项B");
        updateData.setOption_c("更新选项C");
        updateData.setOption_d("更新选项D");
        updateData.setCorrect_answer("B");
        updateData.setDifficulty("中等");
        updateData.setCategory("编程");
        
        var updatedQuestion = quizService.updateQuestion(originalQuestion.getId(), updateData);
        assertTrue(updatedQuestion.isPresent());
        assertEquals("更新后的测试题目", updatedQuestion.get().getQuestion_text());
        assertEquals("中等", updatedQuestion.get().getDifficulty());
        
        System.out.println("更新成功: " + updatedQuestion.get().getQuestion_text());
        
        // 4. 删除题目
        System.out.println("\n4. 删除题目...");
        boolean deleted = quizService.deleteQuestion(originalQuestion.getId());
        assertTrue(deleted);
        
        // 验证删除
        var deletedQuestion = quizService.getQuestionById(originalQuestion.getId());
        assertTrue(deletedQuestion.isEmpty());
        
        System.out.println("删除成功");
        
        System.out.println("\n=== CRUD测试完成 ===");
    }

    @Test
    void testDatabaseConnection() {
        System.out.println("=== 测试数据库连接 ===");
        
        try {
            // 测试数据库连接
            List<QuizQuestion> allQuestions = quizService.getAllQuestions();
            System.out.println("数据库连接成功！");
            System.out.println("当前数据库中有 " + allQuestions.size() + " 道题目");
            
            if (!allQuestions.isEmpty()) {
                System.out.println("示例题目:");
                QuizQuestion example = allQuestions.get(0);
                System.out.println("  ID: " + example.getId());
                System.out.println("  问题: " + example.getQuestion_text());
                System.out.println("  难度: " + example.getDifficulty());
            }
            
        } catch (Exception e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            fail("数据库连接失败: " + e.getMessage());
        }
        
        System.out.println("=== 数据库连接测试完成 ===");
    }
} 