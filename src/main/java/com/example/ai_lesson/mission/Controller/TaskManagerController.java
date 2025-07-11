package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.PublishedMission;
import com.example.ai_lesson.mission.Domain.ReportResource;
import com.example.ai_lesson.mission.Domain.ShowPublilshed;
import com.example.ai_lesson.mission.Service.PublishedMissionService;
import com.example.ai_lesson.mission.utils.FileUploadUtil;
import com.example.ai_lesson.mission.Mapper.StudentReportMissionMapper;
import com.example.ai_lesson.mission.Domain.StudentReportMission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task-manager")
@CrossOrigin(origins = "*")
public class TaskManagerController {
    
    @Autowired
    private PublishedMissionService publishedMissionService;
    
    @Autowired
    private StudentReportMissionMapper studentReportMissionMapper;
    
    @Value("${file.upload.mission-dir}")
    private String missionUploadDir;
    
    @Value("${file.upload.mission-access-path}")
    private String missionAccessPath;
    
    @GetMapping("/published-missions")
    public ResponseEntity<Map<String, Object>> getAllPublishedMissions() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PublishedMission> publishedMissions = publishedMissionService.getAllPublishedMissions();
            response.put("success", true);
            response.put("data", publishedMissions);
            response.put("message", "获取已发布任务列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取已发布任务列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/published-missions/{id}")
    public ResponseEntity<Map<String, Object>> getPublishedMissionById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            PublishedMission publishedMission = publishedMissionService.getPublishedMissionById(id);
            if (publishedMission != null) {
                response.put("success", true);
                response.put("data", publishedMission);
                response.put("message", "获取已发布任务详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "已发布任务不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取已发布任务详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/published-missions/mission/{missionId}")
    public ResponseEntity<Map<String, Object>> getPublishedMissionsByMissionId(@PathVariable Integer missionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PublishedMission> publishedMissions = publishedMissionService.getPublishedMissionsByMissionId(missionId);
            response.put("success", true);
            response.put("data", publishedMissions);
            response.put("message", "获取任务发布记录成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务发布记录失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/published-missions/status/{status}")
    public ResponseEntity<Map<String, Object>> getPublishedMissionsByStatus(@PathVariable String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PublishedMission> publishedMissions = publishedMissionService.getPublishedMissionsByStatus(status);
            response.put("success", true);
            response.put("data", publishedMissions);
            response.put("message", "获取任务列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/publish-mission")
    public ResponseEntity<Map<String, Object>> publishMission(@RequestBody PublishedMission publishedMission) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== 开始发布任务 ===");
            System.out.println("接收到的数据:");
            System.out.println("- missionId: " + publishedMission.getMissionId());
            System.out.println("- startTime: " + publishedMission.getStartTime());
            System.out.println("- endTime: " + publishedMission.getEndTime());
            System.out.println("- status: " + publishedMission.getStatus());
            
            PublishedMission createdPublishedMission = publishedMissionService.publishMission(publishedMission);
            System.out.println("任务发布成功，ID: " + createdPublishedMission.getId());
            
            response.put("success", true);
            response.put("data", createdPublishedMission);
            response.put("message", "发布任务成功");
            System.out.println("=== 任务发布完成 ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("发布任务失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "发布任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/published-missions/{id}")
    public ResponseEntity<Map<String, Object>> updatePublishedMission(@PathVariable Integer id, 
                                                                     @RequestBody PublishedMission publishedMission) {
        Map<String, Object> response = new HashMap<>();
        try {
            publishedMission.setId(id);
            boolean success = publishedMissionService.updatePublishedMission(publishedMission);
            if (success) {
                response.put("success", true);
                response.put("message", "更新已发布任务成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "已发布任务不存在或更新失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新已发布任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/published-missions/{id}/status")
    public ResponseEntity<Map<String, Object>> updatePublishedMissionStatus(@PathVariable Integer id, 
                                                                           @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = publishedMissionService.updatePublishedMissionStatus(id, status);
            if (success) {
                response.put("success", true);
                response.put("message", "更新任务状态成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "已发布任务不存在或更新失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新任务状态失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/published-missions/{id}")
    public ResponseEntity<Map<String, Object>> deletePublishedMission(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = publishedMissionService.deletePublishedMission(id);
            if (success) {
                response.put("success", true);
                response.put("message", "删除已发布任务成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "已发布任务不存在或删除失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除已发布任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/published-missions/show")
    public List<ShowPublilshed> showPublishedMissions() {
        List<ShowPublilshed> showPublishedMissions = publishedMissionService.showPublishedMissions();
        return showPublishedMissions;
    }

    @GetMapping("/published-missions/showcontent")
    public String showContent(@RequestParam Integer mission_id) {
        String content = publishedMissionService.showContent(mission_id);
        return content;
    }

    @GetMapping("/published-missions/showreportresource")
    public ReportResource getResource(@RequestParam int resource_id) {
        ReportResource resource = publishedMissionService.getResource(resource_id);
        return resource;
    }

    // 更新学生分数
    @RequestMapping("/published-missions/updatescore")
    public int updateScore(@RequestParam int mission_id, @RequestParam String student_id, @RequestParam int score) {
        return publishedMissionService.updateScore(mission_id, student_id, score);
    }

    /**
     * 学生报告上传接口
     */
    @PostMapping("/report/upload")
    public ResponseEntity<Map<String, Object>> uploadStudentReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "missionId", required = false) Integer missionId,
            @RequestParam(value = "introduction", required = false) String introduction) {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] allowTypes = {"pdf", "doc", "docx", "zip"};
            long maxSize = 50 * 1024 * 1024; // 50MB
            String uploadDir = System.getProperty("user.dir") + "/" + missionUploadDir;
            System.out.println("=== 文件上传路径信息 ===");
            System.out.println("当前工作目录: " + System.getProperty("user.dir"));
            System.out.println("配置的上传目录: " + missionUploadDir);
            System.out.println("完整上传路径: " + uploadDir);
            System.out.println("原始文件名: " + file.getOriginalFilename());
            System.out.println("文件大小: " + file.getSize() + " 字节");
            System.out.println("=========================");
            String saveName = FileUploadUtil.uploadFile(file, uploadDir, allowTypes, maxSize);
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                originalFileName = "未命名文件";
            }
            String accessPath = missionAccessPath + saveName;
            System.out.println("=== 文件保存结果 ===");
            System.out.println("保存的文件名: " + saveName);
            System.out.println("访问路径: " + accessPath);
            System.out.println("完整文件路径: " + uploadDir + "/" + saveName);
            System.out.println("=====================");
            Map<String, Object> data = new HashMap<>();
            data.put("fileName", originalFileName == null ? "" : originalFileName);
            data.put("filePath", accessPath == null ? "" : accessPath);
            data.put("saveName", saveName == null ? "" : saveName);
            data.put("missionId", missionId == null ? -1 : missionId);
            data.put("introduction", introduction == null ? "" : introduction);
            response.put("success", true);
            response.put("message", "文件上传成功");
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件保存失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/report/submit")
    public ResponseEntity<Map<String, Object>> submitStudentReport(@RequestBody Map<String, Object> data) {
        try {
            // 兼容前端传递的数字或字符串
            Object missionIdObj = data.get("mission_id");
            Integer missionId = null;
            if (missionIdObj instanceof Integer) {
                missionId = (Integer) missionIdObj;
            } else if (missionIdObj instanceof String) {
                missionId = Integer.valueOf((String) missionIdObj);
            }

            String studentId = (String) data.get("student_id");
            String reportName = (String) data.get("report_name");
            String reportDes = (String) data.get("report_des");
            String reportUrl = (String) data.get("report_url");

            System.out.println("=== 学生报告信息 ===");
            System.out.println("任务ID: " + missionId);
            System.out.println("学生ID: " + studentId);
            System.out.println("报告名称: " + reportName);
            System.out.println("报告描述: " + reportDes);
            System.out.println("报告URL: " + reportUrl);

            StudentReportMission report = new StudentReportMission();
            report.setMission_id(missionId);
            report.setStudent_id(studentId);
            report.setReport_name(reportName);
            report.setReport_des(reportDes);
            report.setReport_url(reportUrl);

            studentReportMissionMapper.insert(report);
            return ResponseEntity.ok(Map.of("success", true, "message", "保存成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "保存失败: " + e.getMessage()));
        }
    }
} 