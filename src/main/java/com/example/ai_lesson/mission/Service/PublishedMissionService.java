package com.example.ai_lesson.mission.Service;

import com.example.ai_lesson.mission.Domain.PublishedMission;
import com.example.ai_lesson.mission.Mapper.PublishedMissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublishedMissionService {
    
    @Autowired
    private PublishedMissionMapper publishedMissionMapper;
    
    public List<PublishedMission> getAllPublishedMissions() {
        System.out.println("=== 开始获取所有已发布任务 ===");
        List<PublishedMission> missions = publishedMissionMapper.findAll();
        System.out.println("查询到的原始数据:");
        for (PublishedMission mission : missions) {
            System.out.println("- ID: " + mission.getId());
            System.out.println("- MissionID: " + mission.getMissionId());
            System.out.println("- StartTime: " + mission.getStartTime());
            System.out.println("- EndTime: " + mission.getEndTime());
            System.out.println("- Status: " + mission.getStatus());
            System.out.println("- CreateTime: " + mission.getCreateTime());
            System.out.println("---");
        }
        System.out.println("=== 获取已发布任务完成 ===");
        return missions;
    }
    
    public PublishedMission getPublishedMissionById(Integer id) {
        return publishedMissionMapper.findById(id);
    }
    
    public List<PublishedMission> getPublishedMissionsByMissionId(Integer missionId) {
        return publishedMissionMapper.findByMissionId(missionId);
    }
    
    public List<PublishedMission> getPublishedMissionsByStatus(String status) {
        return publishedMissionMapper.findByStatus(status);
    }
    
    public PublishedMission publishMission(PublishedMission publishedMission) {
        publishedMission.setCreateTime(LocalDateTime.now());
        if (publishedMission.getStatus() == null) {
            publishedMission.setStatus("pending");
        }
        publishedMissionMapper.insert(publishedMission);
        return publishedMission;
    }
    
    public boolean updatePublishedMission(PublishedMission publishedMission) {
        return publishedMissionMapper.update(publishedMission) > 0;
    }
    
    public boolean updatePublishedMissionStatus(Integer id, String status) {
        return publishedMissionMapper.updateStatus(id, status) > 0;
    }
    
    public boolean deletePublishedMission(Integer id) {
        return publishedMissionMapper.deleteById(id) > 0;
    }
} 