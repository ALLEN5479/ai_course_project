package com.example.ai_lesson.missiontest.Service;

import com.example.ai_lesson.mission.Service.MissionService;
import com.example.ai_lesson.mission.Domain.Mission;
import com.example.ai_lesson.mission.Mapper.MissionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MissionServiceTest {
    MissionService service;

    static class MissionMapperStub implements com.example.ai_lesson.mission.Mapper.MissionMapper {
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.Mission> selectMissionList(int offset, int pageSize, String missionName, String missionType, String teachingClass) {
            return java.util.List.of(new com.example.ai_lesson.mission.Domain.Mission());
        }
        @Override
        public int countMissionList(String missionName, String missionType, String teachingClass) { return 1; }
        @Override
        public com.example.ai_lesson.mission.Domain.Mission selectMissionById(Integer missionId) { return new com.example.ai_lesson.mission.Domain.Mission(); }
        @Override
        public int insertMission(com.example.ai_lesson.mission.Domain.Mission mission) { mission.setMissionId(1); return 1; }
        @Override
        public int updateMission(com.example.ai_lesson.mission.Domain.Mission mission) { return 1; }
        @Override
        public int deleteMissionById(Integer missionId) { return 1; }
        // 其他接口方法可空实现或抛异常
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.Mission> selectMissionsByState(Integer teacherId, Integer state, String missionName) { throw new UnsupportedOperationException(); }
    }

    @BeforeEach
    void setUp() {
        service = new MissionService();
        setField(service, "missionMapper", new MissionMapperStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }

    @Test void testSelectMissionList() { Map<String, Object> r = service.selectMissionList(1, 10, null, null, null); assertNotNull(r.get("list")); }
    @Test void testSelectMissionById() { assertNotNull(service.selectMissionById(1)); }
    @Test void testInsertMission() { assertEquals(1, service.insertMission(new Mission())); }
    @Test void testUpdateMission() { assertEquals(1, service.updateMission(new Mission())); }
    @Test void testDeleteMissionById() { assertEquals(1, service.deleteMissionById(1)); }
}