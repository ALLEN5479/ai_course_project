package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.MissionSubmission;
import com.example.ai_lesson.mission.Service.MissionSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task-grading")
@CrossOrigin(origins = "*")
public class TaskGradingController {
    
    @Autowired
    private MissionSubmissionService missionSubmissionService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @GetMapping("/submissions/{publishMissionId}")
    public ResponseEntity<Map<String, Object>> getSubmissionsByPublishedMissionId(@PathVariable Integer publishMissionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MissionSubmission> submissions = missionSubmissionService.getSubmissionsByPublishedMissionId(publishMissionId);
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
    
    @GetMapping("/students")
    public ResponseEntity<?> getStudentsByPublishedMissionId(@RequestParam Integer publishedMissionId) {
        // 1. 查 published_mission 表获取 mission_id
        String missionIdSql = "SELECT mission_id FROM published_mission WHERE id = ?";
        Integer missionId = jdbcTemplate.queryForObject(missionIdSql, Integer.class, publishedMissionId);
        if (missionId == null) {
            return ResponseEntity.badRequest().body("未找到对应的mission_id");
        }
        // 2. 查 student_report_mission 表获取 student_id
        String studentIdSql = "SELECT DISTINCT student_id FROM student_report_mission WHERE mission_id = ?";
        List<String> studentIds = jdbcTemplate.queryForList(studentIdSql, String.class, missionId);
        if (studentIds.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        // 3. 查 user_msg 表获取 studentName
        String inSql = String.join(",", java.util.Collections.nCopies(studentIds.size(), "?"));
        String userSql = "SELECT user_id AS studentId, name AS studentName FROM user_msg WHERE user_id IN (" + inSql + ")";
        List<Map<String, Object>> students = jdbcTemplate.queryForList(userSql, studentIds.toArray());
        return ResponseEntity.ok(students);
    }

    /*@GetMapping("/submission-detail")
    public ResponseEntity<?> getSubmissionDetailByMissionAndStudent(@RequestParam Integer missionId, @RequestParam String studentId) {
        String sql = "SELECT ms.*, u.name AS studentName FROM mission_submission ms " +
                "LEFT JOIN user_msg u ON ms.student_id = u.user_id " +
                "WHERE ms.published_mission_id = ? AND ms.student_id = ? LIMIT 1";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, missionId, studentId);
        if (list.isEmpty()) {
            return ResponseEntity.badRequest().body("未找到该学生的提交记录");
        }
        return ResponseEntity.ok(Map.of("data", list.get(0)));
    }*/

    @GetMapping("/submission/{submissionId:\\d+}")
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
    
    @PutMapping("/grade")
    public ResponseEntity<Map<String, Object>> gradeSubmissionByMissionAndStudent(@RequestBody Map<String, Object> params) {
        Integer missionId = (Integer) params.get("missionId");
        Integer studentId = (Integer) params.get("studentId");
        Integer score = (Integer) params.get("score");
        String comment = (String) params.get("comment");
        boolean success = missionSubmissionService.gradeSubmissionByMissionAndStudent(missionId, studentId, score, comment);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("success", true);
            response.put("message", "批改成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "批改失败");
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

    @GetMapping("/student-report")
    public ResponseEntity<?> getStudentReport(@RequestParam Integer missionId, @RequestParam String studentId) {
        String sql = "SELECT report_name, report_url FROM student_report_mission WHERE mission_id = ? AND student_id = ? LIMIT 1";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(sql, missionId, studentId);
        } catch (Exception e) {
            // 查询异常
        }
        if (list == null || list.isEmpty()) {
            return ResponseEntity.badRequest().body("未找到该学生的报告");
        }
        return ResponseEntity.ok(list.get(0));
    }

    @GetMapping("/published-mission/mission-id")
    public ResponseEntity<?> getMissionIdByPublishedMissionId(@RequestParam Integer publishedMissionId) {
        String sql = "SELECT mission_id FROM published_mission WHERE id = ?";
        Integer missionId = null;
        try {
            missionId = jdbcTemplate.queryForObject(sql, Integer.class, publishedMissionId);
        } catch (Exception e) {
            // 查询异常
        }
        if (missionId == null) {
            return ResponseEntity.badRequest().body("未找到对应的mission_id");
        }
        return ResponseEntity.ok(Map.of("missionId", missionId));
    }

    @PostMapping("/report-score")
    public ResponseEntity<?> submitReportScore(@RequestBody Map<String, Object> params) {
        try {
            Integer publishedMissionId = params.get("publishedMissionId") == null ? null : Integer.valueOf(params.get("publishedMissionId").toString());
            String studentId = String.valueOf(params.get("studentId"));
            Integer score = params.get("score") == null ? null : Integer.valueOf(params.get("score").toString());
            if (publishedMissionId == null || studentId == null || score == null) {
                return ResponseEntity.badRequest().body("参数不能为空");
            }
            // 查 mission_id
            String sql = "SELECT mission_id FROM published_mission WHERE id = ?";
            Integer missionId = null;
            try {
                missionId = jdbcTemplate.queryForObject(sql, Integer.class, publishedMissionId);
            } catch (Exception e) {
                // 未查到 missionId
            }
            if (missionId == null) {
                return ResponseEntity.badRequest().body("未找到对应的mission_id");
            }
            boolean success = missionSubmissionService.saveReportScore(missionId, studentId, score);
            if (success) {
                return ResponseEntity.ok("批改分数保存成功");
            } else {
                return ResponseEntity.badRequest().body("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("参数格式错误: " + e.getMessage());
        }
    }

    @GetMapping("/score")
    public ResponseEntity<?> getStudentScore(@RequestParam Integer publishedMissionId, @RequestParam String studentId) {
        // 查 mission_id
        String sql = "SELECT mission_id FROM published_mission WHERE id = ?";
        Integer missionId = jdbcTemplate.queryForObject(sql, Integer.class, publishedMissionId);
        if (missionId == null) {
            return ResponseEntity.badRequest().body("未找到对应的mission_id");
        }
        // 查分数
        String scoreSql = "SELECT score FROM mission_student_score WHERE mission_id = ? AND student_id = ?";
        Integer score = null;
        try {
            score = jdbcTemplate.queryForObject(scoreSql, Integer.class, missionId, studentId);
        } catch (Exception e) {
            // 没有分数
        }
        if (score == null) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.ok().body(score);
    }
} 