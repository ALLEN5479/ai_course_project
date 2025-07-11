package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.MissionController;
import com.example.ai_lesson.mission.Domain.Mission;
import com.example.ai_lesson.mission.Service.MissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MissionControllerTest {
    MissionController controller;
    static class MissionServiceStub extends MissionService {
        @Override public Map<String, Object> selectMissionList(Integer pageNum, Integer pageSize, String missionName, String missionType, String teachingClass) {
            Map<String, Object> map = new HashMap<>();
            map.put("success", true); map.put("data", List.of(new Mission())); return map;
        }
        @Override public Mission selectMissionById(Integer missionId) { return new Mission(); }
        @Override public int insertMission(Mission mission) { mission.setMissionId(1); return 1; }
        @Override public int updateMission(Mission mission) { return 1; }
        @Override public int deleteMissionById(Integer missionId) { return 1; }
    }
    @BeforeEach void setUp() {
        controller = new MissionController();
        setField(controller, "missionService", new MissionServiceStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }

    @Test void testList() {
        var resp = controller.list(1, 10, null, null, null);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetInfo() {
        var resp = controller.getInfo(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testAdd() {
        Mission m = new Mission(); m.setMissionName("test");
        var resp = controller.add(m);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testEdit() {
        Mission m = new Mission(); m.setMissionId(1); m.setMissionName("edit");
        var resp = controller.edit(m);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testRemove() {
        var resp = controller.remove(1);
        assertTrue((Boolean) resp.getBody().get("success"));
    }
    @Test void testGetMissionTypes() {
        var resp = controller.getMissionTypes();
        assertTrue((Boolean) resp.getBody().get("success"));
    }
}
 