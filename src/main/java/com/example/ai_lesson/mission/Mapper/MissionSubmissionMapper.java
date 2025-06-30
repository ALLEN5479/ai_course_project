package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.MissionSubmission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MissionSubmissionMapper {
    
    @Select("SELECT * FROM mission_submission WHERE published_mission_id = #{publishedMissionId}")
    List<MissionSubmission> findByPublishedMissionId(Integer publishedMissionId);
    
    @Select("SELECT * FROM mission_submission WHERE submission_id = #{submissionId}")
    MissionSubmission findById(Integer submissionId);
    
    @Select("SELECT * FROM mission_submission WHERE student_id = #{studentId} AND published_mission_id = #{publishedMissionId}")
    MissionSubmission findByStudentAndMission(@Param("studentId") Integer studentId, @Param("publishedMissionId") Integer publishedMissionId);
    
    @Select("SELECT * FROM mission_submission WHERE published_mission_id = #{publishedMissionId} AND submit_status = #{submitStatus}")
    List<MissionSubmission> findByMissionAndSubmitStatus(@Param("publishedMissionId") Integer publishedMissionId, @Param("submitStatus") String submitStatus);
    
    @Select("SELECT * FROM mission_submission WHERE published_mission_id = #{publishedMissionId} AND grade_status = #{gradeStatus}")
    List<MissionSubmission> findByMissionAndGradeStatus(@Param("publishedMissionId") Integer publishedMissionId, @Param("gradeStatus") String gradeStatus);
    
    @Insert("INSERT INTO mission_submission (published_mission_id, student_id, submit_status, grade_status) " +
            "VALUES (#{publishedMissionId}, #{studentId}, #{submitStatus}, #{gradeStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "submissionId")
    int insert(MissionSubmission missionSubmission);
    
    @Update("UPDATE mission_submission SET submit_time = #{submitTime}, submit_status = #{submitStatus}, " +
            "file_path = #{filePath}, file_name = #{fileName}, file_size = #{fileSize} " +
            "WHERE submission_id = #{submissionId}")
    int updateSubmission(MissionSubmission missionSubmission);
    
    @Update("UPDATE mission_submission SET score = #{score}, comment = #{comment}, " +
            "grade_status = #{gradeStatus}, grade_time = #{gradeTime}, grader_id = #{graderId} " +
            "WHERE submission_id = #{submissionId}")
    int updateGrade(MissionSubmission missionSubmission);
    
    @Update("UPDATE mission_submission SET score = #{score}, comment = #{comment}, " +
            "grade_status = 'graded', grade_time = NOW(), grader_id = #{graderId} " +
            "WHERE published_mission_id = #{publishedMissionId} AND submission_id IN " +
            "<foreach collection='submissionIds' item='id' open='(' separator=',' close=')'>#{id}</foreach>")
    int batchGrade(@Param("publishedMissionId") Integer publishedMissionId, 
                   @Param("submissionIds") List<Integer> submissionIds,
                   @Param("score") Integer score, 
                   @Param("comment") String comment,
                   @Param("graderId") Integer graderId);
} 