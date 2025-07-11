package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.MissionSubmissionController;
import com.example.ai_lesson.mission.Service.MissionSubmissionService;
import com.example.ai_lesson.mission.Domain.MissionSubmission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MissionSubmissionControllerTest {
    MissionSubmissionController controller;
    static class MissionSubmissionServiceStub extends MissionSubmissionService {
        @Override public List<MissionSubmission> getSubmissionsByPublishedMissionId(Integer missionId) { return List.of(new MissionSubmission()); }
        @Override public MissionSubmission getSubmissionById(Integer id) { return new MissionSubmission(); }
        @Override public MissionSubmission createSubmission(MissionSubmission m) { return m; }
        @Override public boolean updateSubmission(MissionSubmission m) { return true; }
    }
    @BeforeEach void setUp() {
        controller = new MissionSubmissionController();
        setField(controller, "missionSubmissionService", new MissionSubmissionServiceStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }
    @Test void testGetMissionSubmissions() {
        var resp = controller.getMissionSubmissions(1, null);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetMissionSubmissionById() {
        var resp = controller.getMissionSubmissionById(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testSubmitMission() {
        var resp = controller.submitMission(new MissionSubmission());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testUpdateMissionSubmission() {
        var resp = controller.updateMissionSubmission(1, new MissionSubmission());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
}
