package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.Paper;
import com.example.ai_lesson.mission.Domain.Question;
import com.example.ai_lesson.mission.Service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/papers")
public class PaperController {
    private final PaperService paperService;

    @Autowired
    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    // 新增试卷列表接口
    @GetMapping("/list")
    public ResponseEntity<List<Paper>> listPapers() {
        //System.out.println("获取试卷列表");
        List<Paper> papers = paperService.getPaperList();
        return ResponseEntity.ok(papers);
    }

    // 新增根据ID查询试卷接口
    @GetMapping("/{paperId}")
    public ResponseEntity<Paper> getPaper(@PathVariable int paperId) {
        Paper paper = paperService.getPaperById(paperId);
        return ResponseEntity.ok(paper);
    }

    // 新增创建试卷接口
    @PostMapping
    public ResponseEntity<Integer> createPaper(@RequestBody Paper paper) {
        int result = paperService.createPaper(paper);
        return ResponseEntity.ok(result);
    }

    // 新增更新试卷接口
    @PutMapping
    public ResponseEntity<Integer> updatePaper(@RequestBody Paper paper) {
        int result = paperService.updatePaper(paper);
        return ResponseEntity.ok(result);
    }

    // 新增删除试卷接口
    @DeleteMapping("/{paperId}")
    public ResponseEntity<Integer> deletePaper(@PathVariable int paperId) {
        int result = paperService.deletePaper(paperId);
        return ResponseEntity.ok(result);
    }


    // 组卷接口：根据paperId生成试卷
    @GetMapping("/{paperId}/questions")
    public ResponseEntity<List<Question>> generatePaper(@PathVariable int paperId) {
        List<Question> paper = paperService.generatePaper(paperId);
        return ResponseEntity.ok(paper);
    }

    // 题目CRUD接口
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable int questionId) {
        Question question = paperService.getQuestionById(questionId);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/questions")
    public ResponseEntity<Integer> createQuestion(@RequestBody Question question) {
        int result = paperService.createQuestion(question);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/questions")
    public ResponseEntity<Integer> updateQuestion(@RequestBody Question question) {
        int result = paperService.updateQuestion(question);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Integer> deleteQuestion(@PathVariable int questionId) {
        int result = paperService.deleteQuestion(questionId);
        return ResponseEntity.ok(result);
    }

    // 管理试卷题目接口
    @PostMapping("/{paperId}/questions/{questionId}")
    public ResponseEntity<Integer> addQuestionToPaper(
            @PathVariable int paperId,
            @PathVariable int questionId) {
        int result = paperService.addQuestionToPaper(paperId, questionId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{paperId}/questions/{questionId}")
    public ResponseEntity<Integer> removeQuestionFromPaper(
            @PathVariable int paperId,
            @PathVariable int questionId) {
        int result = paperService.removeQuestionFromPaper(paperId, questionId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{paperId}/with-questions")
    public ResponseEntity<Map<String, Object>> getPaperWithQuestions(@PathVariable int paperId) {
        Paper paper = paperService.getPaperById(paperId);
        List<Question> questions = paperService.generatePaper(paperId);
        //System.out.println("获取试卷和题目列表: " + paper.toString());
        //System.out.println("获取题目列表: " + questions);
        Map<String, Object> response = new HashMap<>();
        response.put("paper", paper);
        response.put("questions", questions);
        return ResponseEntity.ok(response);
    }
}