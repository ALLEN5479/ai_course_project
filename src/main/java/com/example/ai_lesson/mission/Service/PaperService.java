package com.example.ai_lesson.mission.Service;

import com.example.ai_lesson.mission.Domain.Paper;
import com.example.ai_lesson.mission.Mapper.PaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperService {
    
    @Autowired
    private PaperMapper paperMapper;
    
    public List<Paper> getAllPapers() {
        return paperMapper.findAll();
    }
    
    public Paper getPaperById(Integer paperId) {
        return paperMapper.findById(paperId);
    }
    
    public Paper createPaper(Paper paper) {
        paperMapper.insert(paper);
        return paper;
    }
    
    public boolean updatePaper(Paper paper) {
        return paperMapper.update(paper) > 0;
    }
    
    public boolean deletePaper(Integer paperId) {
        return paperMapper.deleteById(paperId) > 0;
    }
    
    public List<Paper> getPapersByCourseId(Integer courseId) {
        return paperMapper.findByCourseId(courseId);
    }
}