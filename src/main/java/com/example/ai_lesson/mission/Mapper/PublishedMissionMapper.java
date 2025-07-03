package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.PublishedMission;
import com.example.ai_lesson.mission.Domain.ReportResource;
import com.example.ai_lesson.mission.Domain.ShowPublilshed;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PublishedMissionMapper {
    
    @Select("SELECT * FROM published_mission ORDER BY create_time DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "missionId", column = "mission_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    List<PublishedMission> findAll();
    
    @Select("SELECT * FROM published_mission WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "missionId", column = "mission_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    PublishedMission findById(Integer id);
    
    @Select("SELECT * FROM published_mission WHERE mission_id = #{missionId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "missionId", column = "mission_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    List<PublishedMission> findByMissionId(Integer missionId);
    
    @Select("SELECT * FROM published_mission WHERE status = #{status}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "missionId", column = "mission_id"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "status", column = "status"),
        @Result(property = "createTime", column = "create_time")
    })
    List<PublishedMission> findByStatus(String status);
    
    @Insert("INSERT INTO published_mission (mission_id, start_time, end_time, status, create_time) " +
            "VALUES (#{missionId}, #{startTime}, #{endTime}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PublishedMission publishedMission);
    
    @Update("UPDATE published_mission SET start_time = #{startTime}, end_time = #{endTime}, status = #{status} " +
            "WHERE id = #{id}")
    int update(PublishedMission publishedMission);
    
    @Update("UPDATE published_mission SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
    
    @Delete("DELETE FROM published_mission WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("SELECT " +
            "    mt.mission_id," +
            "    mt.mission_name," +
            "    mt.mission_description," +
            "    mt.mission_type," +
            "    pm.start_time," +
            "    pm.end_time " +
            "FROM " +
            "    mission_template mt " +
            "JOIN " +
            "    published_mission pm ON mt.mission_id = pm.mission_id;")
    List<ShowPublilshed> showPublishedMissions();

    @Select("select content from mission_template where mission_id=#{mission_id}")
    String getContent(Integer mission_id);

    @Select("SELECT path, resource_name " +
            "FROM mission_resource " +
            "WHERE resource_id = #{resource_id}")
    ReportResource getResource(int resource_id);

    @Insert("INSERT INTO mission_student_score (mission_id, student_id, score) " +
            "VALUES (#{mission_id}, #{student_id}, #{score}) " +
            "ON DUPLICATE KEY UPDATE " +
            "score = VALUES(score)")
    int updateScore(@Param("mission_id") int mission_id, @Param("student_id") String student_id, @Param("score") int score);
} 