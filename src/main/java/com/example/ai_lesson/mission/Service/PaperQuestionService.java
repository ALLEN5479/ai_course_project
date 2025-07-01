package com.example.ai_lesson.mission.Service;

import com.example.ai_lesson.mission.Domain.PaperQuestion;
import com.example.ai_lesson.mission.Mapper.PaperQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaperQuestionService {
    
    @Autowired
    private PaperQuestionMapper paperQuestionMapper;
    
    public List<PaperQuestion> getPaperQuestionsByPaperId(Integer paperId) {
        return paperQuestionMapper.findByPaperId(paperId);
    }
    
    public PaperQuestion getPaperQuestionById(Integer id) {
        return paperQuestionMapper.findById(id);
    }
    
    public PaperQuestion createPaperQuestion(PaperQuestion paperQuestion) {
        paperQuestionMapper.insert(paperQuestion);
        return paperQuestion;
    }
    
    public boolean updatePaperQuestion(PaperQuestion paperQuestion) {
        return paperQuestionMapper.update(paperQuestion) > 0;
    }
    
    public boolean deletePaperQuestion(Integer id) {
        return paperQuestionMapper.deleteById(id) > 0;
    }
    
    public boolean deletePaperQuestionsByPaperId(Integer paperId) {
        return paperQuestionMapper.deleteByPaperId(paperId) > 0;
    }
    
    public boolean batchCreatePaperQuestions(List<PaperQuestion> paperQuestions) {
        return paperQuestionMapper.batchInsert(paperQuestions) > 0;
    }
    
    public Map<String, Object> getQuestionDetailById(Integer questionId) {
        return paperQuestionMapper.findQuestionDetailById(questionId);
    }
} 