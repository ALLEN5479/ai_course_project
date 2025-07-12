package com.example.ai_lesson.aiquestion.service;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import com.example.ai_lesson.aiquestion.mapper.QuizQuestionMapper;
import io.github.imfangs.dify.client.DifyChatClient;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuizService测试")
class QuizServiceTest {

    @Mock
    private QuizQuestionMapper mockQuestionMapper;

    @Mock
    private DifyChatClient mockChatClient;

    private QuizService quizService;

    @BeforeEach
    void setUp() {
        quizService = new QuizService(mockQuestionMapper, "http://test-api.com", "test-api-key");
        ReflectionTestUtils.setField(quizService, "chatClient", mockChatClient);
    }

    @Test
    @DisplayName("测试生成问题成功")
    void testGenerateQuestionsSuccess() throws IOException {
        // 准备测试数据
        int count = 2;
        String difficulty = "中等";
        String requirement = "关于Java基础";
        
        String aiResponse = "题目1：\n" +
                "问题：什么是Java中的多态？\n" +
                "A. 同一个接口，不同的实现\n" +
                "B. 同一个类，不同的方法\n" +
                "C. 同一个对象，不同的属性\n" +
                "D. 同一个方法，不同的参数\n" +
                "正确答案：A\n\n" +
                "题目2：\n" +
                "问题：Java中的继承是什么？\n" +
                "A. 一个类可以继承多个父类\n" +
                "B. 一个类只能继承一个父类\n" +
                "C. 一个类不能继承任何父类\n" +
                "D. 一个类可以继承接口\n" +
                "正确答案：B";

        // 模拟AI响应
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(aiResponse);
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        List<QuizQuestion> result = quizService.generateQuestions(count, difficulty, requirement);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        
        // 验证第一个问题
        QuizQuestion firstQuestion = result.get(0);
        assertEquals("什么是Java中的多态？", firstQuestion.getQuestion_text());
        assertEquals("同一个接口，不同的实现", firstQuestion.getOption_a());
        assertEquals("同一个类，不同的方法", firstQuestion.getOption_b());
        assertEquals("同一个对象，不同的属性", firstQuestion.getOption_c());
        assertEquals("同一个方法，不同的参数", firstQuestion.getOption_d());
        assertEquals("A", firstQuestion.getCorrect_answer());
        assertEquals("中等", firstQuestion.getDifficulty());
        assertEquals("编程", firstQuestion.getCategory());

        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试生成问题时AI服务异常")
    void testGenerateQuestionsWithIOException() throws IOException {
        // 准备测试数据
        int count = 2;
        String difficulty = "中等";
        String requirement = "关于Java基础";

        // 模拟AI服务异常
        when(mockChatClient.sendChatMessage(any(ChatMessage.class)))
                .thenThrow(new IOException("AI服务不可用"));

        // 执行测试并验证异常
        assertThrows(IOException.class, () -> {
            quizService.generateQuestions(count, difficulty, requirement);
        });

        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试保存单个问题")
    void testSaveQuestion() {
        // 准备测试数据
        QuizQuestion question = new QuizQuestion();
        question.setQuestion_text("测试问题");
        question.setOption_a("选项A");
        question.setOption_b("选项B");
        question.setOption_c("选项C");
        question.setOption_d("选项D");
        question.setCorrect_answer("A");
        question.setDifficulty("简单");
        question.setCategory("编程");

        // 模拟Mapper行为
        when(mockQuestionMapper.insert(any(QuizQuestion.class))).thenReturn(1);

        // 执行测试
        QuizQuestion result = quizService.saveQuestion(question);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试问题", result.getQuestion_text());
        verify(mockQuestionMapper, times(1)).insert(question);
    }

    @Test
    @DisplayName("测试批量保存问题")
    void testSaveQuestions() {
        // 准备测试数据
        QuizQuestion question1 = new QuizQuestion();
        question1.setQuestion_text("问题1");
        question1.setOption_a("A1");
        question1.setOption_b("B1");
        question1.setOption_c("C1");
        question1.setOption_d("D1");
        question1.setCorrect_answer("A");

        QuizQuestion question2 = new QuizQuestion();
        question2.setQuestion_text("问题2");
        question2.setOption_a("A2");
        question2.setOption_b("B2");
        question2.setOption_c("C2");
        question2.setOption_d("D2");
        question2.setCorrect_answer("B");

        List<QuizQuestion> questions = Arrays.asList(question1, question2);

        // 模拟Mapper行为
        when(mockQuestionMapper.batchInsert(anyList())).thenReturn(2);

        // 执行测试
        List<QuizQuestion> result = quizService.saveQuestions(questions);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockQuestionMapper, times(1)).batchInsert(questions);
    }

    @Test
    @DisplayName("测试批量保存空列表")
    void testSaveEmptyQuestions() {
        // 准备测试数据
        List<QuizQuestion> questions = Arrays.asList();

        // 执行测试
        List<QuizQuestion> result = quizService.saveQuestions(questions);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockQuestionMapper, never()).batchInsert(anyList());
    }

