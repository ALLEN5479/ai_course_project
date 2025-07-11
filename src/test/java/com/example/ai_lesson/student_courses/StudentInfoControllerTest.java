package com.example.ai_lesson.student_courses;

import com.example.ai_lesson.student_courses.controller.StudentInfoController;
import com.example.ai_lesson.student_courses.entity.StudentInfoEntity;
import com.example.ai_lesson.student_courses.service.StudentInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentInfoControllerTest {
    @Autowired
    private StudentInfoController studentInfoController;

    @MockBean
    private StudentInfoService studentInfoService;

    @Test
    void contextLoads() {
        assertNotNull(studentInfoController);
    }

    @Test
    void testGetStudentInfo() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u1");
        Mockito.when(studentInfoService.getStudentInfo("u1")).thenReturn(entity);
        StudentInfoEntity result = studentInfoController.getStudentInfo("u1");
        assertNotNull(result);
        assertEquals("u1", result.getUser_id());
    }

    @Test
    void testGetStudentInfo_Normal() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u1");
        Mockito.when(studentInfoService.getStudentInfo("u1")).thenReturn(entity);
        StudentInfoEntity result = studentInfoController.getStudentInfo("u1");
        assertNotNull(result);
        assertEquals("u1", result.getUser_id());
    }

    @Test
    void testGetStudentInfo_NotFound() {
        Mockito.when(studentInfoService.getStudentInfo("u2")).thenReturn(null);
        StudentInfoEntity result = studentInfoController.getStudentInfo("u2");
        assertNull(result);
    }

    @Test
    void testUpdateStudentInfo() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u2");
        Mockito.doNothing().when(studentInfoService).saveOrUpdateStudentInfo(entity);
        String res = studentInfoController.updateStudentInfo(entity);
        assertEquals("success", res);
    }

    @Test
    void testUpdateStudentInfo_Normal() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u2");
        Mockito.doNothing().when(studentInfoService).saveOrUpdateStudentInfo(entity);
        String res = studentInfoController.updateStudentInfo(entity);
        assertEquals("success", res);
    }

    @Test
    void testUpdateStudentInfo_Null() {
        Mockito.doNothing().when(studentInfoService).saveOrUpdateStudentInfo(null);
        String res = studentInfoController.updateStudentInfo(null);
        assertEquals("success", res);
    }

    // TODO: 可根据实际接口添加更多测试方法
} 