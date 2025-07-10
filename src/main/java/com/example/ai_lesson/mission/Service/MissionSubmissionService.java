package com.example.ai_lesson.mission.Service;

import com.example.ai_lesson.mission.Domain.MissionSubmission;
import com.example.ai_lesson.mission.Mapper.MissionSubmissionMapper;
import com.example.ai_lesson.mission.Mapper.MissionStudentScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MissionSubmissionService {
    
    @Autowired
    private MissionSubmissionMapper missionSubmissionMapper;
    
    @Autowired
    private MissionStudentScoreMapper missionStudentScoreMapper;
    
    public List<MissionSubmission> getSubmissionsByPublishedMissionId(Integer publishedMissionId) {
        return missionSubmissionMapper.findByPublishedMissionId(publishedMissionId);
    }
    
    public MissionSubmission getSubmissionById(Integer submissionId) {
        return missionSubmissionMapper.findById(submissionId);
    }
    
    public MissionSubmission getSubmissionByStudentAndMission(Integer studentId, Integer publishedMissionId) {
        return missionSubmissionMapper.findByStudentAndMission(studentId, publishedMissionId);
    }
    
    public List<MissionSubmission> getSubmissionsByMissionAndSubmitStatus(Integer publishedMissionId, String submitStatus) {
        return missionSubmissionMapper.findByMissionAndSubmitStatus(publishedMissionId, submitStatus);
    }
    
    public List<MissionSubmission> getSubmissionsByMissionAndGradeStatus(Integer publishedMissionId, String gradeStatus) {
        return missionSubmissionMapper.findByMissionAndGradeStatus(publishedMissionId, gradeStatus);
    }
    
    public MissionSubmission createSubmission(MissionSubmission missionSubmission) {
        missionSubmissionMapper.insert(missionSubmission);
        return missionSubmission;
    }
    
    public boolean updateSubmission(MissionSubmission missionSubmission) {
        missionSubmission.setSubmitTime(LocalDateTime.now());
        return missionSubmissionMapper.updateSubmission(missionSubmission) > 0;
    }
    
    public boolean gradeSubmission(MissionSubmission missionSubmission) {
        missionSubmission.setGradeTime(LocalDateTime.now());
        return missionSubmissionMapper.updateGrade(missionSubmission) > 0;
    }
    
    public boolean batchGrade(Integer publishedMissionId, List<Integer> submissionIds, 
                             Integer score, String comment, Integer graderId) {
        return missionSubmissionMapper.batchGrade(publishedMissionId, submissionIds, score, comment, graderId) > 0;
    }

    public boolean saveReportScore(Integer missionId, String studentId, Integer score) {
        return missionStudentScoreMapper.insertScore(missionId, studentId, score) > 0;
    }

    public boolean gradeSubmissionByMissionAndStudent(Integer missionId, Integer studentId, Integer score, String comment) {
        return missionSubmissionMapper.updateGradeByMissionAndStudent(missionId, studentId, score, comment) > 0;
    }
} 