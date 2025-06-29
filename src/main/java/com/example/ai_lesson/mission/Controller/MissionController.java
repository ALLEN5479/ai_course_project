package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.Mission;
import com.example.ai_lesson.mission.Service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/mission")
public class MissionController {

    @Autowired
    private MissionService missionService;

    /**
     * 分页查询任务列表
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String missionName,
            @RequestParam(required = false) String missionType,
            @RequestParam(required = false) String teachingClass) {
        try {
            // 参数验证
            if (pageNum <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "页码必须大于0");
            }
            if (pageSize <= 0 || pageSize > 100) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "页面大小必须在1-100之间");
            }

            Map<String, Object> result = missionService.selectMissionList(pageNum, pageSize, missionName, missionType, teachingClass);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取任务列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取任务详情
     */
//    @GetMapping("/{missionId}")
//    public ResponseEntity<Mission> getInfo(@PathVariable Integer missionId) {
//        Mission mission = missionService.selectMissionById(missionId);
//        return ResponseEntity.ok(mission);
//    }
    @GetMapping("/{missionId}")
    public ResponseEntity<Map<String, Object>> getInfo(@PathVariable Integer missionId) {
        try {
            if (missionId == null || missionId <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务ID无效");
            }

            Mission mission = missionService.selectMissionById(missionId);
            Map<String, Object> response = new HashMap<>();

            if (mission != null) {
                response.put("success", true);
                response.put("data", mission);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取任务详情失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 新增任务
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> add(@RequestBody Mission mission) {
        try {
            // 参数验证
            if (mission == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务信息不能为空");
            }
            if (mission.getMissionName() == null || mission.getMissionName().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务名称不能为空");
            }

            int result = missionService.insertMission(mission);
            Map<String, Object> response = new HashMap<>();
            response.put("success", result > 0);
            response.put("missionId", mission.getMissionId());
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "新增任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 修改任务
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> edit(@RequestBody Mission mission) {
        try {
            // 参数验证
            if (mission == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务信息不能为空");
            }
            if (mission.getMissionId() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务ID无效");
            }

            int result = missionService.updateMission(mission);
            Map<String, Object> response = new HashMap<>();
            response.put("success", result > 0);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "修改任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{missionId}")
    public ResponseEntity<Map<String, Object>> remove(@PathVariable Integer missionId) {
        try {
            if (missionId == null || missionId <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "任务ID无效");
            }

            int result = missionService.deleteMissionById(missionId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", result > 0);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取任务类型选项
     */
    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getMissionTypes() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", List.of("测验任务", "报告任务"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取任务类型失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}