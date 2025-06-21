package com.example.ai_lesson.mission.Mapper;

import com.example.ai_lesson.mission.Domain.ClassInfo;
import com.example.ai_lesson.mission.Domain.Mission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassInfoMapper {

    /**
     * 新增班级信息
     *
     * @param classInfo 班级信息对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO class(class_name, total_number) " +
            "VALUES(#{className}, #{totalStudents})")
    @Options(useGeneratedKeys = true, keyProperty = "classId", keyColumn = "class_id")
    int insertClass(ClassInfo classInfo);

    /**
     * 根据班级ID删除班级信息
     *
     * @param classId 班级ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM class WHERE class_id = #{classId}")
    int deleteClassById(@Param("classId") int classId);

    /**
     * 更新班级信息
     *
     * @param classInfo 班级信息对象
     * @return 影响的行数
     */
    @Update("UPDATE class SET " +
            "class_name = #{className}, " +
            "total_number = #{totalStudents} " +
            "WHERE class_id = #{classId}")
    int updateClass(ClassInfo classInfo);

    /**
     * 根据班级ID查询班级信息
     *
     * @param classId 班级ID
     * @return 班级信息对象
     */
    @Select("SELECT " +
            "class_id AS classId, " +
            "class_name AS className, " +
            "total_number AS totalStudents " +
            "FROM class " +
            "WHERE class_id = #{classId}")
    @Results({
            @Result(property = "classId", column = "classId"),
            @Result(property = "className", column = "className"),
            @Result(property = "totalStudents", column = "totalStudents"),
            @Result(property = "missions", javaType = List.class, column = "className",
                    many = @Many(select = "findMissionsByClassName"))
    })
    ClassInfo findClassById(@Param("classId") int classId);

    /**
     * 查询所有班级信息（包含关联的任务）
     *
     * @return 班级信息列表
     */
    @Select("SELECT " +
            "class_id AS classId, " +
            "class_name AS className, " +
            "total_number AS totalStudents " +
            "FROM class")
    @Results({
            @Result(property = "classId", column = "classId"),
            @Result(property = "className", column = "className"),
            @Result(property = "totalStudents", column = "totalStudents"),
            @Result(property = "missions", javaType = List.class, column = "className",
                    many = @Many(select = "findMissionsByClassName"))
    })
    List<ClassInfo> findAllClasses();

    /**
     * 根据班级名称查询班级信息
     *
     * @param className 班级名称
     * @return 班级信息对象
     */
    @Select("SELECT " +
            "class_id AS classId, " +
            "class_name AS className, " +
            "total_number AS totalStudents " +
            "FROM class " +
            "WHERE class_name = #{className}")
    @Results({
            @Result(property = "classId", column = "classId"),
            @Result(property = "className", column = "className"),
            @Result(property = "totalStudents", column = "totalStudents"),
            @Result(property = "missions", javaType = List.class, column = "className",
                    many = @Many(select = "findMissionsByClassName"))
    })
    ClassInfo findClassByName(@Param("className") String className);

    /**
     * 根据班级名称查询关联的任务
     *
     * @param className 班级名称
     * @return 任务列表
     */
    @Select("SELECT " +
            "mission_id AS missionId, " +
            "mission_name AS missionName, " +
            "mission_type AS missionType, " +
            "mission_description AS missionDescription, " +
            "DATE_FORMAT(start_time, '%Y-%m-%d %H:%i:%s') AS start_time, " +
            "DATE_FORMAT(end_time, '%Y-%m-%d %H:%i:%s') AS end_time, " +
            "content, " +
            "teaching_class AS teachingClass " +
            "FROM mission " +
            "WHERE teaching_class = #{className} " +
            "ORDER BY start_time DESC")
    List<Mission> findMissionsByClassName(@Param("className") String className);

    /**
     * 统计班级数量
     *
     * @return 班级总数
     */
    @Select("SELECT COUNT(*) FROM class")
    int countClasses();

    /**
     * 分页查询班级信息
     *
     * @param offset   偏移量
     * @param pageSize 每页大小
     * @return 班级信息列表
     */
    @Select("SELECT " +
            "class_id AS classId, " +
            "class_name AS className, " +
            "total_number AS totalStudents " +
            "FROM class " +
            "LIMIT #{offset}, #{pageSize}")
    @Results({
            @Result(property = "classId", column = "classId"),
            @Result(property = "className", column = "className"),
            @Result(property = "totalStudents", column = "totalStudents"),
            @Result(property = "missions", javaType = List.class, column = "className",
                    many = @Many(select = "findMissionsByClassName"))
    })
    List<ClassInfo> findClassesByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT " +
            "c.class_id AS classId, " +
            "c.class_name AS className, " +
            "c.total_number AS totalStudents " +
            "FROM class c " +
            "ORDER BY c.class_name")
    @Results({
            @Result(property = "classId", column = "classId"),
            @Result(property = "className", column = "className"),
            @Result(property = "totalStudents", column = "totalStudents"),
            @Result(property = "missions", column = "classId",
                    many = @Many(select = "findMissionsWithStatsByClassId"))
    })
    List<ClassInfo> findAllClassesWithMissionsAndStats();
    /**
     * 根据班级ID查询关联的任务及统计信息
     *
     * @param classId 班级ID
     * @return 任务列表包含统计信息
     */
    @Select("SELECT " +
            "m.mission_id AS missionId, " +
            "m.mission_name AS missionName, " +
            "m.mission_type AS missionType, " +
            "DATE_FORMAT(m.start_time, '%Y-%m-%d %H:%i:%s') AS start_time, " +
            "DATE_FORMAT(m.end_time, '%Y-%m-%d %H:%i:%s') AS end_time, " +
            "cm.completed_number AS completedNumber " +
            "FROM mission m " +
            "JOIN class_mission cm ON m.mission_id = cm.mission_id " +
            "WHERE cm.class_id = #{classId} " +
            "ORDER BY m.start_time DESC")
    List<Mission> findMissionsWithStatsByClassId(@Param("classId") int classId);

    /**
     * 获取任务统计数据
     *
     * @param missionId 任务ID
     * @return 任务统计信息
     */
    @Select("SELECT " +
            "completed_number AS completedNumber " +
            "FROM class_mission " +
            "WHERE mission_id = #{missionId}")
    int findMissionStatistics(@Param("missionId") int missionId);


}