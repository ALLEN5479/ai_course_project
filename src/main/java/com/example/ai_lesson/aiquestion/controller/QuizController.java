package com.example.ai_lesson.aiquestion.controller;

import com.example.ai_lesson.aiquestion.entity.QuizQuestion;
import com.example.ai_lesson.aiquestion.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // 请求体类
    public static class GenerateRequest {
        private int count;
        private String difficulty;
        private String requirement;

        // Getters and Setters
        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getRequirement() {
            return requirement;
        }

        public void setRequirement(String requirement) {
            this.requirement = requirement;
        }
    }

    // 测试端点
    @GetMapping("/test")
    public String test() {
        return "Quiz API is working!";
    }

    // 生成多个问题
    @PostMapping("/generate")
    public List<QuizQuestion> generateQuestions(@RequestBody GenerateRequest request) {
        try {
            // 只生成，不保存
            return quizService.generateQuestions(
                request.getCount(),
                request.getDifficulty(),
                request.getRequirement()
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AI服务调用失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误: " + e.getMessage());
        }
    }

    // 获取所有问题
    @GetMapping("/all")
    public List<QuizQuestion> getAllQuestions() {
        try {
            return quizService.getAllQuestions();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "获取问题列表失败: " + e.getMessage());
        }
    }

    // 根据ID获取问题
    @GetMapping("/{id}")
    public QuizQuestion getQuestionById(@PathVariable int id) {
        try {
            return quizService.getQuestionById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "问题不存在"));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "获取问题失败: " + e.getMessage());
        }
    }

    // 根据难度获取问题
    @GetMapping("/difficulty/{difficulty}")
    public List<QuizQuestion> getQuestionsByDifficulty(@PathVariable String difficulty) {
        try {
            return quizService.getQuestionsByDifficulty(difficulty);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "获取问题失败: " + e.getMessage());
        }
    }

    // 更新问题
    @PutMapping("/{id}")
    public QuizQuestion updateQuestion(
            @PathVariable int id,
            @RequestBody QuizQuestion question) {
        try {
            return quizService.updateQuestion(id, question)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "问题不存在"));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "更新问题失败: " + e.getMessage());
        }
    }

    // 删除问题
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable int id) {
        try {
            if (!quizService.deleteQuestion(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "问题不存在");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "删除问题失败: " + e.getMessage());
        }
    }

    // 保存单个题目
    @PostMapping("")
    public QuizQuestion saveQuestion(@RequestBody QuizQuestion question) {
        try {
            return quizService.saveQuestion(question);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "保存题目失败: " + e.getMessage());
        }
    }
}