package com.example.ai_lesson.aiquestion.controller;

import com.example.ai_lesson.aiquestion.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatController测试")
class ChatControllerTest {

    @Mock
    private ChatService mockChatService;

    @InjectMocks
    private ChatController chatController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("测试发送聊天消息成功")
    void testSendMessageSuccess() throws Exception {
        // 准备测试数据
        ChatController.ChatRequest request = new ChatController.ChatRequest();
        request.setMessage("你好，请介绍一下Java编程");
        request.setConversationId("test-conversation-123");

        String expectedResponse = "Java是一种面向对象的编程语言...";

        // 模拟服务行为
        when(mockChatService.sendMessage(anyString(), anyString())).thenReturn(expectedResponse);

        // 执行测试
        mockMvc.perform(post("/api/chat/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse));

        // 验证服务调用
        verify(mockChatService, times(1)).sendMessage("你好，请介绍一下Java编程", "test-conversation-123");
    }

    @Test
    @DisplayName("测试发送聊天消息时服务异常")
    void testSendMessageWithServiceException() throws Exception {
        // 准备测试数据
        ChatController.ChatRequest request = new ChatController.ChatRequest();
        request.setMessage("测试消息");
        request.setConversationId("test-conversation");

        // 模拟服务异常
        when(mockChatService.sendMessage(anyString(), anyString()))
                .thenThrow(new IOException("AI服务不可用"));

        // 执行测试
        mockMvc.perform(post("/api/chat/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("AI服务暂时不可用，请稍后重试"));

        // 验证服务调用
        verify(mockChatService, times(1)).sendMessage("测试消息", "test-conversation");
    }

    @Test
    @DisplayName("测试发送带文件内容的聊天消息")
    void testSendMessageWithFileContent() throws Exception {
        // 准备测试数据
        ChatController.ChatRequest request = new ChatController.ChatRequest();
        request.setMessage("请分析这个文件");
        request.setConversationId("test-conversation");
        request.setFileContent("文件内容：Java编程基础");

        String expectedResponse = "根据文件内容分析...";

        // 模拟服务行为
        when(mockChatService.sendMessage(anyString(), anyString())).thenReturn(expectedResponse);

        // 执行测试
        mockMvc.perform(post("/api/chat/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse));

        // 验证服务调用 - 应该包含文件内容
        verify(mockChatService, times(1)).sendMessage(
                contains("请结合以下文件内容回答问题"), 
                eq("test-conversation")
        );
    }

    @Test
    @DisplayName("测试上传文本文件")
    void testUploadTextFile() throws Exception {
        // 准备测试数据
        String fileName = "test.txt";
        String fileContent = "这是一个文本文件的内容";
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                fileContent.getBytes()
        );

        // 执行测试
        mockMvc.perform(multipart("/api/chat/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value(fileName))
                .andExpect(jsonPath("$.fileContent").value(fileContent));
    }


    @Test
    @DisplayName("测试发送空消息")
    void testSendEmptyMessage() throws Exception {
        // 准备测试数据
        ChatController.ChatRequest request = new ChatController.ChatRequest();
        request.setMessage("");
        request.setConversationId("test-conversation");

        String expectedResponse = "AI回复";

        // 模拟服务行为
        when(mockChatService.sendMessage(anyString(), anyString())).thenReturn(expectedResponse);

        // 执行测试
        mockMvc.perform(post("/api/chat/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(expectedResponse));

        // 验证服务调用
        verify(mockChatService, times(1)).sendMessage("", "test-conversation");
    }

    @Test
    @DisplayName("测试ChatRequest的getter和setter方法")
    void testChatRequestGettersAndSetters() {
        // 创建ChatRequest对象
        ChatController.ChatRequest request = new ChatController.ChatRequest();
        
        // 设置值
        request.setMessage("测试消息");
        request.setConversationId("test-conversation");
        request.setFileContent("文件内容");
        
        // 验证getter方法
        assertEquals("测试消息", request.getMessage());
        assertEquals("test-conversation", request.getConversationId());
        assertEquals("文件内容", request.getFileContent());
        
        // 测试setFileContent为null
        request.setFileContent(null);
        assertNull(request.getFileContent());
    }
} 