package com.example.ai_lesson.aiquestion.service;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatService测试")
class ChatServiceTest {

    @Mock
    private DifyChatClient mockChatClient;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        // 使用反射设置私有字段
        chatService = new ChatService("http://test-api.com", "test-api-key");
        ReflectionTestUtils.setField(chatService, "chatClient", mockChatClient);
    }

    @Test
    @DisplayName("测试正常发送消息")
    void testSendMessageSuccess() throws IOException {
        // 准备测试数据
        String testMessage = "你好，请介绍一下Java编程";
        String conversationId = "test-conversation-123";
        String expectedResponse = "Java是一种面向对象的编程语言...";

        // 模拟ChatMessageResponse
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(expectedResponse);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.sendMessage(testMessage, conversationId);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试发送消息时conversationId为null")
    void testSendMessageWithNullConversationId() throws IOException {
        // 准备测试数据
        String testMessage = "测试消息";
        String expectedResponse = "AI回复内容";

        // 模拟ChatMessageResponse
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(expectedResponse);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.sendMessage(testMessage, null);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试发送消息时发生IOException")
    void testSendMessageWithIOException() throws IOException {
        // 准备测试数据
        String testMessage = "测试消息";
        String conversationId = "test-conversation";

        // 模拟客户端抛出异常
        when(mockChatClient.sendChatMessage(any(ChatMessage.class)))
                .thenThrow(new IOException("网络连接失败"));

        // 执行测试并验证异常
        assertThrows(IOException.class, () -> {
            chatService.sendMessage(testMessage, conversationId);
        });

        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试发送空消息")
    void testSendEmptyMessage() throws IOException {
        // 准备测试数据
        String testMessage = "";
        String conversationId = "test-conversation";
        String expectedResponse = "AI回复";

        // 模拟ChatMessageResponse
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(expectedResponse);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.sendMessage(testMessage, conversationId);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试发送null消息")
    void testSendNullMessage() throws IOException {
        // 准备测试数据
        String testMessage = null;
        String conversationId = "test-conversation";
        String expectedResponse = "AI回复";

        // 模拟ChatMessageResponse
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(expectedResponse);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.sendMessage(testMessage, conversationId);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试askProgrammingQuestion方法")
    void testAskProgrammingQuestion() throws IOException {
        // 准备测试数据
        String question = "什么是多态？";
        String expectedResponse = "多态是面向对象编程的一个重要特性...";

        // 模拟ChatMessageResponse
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(expectedResponse);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.askProgrammingQuestion(question);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试explainCode方法")
    void testExplainCode() throws IOException {
        // 准备测试数据
        String code = "public class Test { public static void main(String[] args) { System.out.println(\"Hello\"); } }";
        String expectedResponse = "这段代码定义了一个Test类...";

        // 模拟ChatMessageResponse
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(expectedResponse);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.explainCode(code);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试getProgrammingAdvice方法")
    void testGetProgrammingAdvice() throws IOException {
        // 准备测试数据
        String topic = "Java集合框架";
        String expectedResponse = "关于Java集合框架的学习建议...";

        // 模拟ChatMessageResponse
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(expectedResponse);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.getProgrammingAdvice(topic);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试AI返回null响应")
    void testSendMessageWithNullResponse() throws IOException {
        // 准备测试数据
        String testMessage = "测试消息";
        String conversationId = "test-conversation";

        // 模拟ChatMessageResponse返回null
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn(null);

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.sendMessage(testMessage, conversationId);

        // 验证结果
        assertNull(result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试AI返回空字符串响应")
    void testSendMessageWithEmptyResponse() throws IOException {
        // 准备测试数据
        String testMessage = "测试消息";
        String conversationId = "test-conversation";

        // 模拟ChatMessageResponse返回空字符串
        ChatMessageResponse mockResponse = mock(ChatMessageResponse.class);
        when(mockResponse.getAnswer()).thenReturn("");

        // 模拟客户端行为
        when(mockChatClient.sendChatMessage(any(ChatMessage.class))).thenReturn(mockResponse);

        // 执行测试
        String result = chatService.sendMessage(testMessage, conversationId);

        // 验证结果
        assertEquals("", result);
        verify(mockChatClient, times(1)).sendChatMessage(any(ChatMessage.class));
    }
} 