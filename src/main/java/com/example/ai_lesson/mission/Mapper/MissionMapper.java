package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.Mission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MissionMapper {
    /**
     * 分页查询任务列表
     */
    @Select("<script>" +
            "SELECT " +
            "mission_id AS missionId, " +
            "teacher_id AS teacherId, " +
            "mission_name AS missionName, " +
            "mission_type AS missionType, " +
            "teaching_class AS teachingClass, " +
            "DATE_FORMAT(start_time, '%Y-%m-%d %H:%i:%s') AS start_time, " +
            "DATE_FORMAT(end_time, '%Y-%m-%d %H:%i:%s') AS end_time, " +
            "contents AS contents, " +
            "mission_description AS missionDescription " +
            "state AS state " +
            "FROM mission " +
            "<where>" +
            "  <if test='missionName != null and missionName != \"\"'> AND mission_name LIKE CONCAT('%', #{missionName}, '%') </if>" +
            "  <if test='missionType != null and missionType != \"\"'> AND mission_type = #{missionType} </if>" +
            "  <if test='teachingClass != null and teachingClass != \"\"'> AND teaching_class = #{teachingClass} </if>" +
            "</where>" +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Mission> selectMissionList(@Param("offset") int offset,
                                    @Param("pageSize") int pageSize,
                                    @Param("missionName") String missionName,
                                    @Param("missionType") String missionType,
                                    @Param("teachingClass") String teachingClass);

    /**
     * 统计任务数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM mission " +
            "<where>" +
            "  <if test='missionName != null and missionName != \"\"'> AND mission_name LIKE CONCAT('%', #{missionName}, '%') </if>" +
            "  <if test='missionType != null and missionType != \"\"'> AND mission_type = #{missionType} </if>" +
            "  <if test='teachingClass != null and teachingClass != \"\"'> AND teaching_class = #{teachingClass} </if>" +
            "</where>" +
            "</script>")
    int countMissionList(@Param("missionName") String missionName,
                         @Param("missionType") String missionType,
                         @Param("teachingClass") String teachingClass);

    /**
     * 根据任务状态查询任务列表（用于任务界面）
     */
    @Select("<script>" +
            "SELECT mission_id AS missionId, mission_name AS missionName, " +
            "mission_type AS missionType, state " +
            "FROM mission " +
            "WHERE teacher_id = #{teacherId} " +
            "<if test='state != null'> AND state = #{state} </if>" +
            "<if test='missionName != null'> AND mission_name LIKE CONCAT('%', #{missionName}, '%') </if>" +
            "ORDER BY create_time DESC" +
            "</script>")
    List<Mission> selectMissionsByState(@Param("teacherId") Integer teacherId,
                                        @Param("state") Integer state,
                                        @Param("missionName") String missionName);

    /**
     * 新增任务
     */
    @Insert("INSERT INTO mission (mission_name, mission_type, teaching_class, start_time, end_time, content, mission_description) " +
            "VALUES (#{missionName}, #{missionType}, #{teachingClass}, " +
            "#{start_time,jdbcType=TIMESTAMP}, #{end_time,jdbcType=TIMESTAMP}, " +
            "#{content}, #{missionDescription})")
    int insertMission(Mission mission);
    /**
     * 修改任务
     */
    @Update("UPDATE mission SET " +
            "mission_name = #{missionName}, " +
            "mission_type = #{missionType}, " +
            "teaching_class = #{teachingClass}, " +
            "start_time = #{start_time}, " +
            "end_time = #{end_time}, " +
            "content = #{content}, " +
            "mission_description = #{missionDescription} " +
            "WHERE mission_id = #{missionId}")
    int updateMission(Mission mission);

    /**
     * 删除任务
     */
    @Delete("DELETE FROM mission WHERE mission_id = #{missionId}")
    int deleteMissionById(@Param("missionId") Integer missionId);

    Mission selectMissionById(Integer missionId);
}