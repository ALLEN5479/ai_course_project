package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.Paper;
import com.example.ai_lesson.mission.Service.PaperService;
import com.example.ai_lesson.mission.Service.PaperQuestionService;
import com.example.ai_lesson.mission.Domain.PaperQuestion;
import com.example.ai_lesson.mission.Mapper.PaperQuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/papers")
@CrossOrigin(origins = "*")
public class PaperController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaperController.class);
    
    @Autowired
    private PaperService paperService;
    
    @Autowired
    private PaperQuestionService paperQuestionService;
    
    @Autowired
    private PaperQuestionMapper paperQuestionMapper;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPapers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Paper> papers = paperService.getAllPapers();
            response.put("success", true);
            response.put("data", papers);
            response.put("message", "获取试卷列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取试卷列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /*@GetMapping("/{paperId}")
    public ResponseEntity<Map<String, Object>> getPaperById(@PathVariable Integer paperId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Paper paper = paperService.getPaperById(paperId);
            if (paper != null) {
                response.put("success", true);
                response.put("data", paper);
                response.put("message", "获取试卷详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "试卷不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取试卷详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }*/
    
    /*@PostMapping
    public ResponseEntity<Map<String, Object>> createPaper(@RequestBody Paper paper) {
        Map<String, Object> response = new HashMap<>();
        try {
            Paper createdPaper = paperService.createPaper(paper);
            response.put("success", true);
            response.put("data", createdPaper);
            response.put("message", "创建试卷成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建试卷失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }*/
    
    @PutMapping("/{paperId}")
    public ResponseEntity<Map<String, Object>> updatePaper(@PathVariable Integer paperId, 
                                                          @RequestBody Paper paper) {
        Map<String, Object> response = new HashMap<>();
        try {
            paper.setPaperId(paperId);
            boolean success = paperService.updatePaper(paper);
            if (success) {
                response.put("success", true);
                response.put("message", "更新试卷成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "试卷不存在或更新失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新试卷失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/{paperId}")
    public ResponseEntity<Map<String, Object>> deletePaper(@PathVariable Integer paperId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = paperService.deletePaper(paperId);
            if (success) {
                response.put("success", true);
                response.put("message", "删除试卷成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "试卷不存在或删除失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除试卷失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getPapersByCourseId(@RequestParam Integer courseId) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("[PaperController] 查询试卷列表, courseId={}", courseId);
            List<Paper> papers = paperService.getPapersByCourseId(courseId);
            logger.info("[PaperController] 查询结果: {}", papers);
            response.put("success", true);
            response.put("data", papers);
            response.put("message", "获取试卷列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("[PaperController] 获取试卷列表失败", e);
            response.put("success", false);
            response.put("message", "获取试卷列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPaperWithQuestions(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("[PaperController] 创建试卷, payload={}", payload);
            Paper paper = new Paper();
            paper.setPaperName((String) payload.get("paper_name"));
            paper.setPaperDescription((String) payload.get("paper_description"));
            // total_score类型健壮处理
            Object totalScoreObj = payload.get("total_score");
            if (totalScoreObj instanceof Integer) {
                paper.setTotalScore((Integer) totalScoreObj);
            } else if (totalScoreObj instanceof Number) {
                paper.setTotalScore(((Number) totalScoreObj).intValue());
            } else if (totalScoreObj != null) {
                paper.setTotalScore(Integer.parseInt(totalScoreObj.toString()));
            }
            // courseId类型健壮处理
            Object courseIdObj = payload.get("courseId");
            if (courseIdObj instanceof Integer) {
                paper.setCourseId((Integer) courseIdObj);
            } else if (courseIdObj instanceof Number) {
                paper.setCourseId(((Number) courseIdObj).intValue());
            } else if (courseIdObj != null) {
                paper.setCourseId(Integer.parseInt(courseIdObj.toString()));
            }
            paperService.createPaper(paper);
            logger.info("[PaperController] 新建试卷ID: {}", paper.getPaperId());
            List<Map<String, Object>> questions = (List<Map<String, Object>>) payload.get("questions");
            if (questions != null && !questions.isEmpty()) {
                List<PaperQuestion> pqList = new java.util.ArrayList<>();
                for (Map<String, Object> q : questions) {
                    PaperQuestion pq = new PaperQuestion();
                    pq.setPaperId(paper.getPaperId());
                    pq.setQuestionId(q.get("question_id") instanceof Integer ? (Integer) q.get("question_id") : Integer.parseInt(q.get("question_id").toString()));
                    pq.setScore(q.get("score") == null ? 5 : (q.get("score") instanceof Integer ? (Integer) q.get("score") : Integer.parseInt(q.get("score").toString())));
                    pq.setSortOrder(q.get("sort_order") == null ? 0 : (q.get("sort_order") instanceof Integer ? (Integer) q.get("sort_order") : Integer.parseInt(q.get("sort_order").toString())));
                    pq.setQuestionType(q.get("question_type") == null ? "single" : q.get("question_type").toString());
                    pqList.add(pq);
                }
                logger.info("[PaperController] 批量插入paper_question: {}", pqList);
                paperQuestionService.batchCreatePaperQuestions(pqList);
            }
            response.put("success", true);
            response.put("paper_id", paper.getPaperId());
            response.put("message", "创建试卷成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("[PaperController] 创建试卷失败", e);
            response.put("success", false);
            response.put("message", "创建试卷失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{paperId}")
    public ResponseEntity<Map<String, Object>> getPaperDetail(@PathVariable Integer paperId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Paper paper = paperService.getPaperById(paperId);
            if (paper != null) {
                // 批量查所有题目
                List<Map<String, Object>> questions = paperQuestionMapper.findQuestionDetailsByPaperId(paperId);
                for (Map<String, Object> q : questions) {
                    List<Map<String, String>> options = new ArrayList<>();
                    if (q.get("option_a") != null) options.add(Map.of("key", "A", "content", q.get("option_a").toString()));
                    if (q.get("option_b") != null) options.add(Map.of("key", "B", "content", q.get("option_b").toString()));
                    if (q.get("option_c") != null) options.add(Map.of("key", "C", "content", q.get("option_c").toString()));
                    if (q.get("option_d") != null) options.add(Map.of("key", "D", "content", q.get("option_d").toString()));
                    q.put("options", options);
                }
                response.put("success", true);
                response.put("data", paper);
                response.put("questions", questions);
                response.put("message", "获取试卷详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "试卷不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取试卷详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}