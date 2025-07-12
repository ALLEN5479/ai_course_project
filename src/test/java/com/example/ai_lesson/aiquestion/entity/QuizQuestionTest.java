package com.example.ai_lesson.aiquestion.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

@DisplayName("QuizQuestion实体类测试")
class QuizQuestionTest {

    private QuizQuestion quizQuestion;

    @BeforeEach
    void setUp() {
        quizQuestion = new QuizQuestion();
    }

    @Test
    @DisplayName("测试QuizQuestion的基本属性设置和获取")
    void testBasicProperties() {
        // 设置属性
        quizQuestion.setId(1);
        quizQuestion.setQuestion_text("什么是Java中的多态？");
        quizQuestion.setOption_a("同一个接口，不同的实现");
        quizQuestion.setOption_b("同一个类，不同的方法");
        quizQuestion.setOption_c("同一个对象，不同的属性");
        quizQuestion.setOption_d("同一个方法，不同的参数");
        quizQuestion.setCorrect_answer("A");
        quizQuestion.setCategory("编程");
        quizQuestion.setDifficulty("中等");
        
        Date now = new Date();
        quizQuestion.setCreated_at(now);

        // 验证属性
        assertEquals(1, quizQuestion.getId());
        assertEquals("什么是Java中的多态？", quizQuestion.getQuestion_text());
        assertEquals("同一个接口，不同的实现", quizQuestion.getOption_a());
        assertEquals("同一个类，不同的方法", quizQuestion.getOption_b());
        assertEquals("同一个对象，不同的属性", quizQuestion.getOption_c());
        assertEquals("同一个方法，不同的参数", quizQuestion.getOption_d());
        assertEquals("A", quizQuestion.getCorrect_answer());
        assertEquals("编程", quizQuestion.getCategory());
        assertEquals("中等", quizQuestion.getDifficulty());
        assertEquals(now, quizQuestion.getCreated_at());
    }

    @Test
    @DisplayName("测试QuizQuestion的空值处理")
    void testNullValues() {
        // 设置空值
        quizQuestion.setId(null);
        quizQuestion.setQuestion_text(null);
        quizQuestion.setOption_a(null);
        quizQuestion.setOption_b(null);
        quizQuestion.setOption_c(null);
        quizQuestion.setOption_d(null);
        quizQuestion.setCorrect_answer(null);
        quizQuestion.setCategory(null);
        quizQuestion.setDifficulty(null);
        quizQuestion.setCreated_at(null);

        // 验证空值
        assertNull(quizQuestion.getId());
        assertNull(quizQuestion.getQuestion_text());
        assertNull(quizQuestion.getOption_a());
        assertNull(quizQuestion.getOption_b());
        assertNull(quizQuestion.getOption_c());
        assertNull(quizQuestion.getOption_d());
        assertNull(quizQuestion.getCorrect_answer());
        assertNull(quizQuestion.getCategory());
        assertNull(quizQuestion.getDifficulty());
        assertNull(quizQuestion.getCreated_at());
    }

    @Test
    @DisplayName("测试QuizQuestion的边界值")
    void testBoundaryValues() {
        // 测试空字符串
        quizQuestion.setQuestion_text("");
        quizQuestion.setOption_a("");
        quizQuestion.setOption_b("");
        quizQuestion.setOption_c("");
        quizQuestion.setOption_d("");
        quizQuestion.setCorrect_answer("");
        quizQuestion.setCategory("");
        quizQuestion.setDifficulty("");

        assertEquals("", quizQuestion.getQuestion_text());
        assertEquals("", quizQuestion.getOption_a());
        assertEquals("", quizQuestion.getOption_b());
        assertEquals("", quizQuestion.getOption_c());
        assertEquals("", quizQuestion.getOption_d());
        assertEquals("", quizQuestion.getCorrect_answer());
        assertEquals("", quizQuestion.getCategory());
        assertEquals("", quizQuestion.getDifficulty());

        // 测试特殊字符
        String specialText = "特殊字符测试：!@#$%^&*()_+-=[]{}|;':\",./<>?";
        quizQuestion.setQuestion_text(specialText);
        assertEquals(specialText, quizQuestion.getQuestion_text());
    }

    @Test
    @DisplayName("测试QuizQuestion的日期处理")
    void testDateHandling() {
        Date testDate = new Date();
        quizQuestion.setCreated_at(testDate);
        assertEquals(testDate, quizQuestion.getCreated_at());

        // 测试null日期
        quizQuestion.setCreated_at(null);
        assertNull(quizQuestion.getCreated_at());
    }

    @Test
    @DisplayName("测试QuizQuestion的ID处理")
    void testIdHandling() {
        // 测试正数ID
        quizQuestion.setId(100);
        assertEquals(100, quizQuestion.getId());

        // 测试零ID
        quizQuestion.setId(0);
        assertEquals(0, quizQuestion.getId());

        // 测试负数ID
        quizQuestion.setId(-1);
        assertEquals(-1, quizQuestion.getId());

        // 测试null ID
        quizQuestion.setId(null);
        assertNull(quizQuestion.getId());
    }
} 