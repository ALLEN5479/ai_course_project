package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.ClassInfo;
import com.example.ai_lesson.mission.Mapper.ClassInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/mission")
public class MissionViewController {

    @Autowired
    private ClassInfoMapper classInfoMapper;

    /**
     * 获取所有班级及其任务信息
     * @return 包含班级和任务列表的响应
     */
    @GetMapping("/classes-with-missions")
    public Map<String, Object> getClassesWithMissions() {
        Map<String, Object> response = new HashMap<>();
        List<ClassInfo> classes = classInfoMapper.findAllClassesWithMissionsAndStats();
        //System.out.println("classes: " + classes);
        response.put("data", classes);
        return response;
    }

    /**
     * 获取任务统计数据
     * @param missionId 任务ID
     * @return 任务统计信息
     */
    @GetMapping("/statistics/{missionId}")
    public Map<String, Object> getMissionStatistics(@PathVariable int missionId) {
        Map<String, Object> response = new HashMap<>();
        int stats = classInfoMapper.findMissionStatistics(missionId);
        //System.out.println("completed_number:"+stats);
        response.put("data", stats);
        return response;
    }

    /**
     * 根据班级ID获取班级详情
     * @param classId 班级ID
     * @return 班级详情
     */
    @GetMapping("/class/{classId}")
    public Map<String, Object> getClassDetail(@PathVariable int classId) {
        Map<String, Object> response = new HashMap<>();
        ClassInfo classInfo = classInfoMapper.findClassById(classId);
        response.put("data", classInfo);
        return response;
    }

    /**
     * 分页查询班级信息
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页班级列表
     */
    @GetMapping("/classes")
    public Map<String, Object> getClassesByPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> response = new HashMap<>();
        int offset = (pageNum - 1) * pageSize;
        List<ClassInfo> classes = classInfoMapper.findClassesByPage(offset, pageSize);
        int total = classInfoMapper.countClasses();

        response.put("data", classes);
        response.put("total", total);
        response.put("pageNum", pageNum);
        response.put("pageSize", pageSize);
        return response;
    }
}