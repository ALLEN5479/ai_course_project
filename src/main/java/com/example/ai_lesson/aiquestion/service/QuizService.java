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
        // 构建更明确的提示词以引导AI生成符合格式的选择题
        String prompt = String.format(
                "请生成%d道关于编程的%s难度选择题。额外要求：%s(如额外要求后没写信息则没有额外要求)。每道题必须严格按照以下格式输出：\n\n" +
                "题目1：\n" +
                "问题：[具体问题内容]\n" +
                "A. [选项A内容]\n" +
                "B. [选项B内容]\n" +
                "C. [选项C内容]\n" +
                "D. [选项D内容]\n" +
                "正确答案：[A/B/C/D]\n\n" +
                "题目2：\n" +
                "问题：[具体问题内容]\n" +
                "A. [选项A内容]\n" +
                "B. [选项B内容]\n" +
                "C. [选项C内容]\n" +
                "D. [选项D内容]\n" +
                "正确答案：[A/B/C/D]\n\n" +
                "请确保每道题都有完整的问题、4个选项和正确答案。",
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

        // 首先尝试解析你提到的具体格式
        questions = parseSpecificFormat(answer, difficulty);

        // 如果解析失败，尝试其他格式
        if (questions.isEmpty()) {
            // 尝试按"题目X："分割
            Pattern questionBlockPattern = Pattern.compile("题目\\d+：\\s*\\n(.*?)(?=题目\\d+：|$)", Pattern.DOTALL);
            Matcher blockMatcher = questionBlockPattern.matcher(answer);

            while (blockMatcher.find()) {
                String questionBlock = blockMatcher.group(1);
                QuizQuestion question = parseSingleQuestion(questionBlock);
                if (question != null && isValidQuestion(question)) {
                    question.setDifficulty(difficulty);
                    question.setCategory("编程");
                    questions.add(question);
                }
            }
        }

        // 如果还是没有找到，尝试按数字编号分割
        if (questions.isEmpty()) {
            Pattern numberedPattern = Pattern.compile("\\d+\\..*?(?=\\d+\\.|$)", Pattern.DOTALL);
            Matcher numberedMatcher = numberedPattern.matcher(answer);
            
            while (numberedMatcher.find()) {
                String questionBlock = numberedMatcher.group(0);
                QuizQuestion question = parseSingleQuestion(questionBlock);
                if (question != null && isValidQuestion(question)) {
                    question.setDifficulty(difficulty);
                    question.setCategory("编程");
                    questions.add(question);
                }
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

    // 解析你提到的具体格式
    private List<QuizQuestion> parseSpecificFormat(String answer, String difficulty) {
        List<QuizQuestion> questions = new ArrayList<>();
        
        try {
            // 提取问题部分
            String questionPart = "";
            if (answer.contains("问题：[问题文本]")) {
                int start = answer.indexOf("问题：[问题文本]");
                int end = answer.indexOf("A:");
                if (end == -1) end = answer.length();
                questionPart = answer.substring(start, end);
            }
            
            // 提取选项部分
            String optionsPart = "";
            if (answer.contains("A:") && answer.contains("B:") && answer.contains("C:") && answer.contains("D:")) {
                int start = answer.indexOf("A:");
                int end = answer.indexOf("正确答案：");
                if (end == -1) end = answer.length();
                optionsPart = answer.substring(start, end);
            }
            
            // 提取答案部分
            String answerPart = "";
            if (answer.contains("正确答案：")) {
                int start = answer.indexOf("正确答案：");
                answerPart = answer.substring(start);
            }
            
            // 解析问题
            List<String> questionTexts = new ArrayList<>();
            Pattern questionPattern = Pattern.compile("\\d+\\..*?(?=\\d+\\.|A:|$)", Pattern.DOTALL);
            Matcher questionMatcher = questionPattern.matcher(questionPart);
            while (questionMatcher.find()) {
                String questionText = questionMatcher.group(0).replaceAll("^\\d+\\.\\s*", "").trim();
                if (!questionText.isEmpty()) {
                    questionTexts.add(questionText);
                }
            }
            
            // 解析选项
            String optionA = "", optionB = "", optionC = "", optionD = "";
            Pattern optionPattern = Pattern.compile("([A-D]):\\s*(.+?)(?=\\s*[A-D]:|正确答案|$)", Pattern.DOTALL);
            Matcher optionMatcher = optionPattern.matcher(optionsPart);
            while (optionMatcher.find()) {
                String option = optionMatcher.group(1);
                String content = optionMatcher.group(2).trim();
                switch (option) {
                    case "A": optionA = content; break;
                    case "B": optionB = content; break;
                    case "C": optionC = content; break;
                    case "D": optionD = content; break;
                }
            }
            
            // 解析正确答案
            List<String> correctAnswers = new ArrayList<>();
            Pattern correctPattern = Pattern.compile("\\d+\\.\\s*([A-D])");
            Matcher correctMatcher = correctPattern.matcher(answerPart);
            while (correctMatcher.find()) {
                correctAnswers.add(correctMatcher.group(1));
            }
            
            // 创建问题对象
            for (int i = 0; i < questionTexts.size(); i++) {
                QuizQuestion question = new QuizQuestion();
                question.setQuestion_text(questionTexts.get(i));
                question.setOption_a(optionA);
                question.setOption_b(optionB);
                question.setOption_c(optionC);
                question.setOption_d(optionD);
                
                // 设置正确答案
                if (i < correctAnswers.size()) {
                    question.setCorrect_answer(correctAnswers.get(i));
                } else {
                    question.setCorrect_answer("A"); // 默认答案
                }
                
                question.setDifficulty(difficulty);
                question.setCategory("编程");
                
                if (isValidQuestion(question)) {
                    questions.add(question);
                }
            }
            
        } catch (Exception e) {
            System.err.println("解析特定格式时出错: " + e.getMessage());
        }
        
        return questions;
    }

    // 解析单个问题
    private QuizQuestion parseSingleQuestion(String questionText) {
        QuizQuestion question = new QuizQuestion();

        // 清理文本，移除多余的空白字符
        questionText = questionText.trim();

        // 提取问题内容 - 支持多种格式
        Pattern questionPattern = Pattern.compile("问题：\\s*(.+?)(?=\\s*A\\.|\\s*选项|$)", Pattern.DOTALL);
        Matcher questionMatcher = questionPattern.matcher(questionText);
        if (questionMatcher.find()) {
            String questionContent = questionMatcher.group(1).trim();
            // 移除可能的编号
            questionContent = questionContent.replaceAll("^\\d+\\.\\s*", "");
            question.setQuestion_text(questionContent);
        }

        // 提取选项 - 支持多种格式
        Pattern optionAPattern = Pattern.compile("A\\.\\s*(.+?)(?=\\s*B\\.|\\s*正确答案|$)", Pattern.DOTALL);
        Pattern optionBPattern = Pattern.compile("B\\.\\s*(.+?)(?=\\s*C\\.|\\s*正确答案|$)", Pattern.DOTALL);
        Pattern optionCPattern = Pattern.compile("C\\.\\s*(.+?)(?=\\s*D\\.|\\s*正确答案|$)", Pattern.DOTALL);
        Pattern optionDPattern = Pattern.compile("D\\.\\s*(.+?)(?=\\s*正确答案|$)", Pattern.DOTALL);

        Matcher optionAMatcher = optionAPattern.matcher(questionText);
        if (optionAMatcher.find()) {
            question.setOption_a(optionAMatcher.group(1).trim());
        }

        Matcher optionBMatcher = optionBPattern.matcher(questionText);
        if (optionBMatcher.find()) {
            question.setOption_b(optionBMatcher.group(1).trim());
        }

        Matcher optionCMatcher = optionCPattern.matcher(questionText);
        if (optionCMatcher.find()) {
            question.setOption_c(optionCMatcher.group(1).trim());
        }

        Matcher optionDMatcher = optionDPattern.matcher(questionText);
        if (optionDMatcher.find()) {
            question.setOption_d(optionDMatcher.group(1).trim());
        }

        // 提取正确答案 - 支持多种格式
        Pattern answerPattern = Pattern.compile("正确答案：\\s*([A-D])", Pattern.DOTALL);
        Matcher answerMatcher = answerPattern.matcher(questionText);
        if (answerMatcher.find()) {
            question.setCorrect_answer(answerMatcher.group(1).trim());
        }

        return question;
    }

    // 验证问题是否有效
    private boolean isValidQuestion(QuizQuestion question) {
        return question.getQuestion_text() != null && !question.getQuestion_text().trim().isEmpty() &&
               question.getOption_a() != null && !question.getOption_a().trim().isEmpty() &&
               question.getOption_b() != null && !question.getOption_b().trim().isEmpty() &&
               question.getOption_c() != null && !question.getOption_c().trim().isEmpty() &&
               question.getOption_d() != null && !question.getOption_d().trim().isEmpty() &&
               question.getCorrect_answer() != null && !question.getCorrect_answer().trim().isEmpty();
    }

    // 创建默认问题（当AI未能生成足够问题时使用）
    private QuizQuestion createDefaultQuestion(String difficulty) {
        QuizQuestion question = new QuizQuestion();
        question.setQuestion_text("默认问题（AI未能生成足够问题）");
        question.setOption_a("选项A");
        question.setOption_b("选项B");
        question.setOption_c("选项C");
        question.setOption_d("选项D");
        question.setCorrect_answer("A");
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
                    existingQuestion.setQuestion_text(questionDetails.getQuestion_text());
                    existingQuestion.setOption_a(questionDetails.getOption_a());
                    existingQuestion.setOption_b(questionDetails.getOption_b());
                    existingQuestion.setOption_c(questionDetails.getOption_c());
                    existingQuestion.setOption_d(questionDetails.getOption_d());
                    existingQuestion.setCorrect_answer(questionDetails.getCorrect_answer());
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