package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.TaskManagerController;
import com.example.ai_lesson.mission.Service.PublishedMissionService;
import com.example.ai_lesson.mission.Domain.PublishedMission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerControllerTest {
    TaskManagerController controller;
    static class PublishedMissionServiceStub extends PublishedMissionService {
        @Override public List<PublishedMission> getAllPublishedMissions() { return List.of(new PublishedMission()); }
        @Override public PublishedMission getPublishedMissionById(Integer id) { return new PublishedMission(); }
        @Override public List<PublishedMission> getPublishedMissionsByMissionId(Integer missionId) { return List.of(new PublishedMission()); }
        @Override public List<PublishedMission> getPublishedMissionsByStatus(String status) { return List.of(new PublishedMission()); }
        @Override public PublishedMission publishMission(PublishedMission m) { return m; }
        @Override public boolean updatePublishedMission(PublishedMission m) { return true; }
        @Override public boolean updatePublishedMissionStatus(Integer id, String status) { return true; }
        @Override public boolean deletePublishedMission(Integer id) { return true; }
    }
    @BeforeEach void setUp() {
        controller = new TaskManagerController();
        setField(controller, "publishedMissionService", new PublishedMissionServiceStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }
    @Test void testGetAllPublishedMissions() {
        var resp = controller.getAllPublishedMissions();
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetPublishedMissionById() {
        var resp = controller.getPublishedMissionById(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetPublishedMissionsByMissionId() {
        var resp = controller.getPublishedMissionsByMissionId(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetPublishedMissionsByStatus() {
        var resp = controller.getPublishedMissionsByStatus("status");
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testPublishMission() {
        var resp = controller.publishMission(new PublishedMission());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testUpdatePublishedMission() {
        var resp = controller.updatePublishedMission(1, new PublishedMission());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testUpdatePublishedMissionStatus() {
        var resp = controller.updatePublishedMissionStatus(1, "status");
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testDeletePublishedMission() {
        var resp = controller.deletePublishedMission(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
}
