package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.PaperQuestion;
import com.example.ai_lesson.mission.Service.PaperQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/paper-questions")
@CrossOrigin(origins = "*")
public class PaperQuestionController {
    
    @Autowired
    private PaperQuestionService paperQuestionService;
    
    @GetMapping("/paper/{paperId}")
    public ResponseEntity<Map<String, Object>> getPaperQuestionsByPaperId(@PathVariable Integer paperId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PaperQuestion> paperQuestions = paperQuestionService.getPaperQuestionsByPaperId(paperId);
            response.put("success", true);
            response.put("data", paperQuestions);
            response.put("message", "获取试卷题目列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取试卷题目列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPaperQuestionById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            PaperQuestion paperQuestion = paperQuestionService.getPaperQuestionById(id);
            if (paperQuestion != null) {
                response.put("success", true);
                response.put("data", paperQuestion);
                response.put("message", "获取试卷题目详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "试卷题目不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取试卷题目详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPaperQuestion(@RequestBody PaperQuestion paperQuestion) {
        Map<String, Object> response = new HashMap<>();
        try {
            PaperQuestion createdPaperQuestion = paperQuestionService.createPaperQuestion(paperQuestion);
            response.put("success", true);
            response.put("data", createdPaperQuestion);
            response.put("message", "创建试卷题目成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建试卷题目失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchCreatePaperQuestions(@RequestBody List<PaperQuestion> paperQuestions) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = paperQuestionService.batchCreatePaperQuestions(paperQuestions);
            if (success) {
                response.put("success", true);
                response.put("message", "批量创建试卷题目成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量创建试卷题目失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量创建试卷题目失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePaperQuestion(@PathVariable Integer id, 
                                                                  @RequestBody PaperQuestion paperQuestion) {
        Map<String, Object> response = new HashMap<>();
        try {
            paperQuestion.setId(id);
            boolean success = paperQuestionService.updatePaperQuestion(paperQuestion);
            if (success) {
                response.put("success", true);
                response.put("message", "更新试卷题目成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "试卷题目不存在或更新失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新试卷题目失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePaperQuestion(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = paperQuestionService.deletePaperQuestion(id);
            if (success) {
                response.put("success", true);
                response.put("message", "删除试卷题目成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "试卷题目不存在或删除失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除试卷题目失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/paper/{paperId}")
    public ResponseEntity<Map<String, Object>> deletePaperQuestionsByPaperId(@PathVariable Integer paperId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = paperQuestionService.deletePaperQuestionsByPaperId(paperId);
            if (success) {
                response.put("success", true);
                response.put("message", "删除试卷所有题目成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "删除试卷题目失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除试卷题目失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 