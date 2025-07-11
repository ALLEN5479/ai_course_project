package com.example.ai_lesson.missiontest.Service;

import com.example.ai_lesson.mission.Service.PaperService;
import com.example.ai_lesson.mission.Domain.Paper;
import com.example.ai_lesson.mission.Mapper.PaperMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaperServiceTest {
    PaperService service;
    static class PaperMapperStub implements com.example.ai_lesson.mission.Mapper.PaperMapper {
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.Paper> findAll() { return java.util.List.of(new com.example.ai_lesson.mission.Domain.Paper()); }
        @Override
        public com.example.ai_lesson.mission.Domain.Paper findById(Integer paperId) { return new com.example.ai_lesson.mission.Domain.Paper(); }
        @Override
        public int insert(com.example.ai_lesson.mission.Domain.Paper paper) { paper.setPaperId(1); return 1; }
        @Override
        public int update(com.example.ai_lesson.mission.Domain.Paper paper) { return 1; }
        @Override
        public int deleteById(Integer paperId) { return 1; }
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.Paper> findByCourseId(Integer courseId) { return java.util.List.of(new com.example.ai_lesson.mission.Domain.Paper()); }
    }

    @BeforeEach
    void setUp() {
        service = new PaperService();
        setField(service, "paperMapper", new PaperMapperStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }

    @Test void testGetAllPapers() { assertFalse(service.getAllPapers().isEmpty()); }
    @Test void testGetPaperById() { assertNotNull(service.getPaperById(1)); }
    @Test void testCreatePaper() { assertNotNull(service.createPaper(new Paper())); }
    @Test void testUpdatePaper() { assertTrue(service.updatePaper(new Paper())); }
    @Test void testDeletePaper() { assertTrue(service.deletePaper(1)); }
    @Test void testGetPapersByCourseId() { assertFalse(service.getPapersByCourseId(1).isEmpty()); }
}