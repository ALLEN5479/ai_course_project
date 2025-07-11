package com.example.ai_lesson.missiontest.Service;

import com.example.ai_lesson.mission.Service.MissionTemplateService;
import com.example.ai_lesson.mission.Domain.MissionTemplate;
import com.example.ai_lesson.mission.Mapper.MissionTemplateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionTemplateServiceTest {
    MissionTemplateService service;

    static class MissionTemplateMapperStub implements com.example.ai_lesson.mission.Mapper.MissionTemplateMapper {
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.MissionTemplate> findAllActive() { return java.util.List.of(new com.example.ai_lesson.mission.Domain.MissionTemplate()); }
        @Override
        public com.example.ai_lesson.mission.Domain.MissionTemplate findById(Integer missionId) { return new com.example.ai_lesson.mission.Domain.MissionTemplate(); }
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.MissionTemplate> findByType(String missionType) { return java.util.List.of(new com.example.ai_lesson.mission.Domain.MissionTemplate()); }
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.MissionTemplate> findByTeacherId(Integer teacherId) { return java.util.List.of(new com.example.ai_lesson.mission.Domain.MissionTemplate()); }
        @Override
        public int insert(com.example.ai_lesson.mission.Domain.MissionTemplate missionTemplate) { missionTemplate.setMissionId(1); return 1; }
        @Override
        public int update(com.example.ai_lesson.mission.Domain.MissionTemplate missionTemplate) { return 1; }
        @Override
        public int updateStatus(Integer missionId, Integer status) { return 1; }
        @Override
        public int deleteById(Integer missionId) { return 1; }
    }

    @BeforeEach
    void setUp() {
        service = new MissionTemplateService();
        setField(service, "missionTemplateMapper", new MissionTemplateMapperStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }

    @Test void testGetAllActiveMissions() { assertFalse(service.getAllActiveMissions().isEmpty()); }
    @Test void testGetMissionById() { assertNotNull(service.getMissionById(1)); }
    @Test void testGetMissionsByType() { assertFalse(service.getMissionsByType("type").isEmpty()); }
    @Test void testGetMissionsByTeacherId() { assertFalse(service.getMissionsByTeacherId(1).isEmpty()); }
    @Test void testCreateMission() { assertNotNull(service.createMission(new MissionTemplate())); }
    @Test void testUpdateMission() { assertTrue(service.updateMission(new MissionTemplate())); }
    @Test void testDeleteMission() { assertTrue(service.deleteMission(1)); }
    @Test void testHardDeleteMission() { assertTrue(service.hardDeleteMission(1)); }
}