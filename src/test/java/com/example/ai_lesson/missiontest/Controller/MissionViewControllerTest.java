package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.MissionViewController;
import com.example.ai_lesson.mission.Mapper.ClassInfoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MissionViewControllerTest {
    MissionViewController controller;
    static class ClassInfoMapperStub implements com.example.ai_lesson.mission.Mapper.ClassInfoMapper {
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.ClassInfo> findAllClassesWithMissionsAndStats() { return java.util.List.of(); }
        @Override
        public int findMissionStatistics(int missionId) { return 1; }
        @Override
        public com.example.ai_lesson.mission.Domain.ClassInfo findClassById(int classId) { return new com.example.ai_lesson.mission.Domain.ClassInfo(); }
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.ClassInfo> findClassesByPage(int offset, int pageSize) { return java.util.List.of(); }
        @Override
        public int countClasses() { return 1; }
        // 其他接口方法可空实现或抛异常
        @Override public int insertClass(com.example.ai_lesson.mission.Domain.ClassInfo classInfo) { throw new UnsupportedOperationException(); }
        @Override public int deleteClassById(int classId) { throw new UnsupportedOperationException(); }
        @Override public int updateClass(com.example.ai_lesson.mission.Domain.ClassInfo classInfo) { throw new UnsupportedOperationException(); }
        @Override public com.example.ai_lesson.mission.Domain.ClassInfo findClassByName(String className) { throw new UnsupportedOperationException(); }
        @Override public java.util.List<com.example.ai_lesson.mission.Domain.Mission> findMissionsByClassName(String className) { throw new UnsupportedOperationException(); }
        @Override public java.util.List<com.example.ai_lesson.mission.Domain.ClassInfo> findAllClasses() { throw new UnsupportedOperationException(); }
        @Override public java.util.List<com.example.ai_lesson.mission.Domain.Mission> findMissionsWithStatsByClassId(int classId) { throw new UnsupportedOperationException(); }
    }
    @BeforeEach void setUp() {
        controller = new MissionViewController();
        setField(controller, "classInfoMapper", new ClassInfoMapperStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }
    @Test void testGetClassesWithMissions() {
        var resp = controller.getClassesWithMissions();
        assertNotNull(resp.get("data"));
    }
    @Test void testGetMissionStatistics() {
        var resp = controller.getMissionStatistics(1);
        assertNotNull(resp.get("data"));
    }
    @Test void testGetClassDetail() {
        var resp = controller.getClassDetail(1);
        assertNotNull(resp.get("data"));
    }
    @Test void testGetClassesByPage() {
        var resp = controller.getClassesByPage(1, 10);
        assertNotNull(resp.get("data"));
    }
}
