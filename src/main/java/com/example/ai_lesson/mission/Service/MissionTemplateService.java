package com.example.ai_lesson.mission.Service;

import com.example.ai_lesson.mission.Domain.MissionTemplate;
import com.example.ai_lesson.mission.Mapper.MissionTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MissionTemplateService {
    
    @Autowired
    private MissionTemplateMapper missionTemplateMapper;
    
    public List<MissionTemplate> getAllActiveMissions() {
        System.out.println("Service: 开始查询所有活跃任务");
        try {
            List<MissionTemplate> missions = missionTemplateMapper.findAllActive();
            System.out.println("Service: 查询到 " + (missions != null ? missions.size() : 0) + " 个任务");
            return missions;
        } catch (Exception e) {
            System.err.println("Service: 查询所有活跃任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public MissionTemplate getMissionById(Integer missionId) {
        System.out.println("Service: 根据ID查询任务，ID: " + missionId);
        try {
            MissionTemplate mission = missionTemplateMapper.findById(missionId);
            System.out.println("Service: 查询结果: " + (mission != null ? "找到" : "未找到"));
            return mission;
        } catch (Exception e) {
            System.err.println("Service: 根据ID查询任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public List<MissionTemplate> getMissionsByType(String missionType) {
        System.out.println("Service: 根据类型查询任务，类型: " + missionType);
        try {
            List<MissionTemplate> missions = missionTemplateMapper.findByType(missionType);
            System.out.println("Service: 查询到 " + (missions != null ? missions.size() : 0) + " 个任务");
            return missions;
        } catch (Exception e) {
            System.err.println("Service: 根据类型查询任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public List<MissionTemplate> getMissionsByTeacherId(Integer teacherId) {
        System.out.println("Service: 根据教师ID查询任务，教师ID: " + teacherId);
        try {
            List<MissionTemplate> missions = missionTemplateMapper.findByTeacherId(teacherId);
            System.out.println("Service: 查询到 " + (missions != null ? missions.size() : 0) + " 个任务");
            return missions;
        } catch (Exception e) {
            System.err.println("Service: 根据教师ID查询任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public MissionTemplate createMission(MissionTemplate missionTemplate) {
        System.out.println("Service: 开始创建任务");
        try {
            missionTemplate.setCreateTime(LocalDateTime.now());
            missionTemplate.setStatus(1);
            System.out.println("Service: 设置创建时间和状态");
            
            int result = missionTemplateMapper.insert(missionTemplate);
            System.out.println("Service: 插入结果: " + result);
            
            if (result > 0) {
                System.out.println("Service: 任务创建成功，ID: " + missionTemplate.getMissionId());
                return missionTemplate;
            } else {
                System.err.println("Service: 任务创建失败，插入返回0");
                throw new RuntimeException("任务创建失败");
            }
        } catch (Exception e) {
            System.err.println("Service: 创建任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public boolean updateMission(MissionTemplate missionTemplate) {
        System.out.println("Service: 开始更新任务，ID: " + missionTemplate.getMissionId());
        try {
            int result = missionTemplateMapper.update(missionTemplate);
            System.out.println("Service: 更新结果: " + result);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Service: 更新任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public boolean deleteMission(Integer missionId) {
        System.out.println("Service: 开始删除任务，ID: " + missionId);
        try {
            int result = missionTemplateMapper.updateStatus(missionId, 0);
            System.out.println("Service: 删除结果: " + result);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Service: 删除任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public boolean hardDeleteMission(Integer missionId) {
        System.out.println("Service: 开始硬删除任务，ID: " + missionId);
        try {
            int result = missionTemplateMapper.deleteById(missionId);
            System.out.println("Service: 硬删除结果: " + result);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Service: 硬删除任务失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 