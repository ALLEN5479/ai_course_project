package com.example.ai_lesson.student_courses;

import com.example.ai_lesson.student_courses.controller.CourseController;
import com.example.ai_lesson.student_courses.entity.CourseEntity;
import com.example.ai_lesson.student_courses.mapper.CourseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CourseControllerTest {
    @Autowired
    private CourseController courseController;

    @MockBean
    private CourseMapper courseMapper;

    @Test
    void contextLoads() {
        assertNotNull(courseController);
    }

    @Test
    void testGetCourse_Normal() {
        CourseEntity course = new CourseEntity();
        course.setCourse_id(10);
        Mockito.when(courseMapper.getCourseById(10)).thenReturn(course);
        CourseEntity result = courseController.getCourse(10);
        assertNotNull(result);
        assertEquals(10, result.getCourse_id());
    }

    @Test
    void testGetCourse_NotFound() {
        Mockito.when(courseMapper.getCourseById(11)).thenReturn(null);
        CourseEntity result = courseController.getCourse(11);
        assertNull(result);
    }

    @Test
    void testGetTeacherName_Normal() {
        Mockito.when(courseMapper.getTeacherNameById("t1")).thenReturn("张老师");
        String name = courseController.getTeacherName("t1");
        assertEquals("张老师", name);
    }

    @Test
    void testGetTeacherName_Empty() {
        Mockito.when(courseMapper.getTeacherNameById("")).thenReturn("");
        String name = courseController.getTeacherName("");
        assertEquals("", name);
    }

    @Test
    void testGetCoursesByTeacherId_Normal() {
        CourseEntity c1 = new CourseEntity();
        c1.setCourse_id(1);
        CourseEntity c2 = new CourseEntity();
        c2.setCourse_id(2);
        Mockito.when(courseMapper.getCoursesByTeacherId("t2")).thenReturn(Arrays.asList(c1, c2));
        List<CourseEntity> result = courseController.getCoursesByTeacherId("t2");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCoursesByTeacherId_Empty() {
        Mockito.when(courseMapper.getCoursesByTeacherId("t3")).thenReturn(Arrays.asList());
        List<CourseEntity> result = courseController.getCoursesByTeacherId("t3");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // TODO: 可根据实际接口添加更多测试方法
} 