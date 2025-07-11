package com.example.ai_lesson.missiontest.Service;

import com.example.ai_lesson.mission.Service.PaperQuestionService;
import com.example.ai_lesson.mission.Domain.PaperQuestion;
import com.example.ai_lesson.mission.Domain.ShowQuiz;
import com.example.ai_lesson.mission.Mapper.PaperQuestionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaperQuestionServiceTest {
    PaperQuestionService service;

    static class PaperQuestionMapperStub implements com.example.ai_lesson.mission.Mapper.PaperQuestionMapper {
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.PaperQuestion> findByPaperId(Integer paperId) { 
            return java.util.List.of(new com.example.ai_lesson.mission.Domain.PaperQuestion()); 
        }
        
        @Override
        public com.example.ai_lesson.mission.Domain.PaperQuestion findById(Integer id) { 
            return new com.example.ai_lesson.mission.Domain.PaperQuestion(); 
        }
        
        @Override
        public int insert(com.example.ai_lesson.mission.Domain.PaperQuestion paperQuestion) { 
            paperQuestion.setId(1); 
            return 1; 
        }
        
        @Override
        public int update(com.example.ai_lesson.mission.Domain.PaperQuestion paperQuestion) { 
            return 1; 
        }
        
        @Override
        public int deleteById(Integer id) { 
            return 1; 
        }
        
        @Override
        public int deleteByPaperId(Integer paperId) { 
            return 1; 
        }
        
        @Override
        public int batchInsert(java.util.List<com.example.ai_lesson.mission.Domain.PaperQuestion> paperQuestions) { 
            return 1; 
        }
        
        @Override
        public java.util.Map<String, Object> findQuestionDetailById(Integer questionId) { 
            return java.util.Map.of("id", questionId); 
        }
        
        @Override
        public java.util.List<java.util.Map<String, Object>> findQuestionDetailsByPaperId(Integer paperId) { 
            return java.util.List.of(); 
        }
        
        @Override
        public java.util.List<com.example.ai_lesson.mission.Domain.ShowQuiz> showQuiz(int paper_id) {
            return java.util.List.of(new com.example.ai_lesson.mission.Domain.ShowQuiz(
                "测试题目", "A", "B", "C", "D", "A", 5
            ));
        }
        
        @Override
        public int updateJudge(int question_id, String student_id, int judge) {
            return 1;
        }
    }
    
    @BeforeEach
    void setUp() {
        service = new PaperQuestionService();
        setField(service, "paperQuestionMapper", new PaperQuestionMapperStub());
    }
    
    void setField(Object t, String f, Object v) { 
        try { 
            var fld = t.getClass().getDeclaredField(f); 
            fld.setAccessible(true); 
            fld.set(t, v); 
        } catch (Exception e) { 
            throw new RuntimeException(e); 
        } 
    }

    @Test 
    void testGetPaperQuestionsByPaperId() { 
        assertFalse(service.getPaperQuestionsByPaperId(1).isEmpty()); 
    }
    
    @Test 
    void testGetPaperQuestionById() { 
        assertNotNull(service.getPaperQuestionById(1)); 
    }
    
    @Test 
    void testCreatePaperQuestion() { 
        assertNotNull(service.createPaperQuestion(new PaperQuestion())); 
    }
    
    @Test 
    void testUpdatePaperQuestion() { 
        assertTrue(service.updatePaperQuestion(new PaperQuestion())); 
    }
    
    @Test 
    void testDeletePaperQuestion() { 
        assertTrue(service.deletePaperQuestion(1)); 
    }
    
    @Test 
    void testDeletePaperQuestionsByPaperId() { 
        assertTrue(service.deletePaperQuestionsByPaperId(1)); 
    }
    
    @Test 
    void testBatchCreatePaperQuestions() { 
        assertTrue(service.batchCreatePaperQuestions(List.of(new PaperQuestion()))); 
    }
    
    @Test 
    void testGetQuestionDetailById() { 
        assertNotNull(service.getQuestionDetailById(1)); 
    }
    
    @Test 
    void testShowQuizs() { 
        List<ShowQuiz> result = service.showQuizs(1);
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0));
    }
    
    @Test 
    void testUpdateJudge() { 
        int result = service.updateJudge(1, "student123", 1);
        assertEquals(1, result);
    }
}