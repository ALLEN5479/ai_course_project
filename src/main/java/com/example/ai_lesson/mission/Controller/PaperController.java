package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.Paper;
import com.example.ai_lesson.mission.Service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/papers")
@CrossOrigin(origins = "*")
public class PaperController {
    
    @Autowired
    private PaperService paperService;
    
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
    
    @GetMapping("/{paperId}")
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
    }
    
    @PostMapping
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
    }
    
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
}