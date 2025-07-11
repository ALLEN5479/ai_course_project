package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.PaperQuestionController;
import com.example.ai_lesson.mission.Service.PaperQuestionService;
import com.example.ai_lesson.mission.Domain.PaperQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PaperQuestionControllerTest {
    PaperQuestionController controller;
    static class PaperQuestionServiceStub extends PaperQuestionService {
        @Override public List<PaperQuestion> getPaperQuestionsByPaperId(Integer paperId) { return List.of(new PaperQuestion()); }
        @Override public PaperQuestion getPaperQuestionById(Integer id) { return new PaperQuestion(); }
        @Override public PaperQuestion createPaperQuestion(PaperQuestion pq) { return pq; }
        @Override public boolean batchCreatePaperQuestions(List<PaperQuestion> pqs) { return true; }
        @Override public boolean updatePaperQuestion(PaperQuestion pq) { return true; }
        @Override public boolean deletePaperQuestion(Integer id) { return true; }
        @Override public boolean deletePaperQuestionsByPaperId(Integer paperId) { return true; }
    }
    @BeforeEach void setUp() {
        controller = new PaperQuestionController();
        setField(controller, "paperQuestionService", new PaperQuestionServiceStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }
    @Test void testGetPaperQuestionsByPaperId() {
        var resp = controller.getPaperQuestionsByPaperId(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetPaperQuestionById() {
        var resp = controller.getPaperQuestionById(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testCreatePaperQuestion() {
        var resp = controller.createPaperQuestion(new PaperQuestion());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testBatchCreatePaperQuestions() {
        var resp = controller.batchCreatePaperQuestions(List.of(new PaperQuestion()));
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testUpdatePaperQuestion() {
        var resp = controller.updatePaperQuestion(1, new PaperQuestion());
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testDeletePaperQuestion() {
        var resp = controller.deletePaperQuestion(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testDeletePaperQuestionsByPaperId() {
        var resp = controller.deletePaperQuestionsByPaperId(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
}
