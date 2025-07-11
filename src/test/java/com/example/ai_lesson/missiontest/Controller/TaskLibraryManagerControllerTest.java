package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.TaskLibraryManagerController;
import com.example.ai_lesson.mission.Service.MissionTemplateService;
import com.example.ai_lesson.mission.Domain.MissionTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TaskLibraryManagerControllerTest {
    TaskLibraryManagerController controller;
    static class MissionTemplateServiceStub extends MissionTemplateService {
        @Override public List<MissionTemplate> getAllActiveMissions() { return List.of(new MissionTemplate()); }
        @Override public MissionTemplate getMissionById(Integer id) { return new MissionTemplate(); }
        @Override public List<MissionTemplate> getMissionsByType(String type) { return List.of(new MissionTemplate()); }
        @Override public List<MissionTemplate> getMissionsByTeacherId(Integer teacherId) { return List.of(new MissionTemplate()); }
        @Override public MissionTemplate createMission(MissionTemplate t) { return t; }
        @Override public boolean updateMission(MissionTemplate t) { return true; }
        @Override public boolean deleteMission(Integer id) { return true; }
    }
    @BeforeEach void setUp() {
        controller = new TaskLibraryManagerController();
        setField(controller, "missionTemplateService", new MissionTemplateServiceStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }
    @Test void testGetAllMissions() {
        var resp = controller.getAllMissions();
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetMissionById() {
        var resp = controller.getMissionById(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetMissionsByType() {
        var resp = controller.getMissionsByType("type");
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetMissionsByTeacherId() {
        var resp = controller.getMissionsByTeacherId(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testCreateMission() {
        var resp = controller.createMission(new MissionTemplate());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testUpdateMission() {
        var resp = controller.updateMission(1, new MissionTemplate());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testDeleteMission() {
        var resp = controller.deleteMission(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
}
