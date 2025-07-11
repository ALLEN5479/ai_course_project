package com.example.ai_lesson.career.mapper;

import com.example.ai_lesson.career.entity.CareerAbilityData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CareerAbilityMapper {
    void insertOrUpdate(CareerAbilityData data);
    CareerAbilityData selectByStudentId(String studentId);

    // 新增
    List<CareerAbilityData> selectPage(@Param("offset") int offset, @Param("size") int size);
    int countAll();
    int countAiReportNotNull();
    Double avgTotalScore();
    List<CareerAbilityData> selectAll(); // 可选，调试用
    // 分页查询
    List<CareerAbilityData> select_page(@Param("offset") int offset, @Param("size") int size);
    // 总数统计
    int count_all();
    // AI报告不为空的数量
    int count_ai_report_not_null();
    // 平均总分（以六项能力分数平均）
    Double avg_total_score();
    // 其他方法
    int deleteByStudentId(String studentId);
}
