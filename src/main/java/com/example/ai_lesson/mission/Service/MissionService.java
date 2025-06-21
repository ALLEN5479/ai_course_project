package com.example.ai_lesson.mission.Service;

import com.example.ai_lesson.mission.Domain.Mission;
import com.example.ai_lesson.mission.Mapper.MissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MissionService {

    @Autowired
    private MissionMapper missionMapper;

    /**
     * 分页查询任务列表
     */
    public Map<String, Object> selectMissionList(Integer pageNum, Integer pageSize,
                                                 String missionName, String missionType,
                                                 String teachingClass) {
        int offset = (pageNum - 1) * pageSize;
        List<Mission> list = missionMapper.selectMissionList(offset, pageSize,
                missionName, missionType, teachingClass);
        int total = missionMapper.countMissionList(missionName, missionType, teachingClass);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }
    /**
     * 查询任务详情
     */
    public Mission selectMissionById(Integer missionId) {
        return missionMapper.selectMissionById(missionId);
    }

    /**
     * 新增任务
     */
    public int insertMission(Mission mission) {
        return missionMapper.insertMission(mission);
    }

    /**
     * 修改任务
     */
    public int updateMission(Mission mission) {
        return missionMapper.updateMission(mission);
    }

    /**
     * 删除任务
     */
    public int deleteMissionById(Integer missionId) {
        return missionMapper.deleteMissionById(missionId);
    }

    /**
     * 批量删除任务
     */
   /* public int deleteMissionByIds(List<Integer> missionIds) {
        return missionMapper.deleteMissionByIds(missionIds);
    }*/
}