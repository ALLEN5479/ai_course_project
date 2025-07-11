package com.example.ai_lesson.missiontest.Controller;

import com.example.ai_lesson.mission.Controller.PaperController;
import com.example.ai_lesson.mission.Domain.Paper;
import com.example.ai_lesson.mission.Service.PaperService;
import com.example.ai_lesson.mission.Service.PaperQuestionService;
import com.example.ai_lesson.mission.Mapper.PaperQuestionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaperControllerTest {

    private PaperService paperService;
    private PaperQuestionService paperQuestionService;
    private PaperQuestionMapper paperQuestionMapper;
    private PaperController paperController;

    @BeforeEach
    void setUp() {
        paperService = mock(PaperService.class);
        paperQuestionService = mock(PaperQuestionService.class);
        paperQuestionMapper = mock(PaperQuestionMapper.class);
        paperController = new PaperController();
        // 通过反射注入mock依赖
        setField(paperController, "paperService", paperService);
        setField(paperController, "paperQuestionService", paperQuestionService);
        setField(paperController, "paperQuestionMapper", paperQuestionMapper);
    }

    // 反射工具方法
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllPapers() {
        List<Paper> papers = List.of(new Paper());
        when(paperService.getAllPapers()).thenReturn(papers);

        ResponseEntity<Map<String, Object>> response = paperController.getAllPapers();
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(papers, response.getBody().get("data"));
    }

    @Test
    void testGetPapersByCourseId() {
        List<Paper> papers = List.of(new Paper());
        when(paperService.getPapersByCourseId(1)).thenReturn(papers);

        ResponseEntity<Map<String, Object>> response = paperController.getPapersByCourseId(1);
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(papers, response.getBody().get("data"));
    }

    @Test
    void testCreatePaperWithQuestions() {
        doNothing().when(paperService).createPaper(any(Paper.class));
        doNothing().when(paperQuestionService).batchCreatePaperQuestions(anyList());

        Map<String, Object> payload = new HashMap<>();
        payload.put("paper_name", "Test Paper");
        payload.put("paper_description", "desc");
        payload.put("total_score", 100);
        payload.put("courseId", 1);
        List<Map<String, Object>> questions = new ArrayList<>();
        Map<String, Object> q = new HashMap<>();
        q.put("question_id", 1);
        q.put("score", 5);
        q.put("sort_order", 1);
        q.put("question_type", "single");
        questions.add(q);
        payload.put("questions", questions);

        ResponseEntity<Map<String, Object>> response = paperController.createPaperWithQuestions(payload);
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("创建试卷成功", response.getBody().get("message"));
    }

    @Test
    void testGetPaperDetail() {
        Paper paper = new Paper();
        paper.setPaperId(1);
        when(paperService.getPaperById(1)).thenReturn(paper);
        when(paperQuestionMapper.findQuestionDetailsByPaperId(1)).thenReturn(List.of(Map.of("option_a", "A", "option_b", "B")));

        ResponseEntity<Map<String, Object>> response = paperController.getPaperDetail(1);
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(paper, response.getBody().get("data"));
        assertNotNull(response.getBody().get("questions"));
    }

    @Test
    void testUpdatePaper() {
        when(paperService.updatePaper(any(Paper.class))).thenReturn(true);
        Paper paper = new Paper();
        paper.setPaperName("Updated");
        paper.setPaperDescription("desc");
        paper.setTotalScore(100);
        paper.setCourseId(1);

        ResponseEntity<Map<String, Object>> response = paperController.updatePaper(1, paper);
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("更新试卷成功", response.getBody().get("message"));
    }

    @Test
    void testDeletePaper() {
        when(paperService.deletePaper(1)).thenReturn(true);
        ResponseEntity<Map<String, Object>> response = paperController.deletePaper(1);
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("删除试卷成功", response.getBody().get("message"));
    }
}