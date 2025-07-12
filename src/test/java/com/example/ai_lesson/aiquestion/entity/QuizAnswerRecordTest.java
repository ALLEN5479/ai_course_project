package com.example.ai_lesson.aiquestion.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QuizAnswerRecord实体类测试")
class QuizAnswerRecordTest {

    private QuizAnswerRecord answerRecord;

    @BeforeEach
    void setUp() {
        answerRecord = new QuizAnswerRecord();
    }

    @Test
    @DisplayName("测试QuizAnswerRecord的基本属性设置和获取")
    void testBasicProperties() {
        // 设置属性
        answerRecord.setId(1L);
        answerRecord.setStudentId(1001L);
        answerRecord.setQuestionId(2001);
        answerRecord.setIsCorrect(true);
        answerRecord.setDifficulty(0.75);
        answerRecord.setAbilityType("编程");
        answerRecord.setScore(85.5);
        answerRecord.setUsedTime(120000L); // 2分钟

        // 验证属性
        assertEquals(1L, answerRecord.getId());
        assertEquals(1001L, answerRecord.getStudentId());
        assertEquals(2001, answerRecord.getQuestionId());
        assertTrue(answerRecord.getIsCorrect());
        assertEquals(0.75, answerRecord.getDifficulty(), 0.001);
        assertEquals("编程", answerRecord.getAbilityType());
        assertEquals(85.5, answerRecord.getScore(), 0.001);
        assertEquals(120000L, answerRecord.getUsedTime());
    }

    @Test
    @DisplayName("测试QuizAnswerRecord的空值处理")
    void testNullValues() {
        // 设置空值
        answerRecord.setId(null);
        answerRecord.setStudentId(null);
        answerRecord.setQuestionId(null);
        answerRecord.setIsCorrect(null);
        answerRecord.setDifficulty(null);
        answerRecord.setAbilityType(null);
        answerRecord.setScore(null);
        answerRecord.setUsedTime(null);

        // 验证空值
        assertNull(answerRecord.getId());
        assertNull(answerRecord.getStudentId());
        assertNull(answerRecord.getQuestionId());
        assertNull(answerRecord.getIsCorrect());
        assertNull(answerRecord.getDifficulty());
        assertNull(answerRecord.getAbilityType());
        assertNull(answerRecord.getScore());
        assertNull(answerRecord.getUsedTime());
    }

    @Test
    @DisplayName("测试QuizAnswerRecord的边界值")
    void testBoundaryValues() {
        // 测试零值
        answerRecord.setId(0L);
        answerRecord.setStudentId(0L);
        answerRecord.setQuestionId(0);
        answerRecord.setDifficulty(0.0);
        answerRecord.setScore(0.0);
        answerRecord.setUsedTime(0L);

        assertEquals(0L, answerRecord.getId());
        assertEquals(0L, answerRecord.getStudentId());
        assertEquals(0, answerRecord.getQuestionId());
        assertEquals(0.0, answerRecord.getDifficulty(), 0.001);
        assertEquals(0.0, answerRecord.getScore(), 0.001);
        assertEquals(0L, answerRecord.getUsedTime());

        // 测试负数
        answerRecord.setId(-1L);
        answerRecord.setStudentId(-1L);
        answerRecord.setQuestionId(-1);
        answerRecord.setDifficulty(-1.0);
        answerRecord.setScore(-1.0);
        answerRecord.setUsedTime(-1L);

        assertEquals(-1L, answerRecord.getId());
        assertEquals(-1L, answerRecord.getStudentId());
        assertEquals(-1, answerRecord.getQuestionId());
        assertEquals(-1.0, answerRecord.getDifficulty(), 0.001);
        assertEquals(-1.0, answerRecord.getScore(), 0.001);
        assertEquals(-1L, answerRecord.getUsedTime());

        // 测试最大值
        answerRecord.setId(Long.MAX_VALUE);
        answerRecord.setStudentId(Long.MAX_VALUE);
        answerRecord.setQuestionId(Integer.MAX_VALUE);
        answerRecord.setDifficulty(Double.MAX_VALUE);
        answerRecord.setScore(Double.MAX_VALUE);
        answerRecord.setUsedTime(Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, answerRecord.getId());
        assertEquals(Long.MAX_VALUE, answerRecord.getStudentId());
        assertEquals(Integer.MAX_VALUE, answerRecord.getQuestionId());
        assertEquals(Double.MAX_VALUE, answerRecord.getDifficulty(), 0.001);
        assertEquals(Double.MAX_VALUE, answerRecord.getScore(), 0.001);
        assertEquals(Long.MAX_VALUE, answerRecord.getUsedTime());
    }

    @Test
    @DisplayName("测试QuizAnswerRecord的布尔值处理")
    void testBooleanValues() {
        // 测试true值
        answerRecord.setIsCorrect(true);
        assertTrue(answerRecord.getIsCorrect());

        // 测试false值
        answerRecord.setIsCorrect(false);
        assertFalse(answerRecord.getIsCorrect());

        // 测试null值
        answerRecord.setIsCorrect(null);
        assertNull(answerRecord.getIsCorrect());
    }

    @Test
    @DisplayName("测试QuizAnswerRecord的字符串处理")
    void testStringValues() {
        // 测试空字符串
        answerRecord.setAbilityType("");
        assertEquals("", answerRecord.getAbilityType());

        // 测试特殊字符
        String specialText = "特殊字符测试：!@#$%^&*()_+-=[]{}|;':\",./<>?";
        answerRecord.setAbilityType(specialText);
        assertEquals(specialText, answerRecord.getAbilityType());

        // 测试中文字符
        answerRecord.setAbilityType("编程能力");
        assertEquals("编程能力", answerRecord.getAbilityType());

        // 测试null字符串
        answerRecord.setAbilityType(null);
        assertNull(answerRecord.getAbilityType());
    }

    @Test
    @DisplayName("测试QuizAnswerRecord的浮点数精度")
    void testFloatingPointPrecision() {
        // 测试高精度浮点数
        answerRecord.setDifficulty(0.123456789);
        answerRecord.setScore(99.999999);

        assertEquals(0.123456789, answerRecord.getDifficulty(), 0.000000001);
        assertEquals(99.999999, answerRecord.getScore(), 0.000001);
    }
} 