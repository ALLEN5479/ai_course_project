package com.example.ai_lesson.student_courses;

import com.example.ai_lesson.student_courses.controller.ManagementController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import com.example.ai_lesson.student_courses.entity.ManagementEntity;
import com.example.ai_lesson.student_courses.entity.StudentEntity;
import com.example.ai_lesson.student_courses.mapper.ManagementMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ManagementControllerTest {
    @Autowired
    private ManagementController managementController;

    @MockBean
    private ManagementMapper managementMapper;

    @Test
    void contextLoads() {
        assertNotNull(managementController);
    }

    @Test
    void testGetStudentNameAndId() {
        ManagementEntity m = new ManagementEntity();
        m.setStudent_id("t1");
        Mockito.when(managementMapper.getStudentNameAndId("t1")).thenReturn(Arrays.asList(m));
        List<ManagementEntity> result = managementController.getStudentNameAndId("t1");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("t1", result.get(0).getStudent_id());
    }

    @Test
    void testGetStudentNameAndId_Normal() {
        ManagementEntity m = new ManagementEntity();
        m.setStudent_id("t1");
        Mockito.when(managementMapper.getStudentNameAndId("t1")).thenReturn(Arrays.asList(m));
        List<ManagementEntity> result = managementController.getStudentNameAndId("t1");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("t1", result.get(0).getStudent_id());
    }

    @Test
    void testGetStudentNameAndId_Empty() {
        Mockito.when(managementMapper.getStudentNameAndId("t2")).thenReturn(Arrays.asList());
        List<ManagementEntity> result = managementController.getStudentNameAndId("t2");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteStudentCourse() {
        Mockito.when(managementMapper.deleteStudentCourse("u1", "课程A")).thenReturn(1);
        int res = managementController.deleteStudentCourse("u1", "课程A");
        assertEquals(1, res);
    }

    @Test
    void testDeleteStudentCourse_Normal() {
        Mockito.when(managementMapper.deleteStudentCourse("u1", "课程A")).thenReturn(1);
        int res = managementController.deleteStudentCourse("u1", "课程A");
        assertEquals(1, res);
    }

    @Test
    void testDeleteStudentCourse_Fail() {
        Mockito.when(managementMapper.deleteStudentCourse("u2", "课程B")).thenReturn(0);
        int res = managementController.deleteStudentCourse("u2", "课程B");
        assertEquals(0, res);
    }

    @Test
    void testGetstuMsg() {
        StudentEntity s = new StudentEntity();
        s.setStudent_id("u2");
        Mockito.when(managementMapper.getstuMsg()).thenReturn(Arrays.asList(s));
        List<StudentEntity> result = managementController.getstuMsg();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("u2", result.get(0).getStudent_id());
    }

    @Test
    void testGetstuMsg_Normal() {
        StudentEntity s = new StudentEntity();
        s.setStudent_id("u2");
        Mockito.when(managementMapper.getstuMsg()).thenReturn(Arrays.asList(s));
        List<StudentEntity> result = managementController.getstuMsg();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("u2", result.get(0).getStudent_id());
    }

    @Test
    void testGetstuMsg_Empty() {
        Mockito.when(managementMapper.getstuMsg()).thenReturn(Arrays.asList());
        List<StudentEntity> result = managementController.getstuMsg();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // TODO: 可根据实际接口添加更多测试方法
} 