package com.example.ai_lesson.ability.StudentAbility;

import com.example.ai_lesson.ability.Domain.StudentAbility;
import com.example.ai_lesson.ability.Service.StudentAbilityService;
import com.example.ai_lesson.aiquestion.entity.QuizAnswerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentAbilityServiceTest {

    @Autowired
    private StudentAbilityService studentAbilityService;

    @Test
    void testGetCapabilityMap() {
        List<StudentAbility> abilities = studentAbilityService.getByStudentId(12121L);
        assertNotNull(abilities);
        assertEquals(5, abilities.size());
        for (StudentAbility ab : abilities) {
            assertTrue(ab.getEloScore() > 0);
        }
    }

    @Test
    void testGetGrammarEloByStudentId() {
        Double score = studentAbilityService.getGrammarEloByStudentId(12121L);
        assertNotNull(score);
        assertTrue(score >= 1000 && score <= 2500);
    }

    @Test
    void testGetEloByStudentIdAndAbilityType() {
        Double score = studentAbilityService.getEloByStudentIdAndAbilityType(12121L, "coding");
        assertNotNull(score);
    }

    @Test
    void testUpdateInitiativeAbility() {
        studentAbilityService.updateInitiativeAbility(12121L);
        Double score = studentAbilityService.getEloByStudentIdAndAbilityType(12121L, "initiative");
        assertNotNull(score);
    }


    @Test
    void testGetByStudentIdEmpty() {
        List<StudentAbility> abilities = studentAbilityService.getByStudentId(99999L);
        assertTrue(abilities == null || abilities.isEmpty());
    }


    @Test
    void testDeleteStudentAbility() {
        studentAbilityService.deleteStudentAbility(12121L);
        List<StudentAbility> abilities = studentAbilityService.getByStudentId(12121L);
        assertTrue(abilities == null || abilities.isEmpty());
    }
}
