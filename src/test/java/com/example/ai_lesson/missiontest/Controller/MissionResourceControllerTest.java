package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.MissionResourceController;
import com.example.ai_lesson.mission.Mapper.MissionResourceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import static org.junit.jupiter.api.Assertions.*;

class MissionResourceControllerTest {
    MissionResourceController controller;
    static class MissionResourceMapperStub implements MissionResourceMapper {
        @Override
        public int insertMissionResource(com.example.ai_lesson.mission.Domain.MissionResource resource) {
            return 1;
        }

    }
    @BeforeEach void setUp() {
        controller = new MissionResourceController();
        setField(controller, "missionResourceMapper", new MissionResourceMapperStub());
        setField(controller, "teacherReportDir", "testdir");
        setField(controller, "teacherReportAccessPath", "/testpath/");
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }
    @Test void testDummy() { assertNotNull(controller); }
}
