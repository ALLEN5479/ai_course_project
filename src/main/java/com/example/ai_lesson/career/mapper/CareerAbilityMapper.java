package com.example.ai_lesson.career.mapper;

import com.example.ai_lesson.career.entity.CareerAbilityData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CareerAbilityMapper {
    void insertOrUpdate(CareerAbilityData data);
    CareerAbilityData selectByStudentId(String studentId);
    // 其他方法
}
