package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.TaskGradingController;
import com.example.ai_lesson.mission.Service.MissionSubmissionService;
import com.example.ai_lesson.mission.Domain.MissionSubmission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TaskGradingControllerTest {
    TaskGradingController controller;
    static class MissionSubmissionServiceStub extends MissionSubmissionService {
        @Override public List<MissionSubmission> getSubmissionsByPublishedMissionId(Integer id) { return List.of(new MissionSubmission()); }
        @Override public List<MissionSubmission> getSubmissionsByMissionAndSubmitStatus(Integer id, String s) { return List.of(new MissionSubmission()); }
        @Override public List<MissionSubmission> getSubmissionsByMissionAndGradeStatus(Integer id, String s) { return List.of(new MissionSubmission()); }
        @Override public MissionSubmission getSubmissionById(Integer id) { return new MissionSubmission(); }
        @Override public boolean gradeSubmissionByMissionAndStudent(Integer missionId, Integer studentId, Integer score, String comment) { return true; }
        @Override public boolean batchGrade(Integer publishedMissionId, List<Integer> submissionIds, Integer score, String comment, Integer graderId) { return true; }
        @Override public boolean saveReportScore(Integer missionId, String studentId, Integer score) { return true; }
    }
    static class JdbcTemplateStub extends JdbcTemplate {
        @Override public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) { return null; }
        @Override public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) { return List.of(); }
        @Override public List<Map<String, Object>> queryForList(String sql, Object... args) { return List.of(); }
    }
    @BeforeEach void setUp() {
        controller = new TaskGradingController();
        setField(controller, "missionSubmissionService", new MissionSubmissionServiceStub());
        setField(controller, "jdbcTemplate", new JdbcTemplateStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }
    @Test void testGetSubmissionsByPublishedMissionId() {
        var resp = controller.getSubmissionsByPublishedMissionId(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetSubmissionsBySubmitStatus() {
        var resp = controller.getSubmissionsBySubmitStatus(1, "submitted");
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetSubmissionsByGradeStatus() {
        var resp = controller.getSubmissionsByGradeStatus(1, "graded");
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetSubmissionById() {
        var resp = controller.getSubmissionById(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGradeSubmissionByMissionAndStudent() {
        var resp = controller.gradeSubmissionByMissionAndStudent(Map.of("missionId",1,"studentId",1,"score",100,"comment","good"));
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testBatchGrade() {
        var resp = controller.batchGrade(1, List.of(1,2), 100, "good", 1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
}
