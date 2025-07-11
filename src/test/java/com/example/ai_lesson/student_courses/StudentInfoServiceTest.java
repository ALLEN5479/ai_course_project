package com.example.ai_lesson.student_courses;

import com.example.ai_lesson.student_courses.service.StudentInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import com.example.ai_lesson.student_courses.entity.StudentInfoEntity;
import com.example.ai_lesson.student_courses.mapper.StudentInfoMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class StudentInfoServiceTest {
    @Autowired
    private StudentInfoService studentInfoService;

    @MockBean
    private StudentInfoMapper studentInfoMapper;

    @Test
    void contextLoads() {
        assertNotNull(studentInfoService);
    }

    @Test
    void testGetStudentInfo() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u1");
        Mockito.when(studentInfoMapper.selectByUserId("u1")).thenReturn(entity);
        StudentInfoEntity result = studentInfoService.getStudentInfo("u1");
        assertNotNull(result);
        assertEquals("u1", result.getUser_id());
    }

    @Test
    void testGetStudentInfo_Normal() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u1");
        Mockito.when(studentInfoMapper.selectByUserId("u1")).thenReturn(entity);
        StudentInfoEntity result = studentInfoService.getStudentInfo("u1");
        assertNotNull(result);
        assertEquals("u1", result.getUser_id());
    }

    @Test
    void testGetStudentInfo_NotFound() {
        Mockito.when(studentInfoMapper.selectByUserId("u2")).thenReturn(null);
        StudentInfoEntity result = studentInfoService.getStudentInfo("u2");
        assertNull(result);
    }

    @Test
    void testSaveOrUpdateStudentInfo_update() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u2");
        Mockito.when(studentInfoMapper.selectByUserId("u2")).thenReturn(entity);
        Mockito.doNothing().when(studentInfoMapper).updateStudentInfo(entity);
        studentInfoService.saveOrUpdateStudentInfo(entity);
        Mockito.verify(studentInfoMapper).updateStudentInfo(entity);
    }

    @Test
    void testSaveOrUpdateStudentInfo_insert() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u3");
        Mockito.when(studentInfoMapper.selectByUserId("u3")).thenReturn(null);
        Mockito.doNothing().when(studentInfoMapper).insertStudentInfo(entity);
        studentInfoService.saveOrUpdateStudentInfo(entity);
        Mockito.verify(studentInfoMapper).insertStudentInfo(entity);
    }

    @Test
    void testSaveOrUpdateStudentInfo_Update() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u2");
        Mockito.when(studentInfoMapper.selectByUserId("u2")).thenReturn(entity);
        Mockito.doNothing().when(studentInfoMapper).updateStudentInfo(entity);
        studentInfoService.saveOrUpdateStudentInfo(entity);
        Mockito.verify(studentInfoMapper).updateStudentInfo(entity);
    }

    @Test
    void testSaveOrUpdateStudentInfo_Insert() {
        StudentInfoEntity entity = new StudentInfoEntity();
        entity.setUser_id("u3");
        Mockito.when(studentInfoMapper.selectByUserId("u3")).thenReturn(null);
        Mockito.doNothing().when(studentInfoMapper).insertStudentInfo(entity);
        studentInfoService.saveOrUpdateStudentInfo(entity);
        Mockito.verify(studentInfoMapper).insertStudentInfo(entity);
    }

    @Test
    void testSaveOrUpdateStudentInfo_Null() {
        // 边界条件：传入null，应抛出异常
        assertThrows(IllegalArgumentException.class, () -> studentInfoService.saveOrUpdateStudentInfo(null));
    }

} 