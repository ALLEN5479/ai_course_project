package com.example.ai_lesson.aiquestion.service;

import io.github.imfangs.dify.client.DifyChatClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ChatService {
    
    private final DifyChatClient chatClient;
    
    public ChatService(
            @Value("${custom.ai.api.url}") String apiBaseUrl,
            @Value("${custom.ai.api.key}") String apiKey) {
        this.chatClient = DifyClientFactory.createChatClient(apiBaseUrl, apiKey);
    }
    
    /**
     * 发送聊天消息到AI
     * @param message 用户消息
     * @param conversationId 会话ID（可选，用于保持对话上下文）
     * @return AI回复
     * @throws IOException 网络异常
     */
    public String sendMessage(String message, String conversationId) throws IOException {
        // 构建聊天消息
        ChatMessage chatMessage = ChatMessage.builder()
                .query(message)
                .user(conversationId != null ? conversationId : "user-" + System.currentTimeMillis())
                .responseMode(ResponseMode.BLOCKING)
                .build();
        
        // 发送消息到AI
        ChatMessageResponse response = chatClient.sendChatMessage(chatMessage);
        
        // 返回AI回复
        return response.getAnswer();
    }
    
    /**
     * 发送简单的编程相关问题
     * @param question 编程问题
     * @return AI回答
     * @throws IOException 网络异常
     */
    public String askProgrammingQuestion(String question) throws IOException {
        // 为编程问题添加上下文提示
        String enhancedQuestion = "作为一个编程导师，请回答以下问题：" + question;
        return sendMessage(enhancedQuestion, null);
    }
    
    /**
     * 获取代码解释
     * @param code 代码片段
     * @return 代码解释
     * @throws IOException 网络异常
     */
    public String explainCode(String code) throws IOException {
        String question = "请解释以下代码的功能和逻辑：\n```\n" + code + "\n```";
        return sendMessage(question, null);
    }
    
    /**
     * 获取编程建议
     * @param topic 编程主题
     * @return 学习建议
     * @throws IOException 网络异常
     */
    public String getProgrammingAdvice(String topic) throws IOException {
        String question = "关于" + topic + "，请给我一些学习建议和最佳实践。";
        return sendMessage(question, null);
    }
} 