    @Test
    @DisplayName("测试获取所有问题")
    void testGetAllQuestions() {
        // 准备测试数据
        QuizQuestion question1 = new QuizQuestion();
        question1.setId(1);
        question1.setQuestion_text("问题1");

        QuizQuestion question2 = new QuizQuestion();
        question2.setId(2);
        question2.setQuestion_text("问题2");

        List<QuizQuestion> expectedQuestions = Arrays.asList(question1, question2);

        // 模拟Mapper行为
        when(mockQuestionMapper.selectAll()).thenReturn(expectedQuestions);

        // 执行测试
        List<QuizQuestion> result = quizService.getAllQuestions();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("问题1", result.get(0).getQuestion_text());
        assertEquals("问题2", result.get(1).getQuestion_text());
        verify(mockQuestionMapper, times(1)).selectAll();
    }

    @Test
    @DisplayName("测试根据ID获取问题")
    void testGetQuestionById() {
        // 准备测试数据
        Integer questionId = 1;
        QuizQuestion expectedQuestion = new QuizQuestion();
        expectedQuestion.setId(questionId);
        expectedQuestion.setQuestion_text("测试问题");

        // 模拟Mapper行为
        when(mockQuestionMapper.selectById(questionId)).thenReturn(expectedQuestion);

        // 执行测试
        Optional<QuizQuestion> result = quizService.getQuestionById(questionId);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals(questionId, result.get().getId());
        assertEquals("测试问题", result.get().getQuestion_text());
        verify(mockQuestionMapper, times(1)).selectById(questionId);
    }

    @Test
    @DisplayName("测试根据ID获取不存在的问题")
    void testGetQuestionByIdNotFound() {
        // 准备测试数据
        Integer questionId = 999;

        // 模拟Mapper行为
        when(mockQuestionMapper.selectById(questionId)).thenReturn(null);

        // 执行测试
        Optional<QuizQuestion> result = quizService.getQuestionById(questionId);

        // 验证结果
        assertFalse(result.isPresent());
        verify(mockQuestionMapper, times(1)).selectById(questionId);
    }

    @Test
    @DisplayName("测试根据难度获取问题")
    void testGetQuestionsByDifficulty() {
        // 准备测试数据
        String difficulty = "中等";
        QuizQuestion question1 = new QuizQuestion();
        question1.setId(1);
        question1.setQuestion_text("中等问题1");
        question1.setDifficulty(difficulty);

        QuizQuestion question2 = new QuizQuestion();
        question2.setId(2);
        question2.setQuestion_text("中等问题2");
        question2.setDifficulty(difficulty);

        List<QuizQuestion> expectedQuestions = Arrays.asList(question1, question2);

        // 模拟Mapper行为
        when(mockQuestionMapper.selectByDifficulty(difficulty)).thenReturn(expectedQuestions);

        // 执行测试
        List<QuizQuestion> result = quizService.getQuestionsByDifficulty(difficulty);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("中等问题1", result.get(0).getQuestion_text());
        assertEquals("中等问题2", result.get(1).getQuestion_text());
        verify(mockQuestionMapper, times(1)).selectByDifficulty(difficulty);
    }

