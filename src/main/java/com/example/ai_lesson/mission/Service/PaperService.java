package com.example.ai_lesson.mission.Service;

import com.example.ai_lesson.mission.Domain.Paper;
import com.example.ai_lesson.mission.Domain.Question;
import com.example.ai_lesson.mission.Mapper.PaperMapper;
import com.example.ai_lesson.mission.Mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaperService {
    private final QuestionMapper questionMapper;
    private final PaperMapper paperMapper;

    @Autowired
    public PaperService(QuestionMapper questionMapper, PaperMapper paperMapper) {
        this.questionMapper = questionMapper;
        this.paperMapper = paperMapper;
    }

    // 新增试卷列表查询方法
    public List<Paper> getPaperList() {
        return paperMapper.selectPaperList();
    }

    // 新增根据ID查询试卷
    public Paper getPaperById(int paperId) {
        return paperMapper.selectPaperById(paperId);
    }

    // 新增创建试卷
    public int createPaper(Paper paper) {
        return paperMapper.insertPaper(paper);
    }

    // 新增更新试卷
    public int updatePaper(Paper paper) {
        return paperMapper.updatePaper(paper);
    }

    // 新增删除试卷
    public int deletePaper(int paperId) {
        return paperMapper.deletePaper(paperId);
    }

    // 组卷功能：根据paperId获取所有题目生成试卷
    public List<Question> generatePaper(int paperId) {
        return questionMapper.getQuestionsByPaperId(paperId);
    }

    // 题目CRUD操作
    public Question getQuestionById(int questionId) {
        return questionMapper.getQuestionById(questionId);
    }

    public int createQuestion(Question question) {
        return questionMapper.insertQuestion(question);
    }

    public int updateQuestion(Question question) {
        return questionMapper.updateQuestion(question);
    }

    public int deleteQuestion(int questionId) {
        return questionMapper.deleteQuestion(questionId);
    }

    // 管理试卷与题目关系
    public int addQuestionToPaper(int paperId, int questionId) {
        return questionMapper.addQuestionToPaper(paperId, questionId);
    }

    public int removeQuestionFromPaper(int paperId, int questionId) {
        return questionMapper.removeQuestionFromPaper(paperId, questionId);
    }
}