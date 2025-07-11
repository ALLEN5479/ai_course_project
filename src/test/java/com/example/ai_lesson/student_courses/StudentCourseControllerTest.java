package com.example.ai_lesson.student_courses;

import com.example.ai_lesson.student_courses.controller.StudentCourseController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import com.example.ai_lesson.student_courses.entity.StudentCourseEntity;
import com.example.ai_lesson.student_courses.entity.CourseEntity;
import com.example.ai_lesson.student_courses.mapper.StudentCourseMapper;
import com.example.ai_lesson.student_courses.mapper.CourseMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class StudentCourseControllerTest {
    @Autowired
    private StudentCourseController studentCourseController;

    @MockBean
    private StudentCourseMapper studentCourseMapper;
    @MockBean
    private CourseMapper courseMapper;

    @Test
    void contextLoads() {
        assertNotNull(studentCourseController);
    }

    @Test
    void testGetStudentCourses_Normal() {
        StudentCourseEntity sc = new StudentCourseEntity();
        sc.setCourse_id(1);
        Mockito.when(studentCourseMapper.getStudentCoursesByUserId("u1")).thenReturn(Arrays.asList(sc));
        CourseEntity course = new CourseEntity();
        course.setCourse_id(1);
        Mockito.when(courseMapper.getCoursesByIds(Arrays.asList(1))).thenReturn(Arrays.asList(course));
        List<CourseEntity> result = studentCourseController.getStudentCourses("u1");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCourse_id());
    }

    @Test
    void testGetStudentCourses_Empty() {
        Mockito.when(studentCourseMapper.getStudentCoursesByUserId("u2")).thenReturn(Collections.emptyList());
        List<CourseEntity> result = studentCourseController.getStudentCourses("u2");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetStudentCourseDetail_Normal() {
        StudentCourseEntity sc = new StudentCourseEntity();
        sc.setCourse_id(2);
        Mockito.when(studentCourseMapper.getStudentCoursesByUserId("u4")).thenReturn(Arrays.asList(sc));
        CourseEntity course = new CourseEntity();
        course.setCourse_id(2);
        Mockito.when(courseMapper.getCourseById(2)).thenReturn(course);
        CourseEntity result = studentCourseController.getStudentCourseDetail(2, "u4");
        assertNotNull(result);
        assertEquals(2, result.getCourse_id());
    }

    @Test
    void testGetStudentCourseDetail_NoAccess() {
        Mockito.when(studentCourseMapper.getStudentCoursesByUserId("u5")).thenReturn(Collections.emptyList());
        Exception ex = assertThrows(RuntimeException.class, () -> studentCourseController.getStudentCourseDetail(3, "u5"));
        assertEquals("学生无权访问该课程", ex.getMessage());
    }

    @Test
    void testGetStudentCourseDetail_NullList() {
        Mockito.when(studentCourseMapper.getStudentCoursesByUserId("u6")).thenReturn(null);
        Exception ex = assertThrows(RuntimeException.class, () -> studentCourseController.getStudentCourseDetail(1, "20237065"));
        assertEquals("学生无权访问该课程", ex.getMessage());
    }

    @Test
    void testGetCourseStudentCount_Normal() {
        Mockito.when(studentCourseMapper.getCourseStudentCount("t1")).thenReturn(5);
        int count = studentCourseController.getCourseStudentCount("t1");
        assertEquals(5, count);
    }

    @Test
    void testGetCourseStudentCount_Zero() {
        Mockito.when(studentCourseMapper.getCourseStudentCount("t2")).thenReturn(0);
        int count = studentCourseController.getCourseStudentCount("t2");
        assertEquals(0, count);
    }

    // TODO: 可根据实际接口添加更多测试方法
} 