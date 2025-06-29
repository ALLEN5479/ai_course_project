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
@RequestMapping("/api/mission-submissions")
@CrossOrigin(origins = "*")
public class MissionSubmissionController {
    
    @Autowired
    private MissionSubmissionService missionSubmissionService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getMissionSubmissions(@RequestParam(required = false) Integer missionId,
                                                                     @RequestParam(required = false) String studentId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MissionSubmission> submissions;
            if (missionId != null) {
                submissions = missionSubmissionService.getSubmissionsByPublishedMissionId(missionId);
            } else {
                // 如果没有指定missionId，返回空列表或抛出异常
                response.put("success", false);
                response.put("message", "必须指定missionId参数");
                return ResponseEntity.badRequest().body(response);
            }
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
    
    @GetMapping("/{submissionId}")
    public ResponseEntity<Map<String, Object>> getMissionSubmissionById(@PathVariable Integer submissionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            MissionSubmission submission = missionSubmissionService.getSubmissionById(submissionId);
            if (submission != null) {
                response.put("success", true);
                response.put("data", submission);
                response.put("message", "获取任务提交详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务提交不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务提交详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitMission(@RequestBody MissionSubmission missionSubmission) {
        Map<String, Object> response = new HashMap<>();
        try {
            MissionSubmission createdSubmission = missionSubmissionService.createSubmission(missionSubmission);
            response.put("success", true);
            response.put("data", createdSubmission);
            response.put("message", "提交任务成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "提交任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/{submissionId}")
    public ResponseEntity<Map<String, Object>> updateMissionSubmission(@PathVariable Integer submissionId,
                                                                      @RequestBody MissionSubmission missionSubmission) {
        Map<String, Object> response = new HashMap<>();
        try {
            missionSubmission.setSubmissionId(submissionId);
            boolean success = missionSubmissionService.updateSubmission(missionSubmission);
            if (success) {
                response.put("success", true);
                response.put("message", "更新任务提交成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务提交不存在或更新失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新任务提交失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 