    @Test
    @DisplayName("测试更新问题")
    void testUpdateQuestion() {
        // 准备测试数据
        Integer questionId = 1;
        QuizQuestion existingQuestion = new QuizQuestion();
        existingQuestion.setId(questionId);
        existingQuestion.setQuestion_text("原问题");

        QuizQuestion updateDetails = new QuizQuestion();
        updateDetails.setQuestion_text("更新后的问题");
        updateDetails.setOption_a("新选项A");
        updateDetails.setOption_b("新选项B");
        updateDetails.setOption_c("新选项C");
        updateDetails.setOption_d("新选项D");
        updateDetails.setCorrect_answer("B");
        updateDetails.setDifficulty("困难");
        updateDetails.setCategory("算法");

        // 模拟Mapper行为
        when(mockQuestionMapper.selectById(questionId)).thenReturn(existingQuestion);
        when(mockQuestionMapper.update(any(QuizQuestion.class))).thenReturn(1);

        // 执行测试
        Optional<QuizQuestion> result = quizService.updateQuestion(questionId, updateDetails);

        // 验证结果
        assertTrue(result.isPresent());
        assertEquals("更新后的问题", result.get().getQuestion_text());
        assertEquals("新选项A", result.get().getOption_a());
        assertEquals("新选项B", result.get().getOption_b());
        assertEquals("新选项C", result.get().getOption_c());
        assertEquals("新选项D", result.get().getOption_d());
        assertEquals("B", result.get().getCorrect_answer());
        assertEquals("困难", result.get().getDifficulty());
        assertEquals("算法", result.get().getCategory());
        
        verify(mockQuestionMapper, times(1)).selectById(questionId);
        verify(mockQuestionMapper, times(1)).update(any(QuizQuestion.class));
    }

    @Test
    @DisplayName("测试更新不存在的问题")
    void testUpdateQuestionNotFound() {
        // 准备测试数据
        Integer questionId = 999;
        QuizQuestion updateDetails = new QuizQuestion();
        updateDetails.setQuestion_text("更新后的问题");

        // 模拟Mapper行为
        when(mockQuestionMapper.selectById(questionId)).thenReturn(null);

        // 执行测试
        Optional<QuizQuestion> result = quizService.updateQuestion(questionId, updateDetails);

        // 验证结果
        assertFalse(result.isPresent());
        verify(mockQuestionMapper, times(1)).selectById(questionId);
        verify(mockQuestionMapper, never()).update(any(QuizQuestion.class));
    }

    @Test
    @DisplayName("测试删除问题")
    void testDeleteQuestion() {
        // 准备测试数据
        Integer questionId = 1;

        // 模拟Mapper行为
        when(mockQuestionMapper.deleteById(questionId)).thenReturn(1);

        // 执行测试
        boolean result = quizService.deleteQuestion(questionId);

        // 验证结果
        assertTrue(result);
        verify(mockQuestionMapper, times(1)).deleteById(questionId);
    }

    @Test
    @DisplayName("测试删除不存在的问题")
    void testDeleteQuestionNotFound() {
        // 准备测试数据
        Integer questionId = 999;

        // 模拟Mapper行为
        when(mockQuestionMapper.deleteById(questionId)).thenReturn(0);

        // 执行测试
        boolean result = quizService.deleteQuestion(questionId);

        // 验证结果
        assertFalse(result);
        verify(mockQuestionMapper, times(1)).deleteById(questionId);
    }

    @Test
    @DisplayName("测试解析AI响应格式1")
    void testParseSpecificFormat() throws IOException {
        // 准备测试数据
        int count = 1;
        String difficulty = "简单";
        String requirement = "";
        
        String aiResponse = "问题：[问题文本]\n" +
                "1. 什么是Java？\n" +
                "2. 什么是面向对象？\n" +
                "A: 选项A内容\n" +
                "B: 选项B内容\n" +
                "C: 选项C内容\n" +
                "D: 选项D内容\n" +
                "正确答案：\n" +
                "1. A\n" +
                "2. B";

        // 模拟AI响应
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(aiResponse);
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        List<QuizQuestion> result = quizService.generateQuestions(count, difficulty, requirement);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试解析AI响应格式2")
    void testParseQuestionBlockFormat() throws IOException {
        // 准备测试数据
        int count = 1;
        String difficulty = "简单";
        String requirement = "";
        
        String aiResponse = "题目1：\n" +
                "问题：什么是Java？\n" +
                "A. 编程语言\n" +
                "B. 操作系统\n" +
                "C. 数据库\n" +
                "D. 浏览器\n" +
                "正确答案：A";

        // 模拟AI响应
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(aiResponse);
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        List<QuizQuestion> result = quizService.generateQuestions(count, difficulty, requirement);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }
} 