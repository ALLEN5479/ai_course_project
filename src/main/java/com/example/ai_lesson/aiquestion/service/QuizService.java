package com.example.ai_lesson.aiquestion.service;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import com.example.ai_lesson.aiquestion.mapper.QuizQuestionMapper;
import io.github.imfangs.dify.client.DifyChatClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuizService {
    private final QuizQuestionMapper questionMapper;
    private final DifyChatClient chatClient;

    public QuizService(
            QuizQuestionMapper questionMapper,
            @Value("${custom.ai.api.url}") String apiBaseUrl,
            @Value("${custom.ai.api.key}") String apiKey) {
        this.questionMapper = questionMapper;
        this.chatClient = DifyClientFactory.createChatClient(apiBaseUrl, apiKey);
    }

    // 生成多个问题
    public List<QuizQuestion> generateQuestions(int count, String difficulty, String requirement) throws IOException {
        // 构建提示词以引导AI生成符合格式的选择题
        String prompt = String.format(
                "请生成%d道关于编程的%s难度选择题，包含4个选项(A-D)和正确答案。" +
                        "请使用以下格式回答：\n\n" +
                        "问题：[问题文本]\n" +
                        "选项：\n" +
                        "A. [选项A文本]\n" +
                        "B. [选项B文本]\n" +
                        "C. [选项C文本]\n" +
                        "D. [选项D文本]\n" +
                        "正确答案：[A/B/C/D] \n" +
                        "额外要求：%s",
                count, difficulty, requirement
        );

        ChatMessage message = ChatMessage.builder()
                .query(prompt)
                .user("quiz-generator-" + System.currentTimeMillis())
                .responseMode(ResponseMode.BLOCKING)
                .build();

        ChatMessageResponse response = chatClient.sendChatMessage(message);
        String answer = response.getAnswer();

        // 解析AI返回的多个问题
        return parseMultipleQuestions(answer, count, difficulty);
    }

    // 解析多个问题
    private List<QuizQuestion> parseMultipleQuestions(String answer, int expectedCount, String difficulty) {
        List<QuizQuestion> questions = new ArrayList<>();

        // 定义问题分隔模式
        Pattern questionSeparator = Pattern.compile("问题：(.+?)(?=问题：|$)", Pattern.DOTALL);
        Matcher matcher = questionSeparator.matcher(answer);

        while (matcher.find()) {
            String questionText = "问题：" + matcher.group(1);
            QuizQuestion question = parseSingleQuestion(questionText);
            if (question != null) {
                question.setDifficulty(difficulty);
                question.setCategory("编程");
                questions.add(question);
            }
        }

        // 如果解析的问题数量不足，添加默认问题
        if (questions.size() < expectedCount) {
            for (int i = questions.size(); i < expectedCount; i++) {
                QuizQuestion defaultQuestion = createDefaultQuestion(difficulty);
                questions.add(defaultQuestion);
            }
        }

        return questions;
    }

    // 解析单个问题
    private QuizQuestion parseSingleQuestion(String questionText) {
        QuizQuestion question = new QuizQuestion();

        // 使用正则表达式提取问题、选项和答案
        Pattern questionPattern = Pattern.compile("问题：(.+?)(?=选项：|$)", Pattern.DOTALL);
        Pattern optionAPattern = Pattern.compile("A\\.\\s+(.+?)(?=B\\.|$)", Pattern.DOTALL);
        Pattern optionBPattern = Pattern.compile("B\\.\\s+(.+?)(?=C\\.|$)", Pattern.DOTALL);
        Pattern optionCPattern = Pattern.compile("C\\.\\s+(.+?)(?=D\\.|$)", Pattern.DOTALL);
        Pattern optionDPattern = Pattern.compile("D\\.\\s+(.+?)(?=正确答案|$)", Pattern.DOTALL);
        Pattern answerPattern = Pattern.compile("正确答案：\\s*([A-D])", Pattern.DOTALL);

        Matcher questionMatcher = questionPattern.matcher(questionText);
        if (questionMatcher.find()) {
            question.setQuestionText(questionMatcher.group(1).trim());
        }

        Matcher optionAMatcher = optionAPattern.matcher(questionText);
        if (optionAMatcher.find()) {
            question.setOptionA(optionAMatcher.group(1).trim());
        }

        Matcher optionBMatcher = optionBPattern.matcher(questionText);
        if (optionBMatcher.find()) {
            question.setOptionB(optionBMatcher.group(1).trim());
        }

        Matcher optionCMatcher = optionCPattern.matcher(questionText);
        if (optionCMatcher.find()) {
            question.setOptionC(optionCMatcher.group(1).trim());
        }

        Matcher optionDMatcher = optionDPattern.matcher(questionText);
        if (optionDMatcher.find()) {
            question.setOptionD(optionDMatcher.group(1).trim());
        }

        Matcher answerMatcher = answerPattern.matcher(questionText);
        if (answerMatcher.find()) {
            question.setCorrectAnswer(answerMatcher.group(1).trim());
        }

        return question;
    }

    // 创建默认问题（当AI未能生成足够问题时使用）
    private QuizQuestion createDefaultQuestion(String difficulty) {
        QuizQuestion question = new QuizQuestion();
        question.setQuestionText("默认问题（AI未能生成足够问题）");
        question.setOptionA("选项A");
        question.setOptionB("选项B");
        question.setOptionC("选项C");
        question.setOptionD("选项D");
        question.setCorrectAnswer("A");
        question.setDifficulty(difficulty);
        question.setCategory("编程");
        return question;
    }

    // 保存单个问题
    public QuizQuestion saveQuestion(QuizQuestion question) {
        questionMapper.insert(question);
        return question;
    }

    // 批量保存问题
    public List<QuizQuestion> saveQuestions(List<QuizQuestion> questions) {
        if (!questions.isEmpty()) {
            questionMapper.batchInsert(questions);
        }
        return questions;
    }

    // 获取所有问题
    public List<QuizQuestion> getAllQuestions() {
        return questionMapper.selectAll();
    }

    // 根据ID获取问题
    public Optional<QuizQuestion> getQuestionById(Integer id) {
        return Optional.ofNullable(questionMapper.selectById(id));
    }

    // 根据难度获取问题
    public List<QuizQuestion> getQuestionsByDifficulty(String difficulty) {
        return questionMapper.selectByDifficulty(difficulty);
    }

    // 更新问题
    public Optional<QuizQuestion> updateQuestion(Integer id, QuizQuestion questionDetails) {
        return getQuestionById(id)
                .map(existingQuestion -> {
                    existingQuestion.setQuestionText(questionDetails.getQuestionText());
                    existingQuestion.setOptionA(questionDetails.getOptionA());
                    existingQuestion.setOptionB(questionDetails.getOptionB());
                    existingQuestion.setOptionC(questionDetails.getOptionC());
                    existingQuestion.setOptionD(questionDetails.getOptionD());
                    existingQuestion.setCorrectAnswer(questionDetails.getCorrectAnswer());
                    existingQuestion.setDifficulty(questionDetails.getDifficulty());
                    existingQuestion.setCategory(questionDetails.getCategory());
                    questionMapper.update(existingQuestion);
                    return existingQuestion;
                });
    }

    // 删除问题
    public boolean deleteQuestion(Integer id) {
        int rows = questionMapper.deleteById(id);
        return rows > 0;
    }
}