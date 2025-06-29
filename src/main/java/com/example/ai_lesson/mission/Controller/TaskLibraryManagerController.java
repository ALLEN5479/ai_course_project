package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.MissionTemplate;
import com.example.ai_lesson.mission.Service.MissionTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task-library")
@CrossOrigin(origins = "*")
public class TaskLibraryManagerController {
    
    @Autowired
    private MissionTemplateService missionTemplateService;
    
    @GetMapping("/missions")
    public ResponseEntity<Map<String, Object>> getAllMissions() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== 开始获取任务库列表 ===");
            List<MissionTemplate> missions = missionTemplateService.getAllActiveMissions();
            System.out.println("查询到的任务数量: " + (missions != null ? missions.size() : 0));
            
            if (missions != null) {
                for (int i = 0; i < missions.size(); i++) {
                    MissionTemplate mission = missions.get(i);
                    System.out.println("任务" + (i + 1) + ": ID=" + mission.getMissionId() + 
                                     ", 名称=" + mission.getMissionName() + 
                                     ", 类型=" + mission.getMissionType());
                }
            }
            
            response.put("success", true);
            response.put("data", missions);
            response.put("message", "获取任务库列表成功");
            System.out.println("=== 任务库列表获取完成 ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("获取任务库列表失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "获取任务库列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/missions/{missionId}")
    public ResponseEntity<Map<String, Object>> getMissionById(@PathVariable Integer missionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("获取任务详情，ID: " + missionId);
            MissionTemplate mission = missionTemplateService.getMissionById(missionId);
            if (mission != null) {
                response.put("success", true);
                response.put("data", mission);
                response.put("message", "获取任务详情成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务不存在");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("获取任务详情失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "获取任务详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/missions/type/{missionType}")
    public ResponseEntity<Map<String, Object>> getMissionsByType(@PathVariable String missionType) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("根据类型获取任务，类型: " + missionType);
            List<MissionTemplate> missions = missionTemplateService.getMissionsByType(missionType);
            response.put("success", true);
            response.put("data", missions);
            response.put("message", "获取任务列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("根据类型获取任务失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "获取任务列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/missions/teacher/{teacherId}")
    public ResponseEntity<Map<String, Object>> getMissionsByTeacherId(@PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("根据教师ID获取任务，教师ID: " + teacherId);
            List<MissionTemplate> missions = missionTemplateService.getMissionsByTeacherId(teacherId);
            response.put("success", true);
            response.put("data", missions);
            response.put("message", "获取教师任务列表成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("根据教师ID获取任务失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "获取教师任务列表失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/missions")
    public ResponseEntity<Map<String, Object>> createMission(@RequestBody MissionTemplate missionTemplate) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== 开始创建任务 ===");
            System.out.println("任务名称: " + missionTemplate.getMissionName());
            System.out.println("任务类型: " + missionTemplate.getMissionType());
            System.out.println("教师ID: " + missionTemplate.getTeacherId());
            
            MissionTemplate createdMission = missionTemplateService.createMission(missionTemplate);
            System.out.println("任务创建成功，ID: " + createdMission.getMissionId());
            
            response.put("success", true);
            response.put("data", createdMission);
            response.put("message", "创建任务成功");
            System.out.println("=== 任务创建完成 ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("创建任务失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "创建任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/missions/{missionId}")
    public ResponseEntity<Map<String, Object>> updateMission(@PathVariable Integer missionId, 
                                                            @RequestBody MissionTemplate missionTemplate) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("更新任务，ID: " + missionId);
            missionTemplate.setMissionId(missionId);
            boolean success = missionTemplateService.updateMission(missionTemplate);
            if (success) {
                response.put("success", true);
                response.put("message", "更新任务成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务不存在或更新失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("更新任务失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "更新任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/missions/{missionId}")
    public ResponseEntity<Map<String, Object>> deleteMission(@PathVariable Integer missionId) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("删除任务，ID: " + missionId);
            boolean success = missionTemplateService.deleteMission(missionId);
            if (success) {
                response.put("success", true);
                response.put("message", "删除任务成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务不存在或删除失败");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("删除任务失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "删除任务失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 