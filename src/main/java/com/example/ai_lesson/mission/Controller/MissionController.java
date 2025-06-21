package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.Mission;
import com.example.ai_lesson.mission.Service.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Map<String, Object> result = missionService.selectMissionList(pageNum, pageSize, missionName, missionType, teachingClass);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{missionId}")
    public ResponseEntity<Mission> getInfo(@PathVariable Integer missionId) {
        Mission mission = missionService.selectMissionById(missionId);
        return ResponseEntity.ok(mission);
    }

    /**
     * 新增任务
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> add(@RequestBody Mission mission) {
        int result = missionService.insertMission(mission);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result > 0);
        response.put("missionId", mission.getMissionId()); // 返回新增的任务ID
        return ResponseEntity.ok(response);
    }

    /**
     * 修改任务
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> edit(@RequestBody Mission mission) {
        int result = missionService.updateMission(mission);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result > 0);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{missionId}")
    public ResponseEntity<Map<String, Object>> remove(@PathVariable Integer missionId) {
        int result = missionService.deleteMissionById(missionId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", result > 0);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取任务类型选项
     */
    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getMissionTypes() {
        //System.out.println("方法已运行111111");
        Map<String, Object> response = new HashMap<>();
        response.put("data", List.of("测验任务", "报告任务"));
        return ResponseEntity.ok(response);
    }
}