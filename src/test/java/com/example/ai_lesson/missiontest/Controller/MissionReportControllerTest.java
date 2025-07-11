package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.MissionReportController;
import com.example.ai_lesson.mission.Mapper.MissionReportMapper;
import com.example.ai_lesson.mission.utils.FileLoad;
import com.example.ai_lesson.mission.Domain.MissionReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MissionReportControllerTest {
    MissionReportController controller;
    static class MissionReportMapperStub implements MissionReportMapper {
        @Override public int insertMissionReport(MissionReport r) { r.setId(1); return 1; }
        @Override public List<MissionReport> selectMissionReportList(Integer missionId) { return List.of(new MissionReport()); }
        @Override public MissionReport selectMissionReportById(Integer id) { return new MissionReport(); }
        @Override public MissionReport selectByPath(String path) { MissionReport r = new MissionReport(); r.setId(1); return r; }
        @Override public int updateMissionReport(MissionReport missionReport) { return 1; }
        @Override public int deleteMissionReportById(Integer id) { return 1; }
        @Override public List<MissionReport> selectAllMissionReportList(MissionReport missionReport) { return List.of(); }
    }
    static class FileLoadStub extends FileLoad {
        @Override public String uploadMissionFile(org.springframework.web.multipart.MultipartFile file) { return "path/to/file.pdf"; }
        @Override public Resource loadFileAsResource(String path) { return null; }
    }
    @BeforeEach void setUp() {
        controller = new MissionReportController();
        setField(controller, "missionReportMapper", new MissionReportMapperStub());
        setField(controller, "fileLoad", new FileLoadStub());
    }
    void setField(Object t, String f, Object v) { try { var fld = t.getClass().getDeclaredField(f); fld.setAccessible(true); fld.set(t, v); } catch (Exception e) { throw new RuntimeException(e); } }

    @Test void testGetReportsByMissionId() {
        var list = controller.getReportsByMissionId(1);
        assertNotNull(list);
    }
    @Test void testGetMissionReportById() {
        var resp = controller.getMissionReportById(1);
        assertNotNull(resp.getBody());
    }
}
