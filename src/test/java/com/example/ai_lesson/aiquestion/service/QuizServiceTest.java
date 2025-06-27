package com.example.ai_lesson.aiquestion.service;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import com.example.ai_lesson.aiquestion.mapper.QuizQuestionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "custom.ai.api.url=http://localhost/v1",
    "custom.ai.api.key=app-DmumztREf6fuaSQQ8xjHetPs"
})
class QuizServiceTest {

    @Mock
    private QuizQuestionMapper questionMapper;

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizService(questionMapper, "http://localhost/v1", "app-DmumztREf6fuaSQQ8xjHetPs");
    }

    @Test
    void testGenerateQuestions_Success() throws IOException {
        // 测试生成1道简单题目
        List<QuizQuestion> questions = quizService.generateQuestions(1, "简单", "关于Java基础语法");
        
        // 验证返回结果
        assertNotNull(questions);
        assertFalse(questions.isEmpty());
        assertEquals(1, questions.size());
        
        QuizQuestion question = questions.get(0);
        assertNotNull(question);
        assertNotNull(question.getQuestionText());
        assertNotNull(question.getOptionA());
        assertNotNull(question.getOptionB());
        assertNotNull(question.getOptionC());
        assertNotNull(question.getOptionD());
        assertNotNull(question.getCorrectAnswer());
        assertEquals("简单", question.getDifficulty());
        assertEquals("编程", question.getCategory());
        
        // 验证答案格式
        assertTrue(question.getCorrectAnswer().matches("[A-D]"));
        
        System.out.println("生成的题目:");
        System.out.println("问题: " + question.getQuestionText());
        System.out.println("A: " + question.getOptionA());
        System.out.println("B: " + question.getOptionB());
        System.out.println("C: " + question.getOptionC());
        System.out.println("D: " + question.getOptionD());
        System.out.println("正确答案: " + question.getCorrectAnswer());
    }

    @Test
    void testGenerateQuestions_MultipleQuestions() throws IOException {
        // 测试生成3道中等难度题目
        List<QuizQuestion> questions = quizService.generateQuestions(3, "中等", "关于Java面向对象编程");
        
        // 验证返回结果
        assertNotNull(questions);
        assertEquals(3, questions.size());
        
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion question = questions.get(i);
            assertNotNull(question);
            assertNotNull(question.getQuestionText());
            assertEquals("中等", question.getDifficulty());
            assertEquals("编程", question.getCategory());
            
            System.out.println("题目 " + (i + 1) + ":");
            System.out.println("问题: " + question.getQuestionText());
            System.out.println("正确答案: " + question.getCorrectAnswer());
            System.out.println("---");
        }
    }

    @Test
    void testGenerateQuestions_DifficultyLevels() throws IOException {
        // 测试不同难度级别
        String[] difficulties = {"简单", "中等", "困难"};
        
        for (String difficulty : difficulties) {
            List<QuizQuestion> questions = quizService.generateQuestions(1, difficulty, "关于Java集合框架");
            
            assertNotNull(questions);
            assertFalse(questions.isEmpty());
            
            QuizQuestion question = questions.get(0);
            assertEquals(difficulty, question.getDifficulty());
            
            System.out.println(difficulty + "难度题目:");
            System.out.println("问题: " + question.getQuestionText());
            System.out.println("---");
        }
    }

    @Test
    void testGenerateQuestions_WithRequirement() throws IOException {
        // 测试带特定要求的题目生成
        String requirement = "关于Java多线程编程，包含线程安全概念";
        List<QuizQuestion> questions = quizService.generateQuestions(1, "困难", requirement);
        
        assertNotNull(questions);
        assertFalse(questions.isEmpty());
        
        QuizQuestion question = questions.get(0);
        assertNotNull(question.getQuestionText());
        assertEquals("困难", question.getDifficulty());
        
        System.out.println("带要求的题目:");
        System.out.println("要求: " + requirement);
        System.out.println("问题: " + question.getQuestionText());
        System.out.println("选项A: " + question.getOptionA());
        System.out.println("选项B: " + question.getOptionB());
        System.out.println("选项C: " + question.getOptionC());
        System.out.println("选项D: " + question.getOptionD());
        System.out.println("正确答案: " + question.getCorrectAnswer());
    }

    @Test
    void testGenerateQuestions_EdgeCases() throws IOException {
        // 测试边界情况
        try {
            // 测试空要求
            List<QuizQuestion> questions = quizService.generateQuestions(1, "简单", "");
            assertNotNull(questions);
            assertFalse(questions.isEmpty());
            System.out.println("空要求测试通过");
            
            // 测试null要求
            questions = quizService.generateQuestions(1, "简单", null);
            assertNotNull(questions);
            assertFalse(questions.isEmpty());
            System.out.println("null要求测试通过");
            
        } catch (Exception e) {
            fail("边界情况测试失败: " + e.getMessage());
        }
    }

    @Test
    void testSaveQuestions() {
        // 测试保存功能
        QuizQuestion question = new QuizQuestion();
        question.setQuestionText("测试问题");
        question.setOptionA("选项A");
        question.setOptionB("选项B");
        question.setOptionC("选项C");
        question.setOptionD("选项D");
        question.setCorrectAnswer("A");
        question.setDifficulty("简单");
        question.setCategory("编程");
        
        // Mock mapper行为
        when(questionMapper.batchInsert(any())).thenReturn(1);
        
        List<QuizQuestion> questions = List.of(question);
        List<QuizQuestion> savedQuestions = quizService.saveQuestions(questions);
        
        assertNotNull(savedQuestions);
        assertEquals(1, savedQuestions.size());
        assertEquals("测试问题", savedQuestions.get(0).getQuestionText());
    }

    @Test
    void testGetAllQuestions() {
        // 测试获取所有问题
        QuizQuestion question1 = new QuizQuestion();
        question1.setQuestionText("问题1");
        question1.setDifficulty("简单");
        
        QuizQuestion question2 = new QuizQuestion();
        question2.setQuestionText("问题2");
        question2.setDifficulty("中等");
        
        when(questionMapper.selectAll()).thenReturn(List.of(question1, question2));
        
        List<QuizQuestion> questions = quizService.getAllQuestions();
        
        assertNotNull(questions);
        assertEquals(2, questions.size());
        assertEquals("问题1", questions.get(0).getQuestionText());
        assertEquals("问题2", questions.get(1).getQuestionText());
    }
} 