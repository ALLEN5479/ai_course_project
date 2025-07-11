package com.example.ai_lesson.missiontest.Service;

import com.example.ai_lesson.mission.Service.MissionSubmissionService;
import com.example.ai_lesson.mission.Domain.MissionSubmission;
import com.example.ai_lesson.mission.Mapper.MissionSubmissionMapper;
import com.example.ai_lesson.mission.Mapper.MissionStudentScoreMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionSubmissionServiceTest {
    MissionSubmissionService service;

    static class MissionSubmissionMapperStub implements com.example.ai_lesson.mission.Mapper.MissionSubmissionMapper {
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.MissionSubmission> findByPublishedMissionId(Integer publishedMissionId) { return java.util.List.of(new com.example.ai_lesson.mission.Domain.MissionSubmission()); }
        @Override
        public com.example.ai_lesson.mission.Domain.MissionSubmission findById(Integer submissionId) { return new com.example.ai_lesson.mission.Domain.MissionSubmission(); }
        @Override
        public com.example.ai_lesson.mission.Domain.MissionSubmission findByStudentAndMission(Integer studentId, Integer publishedMissionId) { return new com.example.ai_lesson.mission.Domain.MissionSubmission(); }
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.MissionSubmission> findByMissionAndSubmitStatus(Integer publishedMissionId, String submitStatus) { return java.util.List.of(new com.example.ai_lesson.mission.Domain.MissionSubmission()); }
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.MissionSubmission> findByMissionAndGradeStatus(Integer publishedMissionId, String gradeStatus) { return java.util.List.of(new com.example.ai_lesson.mission.Domain.MissionSubmission()); }
        @Override
        public int insert(com.example.ai_lesson.mission.Domain.MissionSubmission missionSubmission) { missionSubmission.setSubmissionId(1); return 1; }
        @Override
        public int updateSubmission(com.example.ai_lesson.mission.Domain.MissionSubmission missionSubmission) { return 1; }
        @Override
        public int updateGrade(com.example.ai_lesson.mission.Domain.MissionSubmission missionSubmission) { return 1; }
        @Override
        public int updateGradeByMissionAndStudent(Integer missionId, Integer studentId, Integer score, String comment) { return 1; }
        @Override
        public int batchGrade(Integer publishedMissionId, java.util.List<Integer> submissionIds, Integer score, String comment, Integer graderId) { return 1; }
    }
    static class MissionStudentScoreMapperStub implements MissionStudentScoreMapper {
        @Override public int insertScore(Integer missionId, String studentId, Integer score) { return 1; }
    }

    @BeforeEach
    void setUp() {
        service = new MissionSubmissionService();
        setField(service, "missionSubmissionMapper", new MissionSubmissionMapperStub());
        setField(service, "missionStudentScoreMapper", new MissionStudentScoreMapperStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }

    @Test void testGetSubmissionsByPublishedMissionId() { assertFalse(service.getSubmissionsByPublishedMissionId(1).isEmpty()); }
    @Test void testGetSubmissionById() { assertNotNull(service.getSubmissionById(1)); }
    @Test void testGetSubmissionByStudentAndMission() { assertNotNull(service.getSubmissionByStudentAndMission(1, 1)); }
    @Test void testGetSubmissionsByMissionAndSubmitStatus() { assertFalse(service.getSubmissionsByMissionAndSubmitStatus(1, "submitted").isEmpty()); }
    @Test void testGetSubmissionsByMissionAndGradeStatus() { assertFalse(service.getSubmissionsByMissionAndGradeStatus(1, "graded").isEmpty()); }
    @Test void testCreateSubmission() { assertNotNull(service.createSubmission(new MissionSubmission())); }
    @Test void testUpdateSubmission() { assertTrue(service.updateSubmission(new MissionSubmission())); }
    @Test void testGradeSubmission() { assertTrue(service.gradeSubmission(new MissionSubmission())); }
    @Test void testBatchGrade() { assertTrue(service.batchGrade(1, List.of(1), 100, "good", 1)); }
    @Test void testSaveReportScore() { assertTrue(service.saveReportScore(1, "1", 100)); }
    @Test void testGradeSubmissionByMissionAndStudent() { assertTrue(service.gradeSubmissionByMissionAndStudent(1, 1, 100, "good")); }
}