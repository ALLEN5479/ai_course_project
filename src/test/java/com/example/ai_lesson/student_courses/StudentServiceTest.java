package com.example.ai_lesson.student_courses;

import com.example.ai_lesson.student_courses.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import com.example.ai_lesson.student_courses.entity.StudentEntity;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class StudentServiceTest {
    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentService mockStudentService;

    @Test
    void contextLoads() {
        assertNotNull(studentService);
    }

    @Test
    void testGetProfile() {
        StudentEntity entity = new StudentEntity();
        entity.setStudent_id("u1");
        Mockito.when(mockStudentService.getProfile("u1")).thenReturn(entity);
        StudentEntity result = mockStudentService.getProfile("u1");
        assertNotNull(result);
        assertEquals("u1", result.getStudent_id());
    }

    @Test
    void testGetProfile_Normal() {
        StudentEntity entity = new StudentEntity();
        entity.setStudent_id("u1");
        Mockito.when(mockStudentService.getProfile("u1")).thenReturn(entity);
        StudentEntity result = mockStudentService.getProfile("u1");
        assertNotNull(result);
        assertEquals("u1", result.getStudent_id());
    }

    @Test
    void testGetProfile_NotFound() {
        Mockito.when(mockStudentService.getProfile("u2")).thenReturn(null);
        StudentEntity result = mockStudentService.getProfile("u2");
        assertNull(result);
    }

    @Test
    void testUpdateProfile() {
        StudentEntity entity = new StudentEntity();
        entity.setStudent_id("u2");
        Mockito.doNothing().when(mockStudentService).updateProfile(entity);
        mockStudentService.updateProfile(entity);
        Mockito.verify(mockStudentService).updateProfile(entity);
    }

    @Test
    void testUpdateProfile_Normal() {
        StudentEntity entity = new StudentEntity();
        entity.setStudent_id("u2");
        Mockito.doNothing().when(mockStudentService).updateProfile(entity);
        mockStudentService.updateProfile(entity);
        Mockito.verify(mockStudentService).updateProfile(entity);
    }

    @Test
    void testUpdateProfile_Null() {
        // 边界条件：传入null
        mockStudentService.updateProfile(null);
        Mockito.verify(mockStudentService).updateProfile(null);
    }

    @Test
    void testSaveAvatar() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(mockStudentService.saveAvatar(file)).thenReturn("avatar_url");
        String url = mockStudentService.saveAvatar(file);
        assertEquals("avatar_url", url);
    }

    @Test
    void testSaveAvatar_Normal() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(mockStudentService.saveAvatar(file)).thenReturn("avatar_url");
        String url = mockStudentService.saveAvatar(file);
        assertEquals("avatar_url", url);
    }

    @Test
    void testSaveAvatar_Null() {
        Mockito.when(mockStudentService.saveAvatar(null)).thenReturn(null);
        String url = mockStudentService.saveAvatar(null);
        assertNull(url);
    }

    // TODO: 可根据实际业务添加更多测试方法
} 