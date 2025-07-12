package com.example.ai_lesson.aiquestion.controller;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import com.example.ai_lesson.aiquestion.service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuizController测试")
class QuizControllerTest {

    @Mock
    private QuizService mockQuizService;

    @InjectMocks
    private QuizController quizController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("测试测试端点")
    void testTest() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/quiz/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Quiz API is working!"));
    }

    @Test
    @DisplayName("测试生成问题成功")
    void testGenerateQuestionsSuccess() throws Exception {
        // 准备测试数据
        QuizController.GenerateRequest request = new QuizController.GenerateRequest();
        request.setCount(2);
        request.setDifficulty("中等");
        request.setRequirement("关于Java基础");

        QuizQuestion question1 = new QuizQuestion();
        question1.setQuestion_text("什么是Java中的多态？");
        question1.setOption_a("同一个接口，不同的实现");
        question1.setOption_b("同一个类，不同的方法");
        question1.setOption_c("同一个对象，不同的属性");
        question1.setOption_d("同一个方法，不同的参数");
        question1.setCorrect_answer("A");
        question1.setDifficulty("中等");
        question1.setCategory("编程");

        QuizQuestion question2 = new QuizQuestion();
        question2.setQuestion_text("Java中的继承是什么？");
        question2.setOption_a("一个类可以继承多个父类");
        question2.setOption_b("一个类只能继承一个父类");
        question2.setOption_c("一个类不能继承任何父类");
        question2.setOption_d("一个类可以继承接口");
        question2.setCorrect_answer("B");
        question2.setDifficulty("中等");
        question2.setCategory("编程");

        List<QuizQuestion> expectedQuestions = Arrays.asList(question1, question2);

        // 模拟服务行为
        when(mockQuizService.generateQuestions(anyInt(), anyString(), anyString()))
                .thenReturn(expectedQuestions);

        // 执行测试
        mockMvc.perform(post("/api/quiz/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question_text").value("什么是Java中的多态？"))
                .andExpect(jsonPath("$[0].option_a").value("同一个接口，不同的实现"))
                .andExpect(jsonPath("$[0].correct_answer").value("A"))
                .andExpect(jsonPath("$[1].question_text").value("Java中的继承是什么？"))
                .andExpect(jsonPath("$[1].correct_answer").value("B"));

        // 验证服务调用
        verify(mockQuizService, times(1)).generateQuestions(2, "中等", "关于Java基础");
    }


    @Test
    @DisplayName("测试获取所有问题")
    void testGetAllQuestions() throws Exception {
        // 准备测试数据
        QuizQuestion question1 = new QuizQuestion();
        question1.setId(1);
        question1.setQuestion_text("问题1");

        QuizQuestion question2 = new QuizQuestion();
        question2.setId(2);
        question2.setQuestion_text("问题2");

        List<QuizQuestion> expectedQuestions = Arrays.asList(question1, question2);

        // 模拟服务行为
        when(mockQuizService.getAllQuestions()).thenReturn(expectedQuestions);

        // 执行测试
        mockMvc.perform(get("/api/quiz/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].question_text").value("问题1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].question_text").value("问题2"));

        // 验证服务调用
        verify(mockQuizService, times(1)).getAllQuestions();
    }



    @Test
    @DisplayName("测试根据ID获取问题")
    void testGetQuestionById() throws Exception {
        // 准备测试数据
        Integer questionId = 1;
        QuizQuestion expectedQuestion = new QuizQuestion();
        expectedQuestion.setId(questionId);
        expectedQuestion.setQuestion_text("测试问题");
        expectedQuestion.setOption_a("选项A");
        expectedQuestion.setOption_b("选项B");
        expectedQuestion.setOption_c("选项C");
        expectedQuestion.setOption_d("选项D");
        expectedQuestion.setCorrect_answer("A");

        // 模拟服务行为
        when(mockQuizService.getQuestionById(questionId))
                .thenReturn(Optional.of(expectedQuestion));

        // 执行测试
        mockMvc.perform(get("/api/quiz/{id}", questionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionId))
                .andExpect(jsonPath("$.question_text").value("测试问题"))
                .andExpect(jsonPath("$.option_a").value("选项A"))
                .andExpect(jsonPath("$.option_b").value("选项B"))
                .andExpect(jsonPath("$.option_c").value("选项C"))
                .andExpect(jsonPath("$.option_d").value("选项D"))
                .andExpect(jsonPath("$.correct_answer").value("A"));

        // 验证服务调用
        verify(mockQuizService, times(1)).getQuestionById(questionId);
    }



    @Test
    @DisplayName("测试根据难度获取问题")
    void testGetQuestionsByDifficulty() throws Exception {
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

        // 模拟服务行为
        when(mockQuizService.getQuestionsByDifficulty(difficulty))
                .thenReturn(expectedQuestions);

        // 执行测试
        mockMvc.perform(get("/api/quiz/difficulty/{difficulty}", difficulty))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].question_text").value("中等问题1"))
                .andExpect(jsonPath("$[0].difficulty").value(difficulty))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].question_text").value("中等问题2"))
                .andExpect(jsonPath("$[1].difficulty").value(difficulty));

        // 验证服务调用
        verify(mockQuizService, times(1)).getQuestionsByDifficulty(difficulty);
    }


    @Test
    @DisplayName("测试更新问题")
    void testUpdateQuestion() throws Exception {
        // 准备测试数据
        Integer questionId = 1;
        QuizQuestion updateDetails = new QuizQuestion();
        updateDetails.setQuestion_text("更新后的问题");
        updateDetails.setOption_a("新选项A");
        updateDetails.setOption_b("新选项B");
        updateDetails.setOption_c("新选项C");
        updateDetails.setOption_d("新选项D");
        updateDetails.setCorrect_answer("B");
        updateDetails.setDifficulty("困难");
        updateDetails.setCategory("算法");

        QuizQuestion updatedQuestion = new QuizQuestion();
        updatedQuestion.setId(questionId);
        updatedQuestion.setQuestion_text("更新后的问题");
        updatedQuestion.setOption_a("新选项A");
        updatedQuestion.setOption_b("新选项B");
        updatedQuestion.setOption_c("新选项C");
        updatedQuestion.setOption_d("新选项D");
        updatedQuestion.setCorrect_answer("B");
        updatedQuestion.setDifficulty("困难");
        updatedQuestion.setCategory("算法");

        // 模拟服务行为
        when(mockQuizService.updateQuestion(eq(questionId), any(QuizQuestion.class)))
                .thenReturn(Optional.of(updatedQuestion));

        // 执行测试
        mockMvc.perform(put("/api/quiz/{id}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(questionId))
                .andExpect(jsonPath("$.question_text").value("更新后的问题"))
                .andExpect(jsonPath("$.option_a").value("新选项A"))
                .andExpect(jsonPath("$.option_b").value("新选项B"))
                .andExpect(jsonPath("$.option_c").value("新选项C"))
                .andExpect(jsonPath("$.option_d").value("新选项D"))
                .andExpect(jsonPath("$.correct_answer").value("B"))
                .andExpect(jsonPath("$.difficulty").value("困难"))
                .andExpect(jsonPath("$.category").value("算法"));

        // 验证服务调用
        verify(mockQuizService, times(1)).updateQuestion(eq(questionId), any(QuizQuestion.class));
    }


    @Test
    @DisplayName("测试删除问题")
    void testDeleteQuestion() throws Exception {
        // 准备测试数据
        Integer questionId = 1;

        // 模拟服务行为
        when(mockQuizService.deleteQuestion(questionId)).thenReturn(true);

        // 执行测试
        mockMvc.perform(delete("/api/quiz/{id}", questionId))
                .andExpect(status().isNoContent());

        // 验证服务调用
        verify(mockQuizService, times(1)).deleteQuestion(questionId);
    }



    @Test
    @DisplayName("测试保存单个问题")
    void testSaveQuestion() throws Exception {
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

        QuizQuestion savedQuestion = new QuizQuestion();
        savedQuestion.setId(1);
        savedQuestion.setQuestion_text("测试问题");
        savedQuestion.setOption_a("选项A");
        savedQuestion.setOption_b("选项B");
        savedQuestion.setOption_c("选项C");
        savedQuestion.setOption_d("选项D");
        savedQuestion.setCorrect_answer("A");
        savedQuestion.setDifficulty("简单");
        savedQuestion.setCategory("编程");

        // 模拟服务行为
        when(mockQuizService.saveQuestion(any(QuizQuestion.class)))
                .thenReturn(savedQuestion);

        // 执行测试
        mockMvc.perform(post("/api/quiz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(question)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question_text").value("测试问题"))
                .andExpect(jsonPath("$.option_a").value("选项A"))
                .andExpect(jsonPath("$.option_b").value("选项B"))
                .andExpect(jsonPath("$.option_c").value("选项C"))
                .andExpect(jsonPath("$.option_d").value("选项D"))
                .andExpect(jsonPath("$.correct_answer").value("A"))
                .andExpect(jsonPath("$.difficulty").value("简单"))
                .andExpect(jsonPath("$.category").value("编程"));

        // 验证服务调用
        verify(mockQuizService, times(1)).saveQuestion(any(QuizQuestion.class));
    }



    @Test
    @DisplayName("测试GenerateRequest的getter和setter方法")
    void testGenerateRequestGettersAndSetters() {
        // 创建GenerateRequest对象
        QuizController.GenerateRequest request = new QuizController.GenerateRequest();
        
        // 设置值
        request.setCount(5);
        request.setDifficulty("困难");
        request.setRequirement("关于算法");
        
        // 验证getter方法
        assertEquals(5, request.getCount());
        assertEquals("困难", request.getDifficulty());
        assertEquals("关于算法", request.getRequirement());
    }
} 