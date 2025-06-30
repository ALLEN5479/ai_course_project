package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.MissionSubmission;
import com.example.ai_lesson.mission.Service.MissionSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task-grading")
@CrossOrigin(origins = "*")
public class TaskGradingController {
    
    @Autowired
    private MissionSubmissionService missionSubmissionService;
    
    @GetMapping("/submissions/{publishedMissionId}")
    public ResponseEntity<Map<String, Object>> getSubmissionsByPublishedMissionId(@PathVariable Integer publishedMissionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MissionSubmission> submissions = missionSubmissionService.getSubmissionsByPublishedMissionId(publishedMissionId);
            response.put("success", true);
            response.put("data", submissions);
            response.put("message", "获取任务提交列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务提交列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/submissions/{publishedMissionId}/submit-status/{submitStatus}")
    public ResponseEntity<Map<String, Object>> getSubmissionsBySubmitStatus(@PathVariable Integer publishedMissionId, 
                                                                           @PathVariable String submitStatus) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MissionSubmission> submissions = missionSubmissionService.getSubmissionsByMissionAndSubmitStatus(publishedMissionId, submitStatus);
            response.put("success", true);
            response.put("data", submissions);
            response.put("message", "获取任务提交列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务提交列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/submissions/{publishedMissionId}/grade-status/{gradeStatus}")
    public ResponseEntity<Map<String, Object>> getSubmissionsByGradeStatus(@PathVariable Integer publishedMissionId, 
                                                                          @PathVariable String gradeStatus) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MissionSubmission> submissions = missionSubmissionService.getSubmissionsByMissionAndGradeStatus(publishedMissionId, gradeStatus);
            response.put("success", true);
            response.put("data", submissions);
            response.put("message", "获取任务提交列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务提交列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<Map<String, Object>> getSubmissionById(@PathVariable Integer submissionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            MissionSubmission submission = missionSubmissionService.getSubmissionById(submissionId);
            if (submission != null) {
                response.put("success", true);
                response.put("data", submission);
                response.put("message", "获取提交详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "提交不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取提交详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/grade/{submissionId}")
    public ResponseEntity<Map<String, Object>> gradeSubmission(@PathVariable Integer submissionId, 
                                                              @RequestBody MissionSubmission missionSubmission) {
        Map<String, Object> response = new HashMap<>();
        try {
            missionSubmission.setSubmissionId(submissionId);
            boolean success = missionSubmissionService.gradeSubmission(missionSubmission);
            if (success) {
                response.put("success", true);
                response.put("message", "批改任务成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "提交不存在或批改失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批改任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/batch-grade/{publishedMissionId}")
    public ResponseEntity<Map<String, Object>> batchGrade(@PathVariable Integer publishedMissionId,
                                                         @RequestParam List<Integer> submissionIds,
                                                         @RequestParam Integer score,
                                                         @RequestParam(required = false) String comment,
                                                         @RequestParam Integer graderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = missionSubmissionService.batchGrade(publishedMissionId, submissionIds, score, comment, graderId);
            if (success) {
                response.put("success", true);
                response.put("message", "批量批改任务成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "批量批改任务失败");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量批改任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 