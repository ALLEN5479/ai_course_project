package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.PublishedMission;
import com.example.ai_lesson.mission.Service.PublishedMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task-manager")
@CrossOrigin(origins = "*")
public class TaskManagerController {
    
    @Autowired
    private PublishedMissionService publishedMissionService;
    
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
} 