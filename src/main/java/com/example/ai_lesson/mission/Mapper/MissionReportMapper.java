package com.example.ai_lesson.mission.Mapper;



import com.example.ai_lesson.mission.Domain.MissionReport;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface MissionReportMapper {
    @Select("SELECT report_id AS id, mission_id as missionId, path, file_name AS fileName " +
            "FROM mission_report where report_id = #{id}")
    public MissionReport selectMissionReportById(Integer id);
    @Insert("insert into mission_report(mission_id,path,file_name) values(#{missionId},#{path},#{fileName})")
    public int insertMissionReport(MissionReport missionReport);
    @Update("update mission_report set report_id = #{id} mission_id = #{missionId},path = #{path},file_name = #{fileName} where mission_id = #{missionId}")
    public int updateMissionReport(MissionReport missionReport);
    @Delete("delete from mission_report where mission_id = #{id}")
    public int deleteMissionReportById(Integer id);
    @Select("SELECT report_id AS id, mission_id, path, file_name AS fileName from mission_report" )
    List<MissionReport> selectAllMissionReportList(MissionReport missionReport);
    @Select("SELECT report_id AS id, mission_id, path, file_name AS fileName " +
            "FROM mission_report where mission_id = #{id}")
    List<MissionReport> selectMissionReportList(Integer id);
    /**
     * 根据文件路径查询报告
     * @param path 文件路径（唯一）
     * @return 匹配的报告对象，未找到返回null
     */
    @Select("SELECT report_id AS id, mission_id, path, file_name AS fileName " +
            "FROM mission_report WHERE path = #{path}")
    MissionReport selectByPath(String path);
}
