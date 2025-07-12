package com.example.ai_lesson.carrer_abilitytest;

import com.example.ai_lesson.career.service.CareerAbilityService;
import com.example.ai_lesson.career.entity.CareerAbilityData;
import com.example.ai_lesson.common.AjaxResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CareerAbilityServiceTest {

    @Autowired
    private CareerAbilityService careerAbilityService;

    @Test
    void testGetStudentCareerAbility() {
        CareerAbilityData data = careerAbilityService.getStudentCareerAbility("12121");
        assertNotNull(data);
        assertTrue(data.getCourse_score() > 0);
        assertNotNull(data.getAi_report());
    }

    @Test
    void testGetStudentCareerAbilityNotFound() {
        CareerAbilityData data = careerAbilityService.getStudentCareerAbility("99999");
        assertNull(data);
    }

    @Test
    void testGenerateAiReport() {
        AjaxResult result = careerAbilityService.generateAiReport("12121");
        assertEquals(200, result.getCode());
    }

    @Test
    void testGetCareerAbilityList() {
        Map<String, Object> result = careerAbilityService.getCareerAbilityList(1, 10);
        assertNotNull(result.get("list"));
        assertNotNull(result.get("stats"));
    }

    @Test
    void testDeleteCareerAbility() {
        int rows = careerAbilityService.deleteCareerAbility("12121");
        assertTrue(rows >= 0);
    }

    @Test
    void testGetStudentCareerAbilityNotExist() {
        CareerAbilityData data = careerAbilityService.getStudentCareerAbility("99999");
        assertNull(data);
    }
}
