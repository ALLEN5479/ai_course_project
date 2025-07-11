package com.example.ai_lesson.study_resources;

import com.example.ai_lesson.study_resources.controller.ResourceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import com.example.ai_lesson.study_resources.entity.ResourceEntity;
import com.example.ai_lesson.study_resources.mapper.ResourceMapper;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ResourceControllerTest {
    @Autowired
    private ResourceController resourceController;

    @MockBean
    private ResourceMapper resourceMapper;

    @Test
    void contextLoads() {
        assertNotNull(resourceController);
    }

    @Test
    void testGetResources() {
        ResourceEntity entity = new ResourceEntity(1, "资源A", "http://url", "描述A", "类型A");
        Mockito.when(resourceMapper.getResources(1)).thenReturn(Arrays.asList(entity));
        List<ResourceEntity> result = resourceController.getResources(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("资源A", result.get(0).getResource_name());
    }

    @Test
    void testGetAllResources() {
        ResourceEntity entity1 = new ResourceEntity(1, "资源A", "http://urlA", "描述A", "类型A");
        ResourceEntity entity2 = new ResourceEntity(2, "资源B", "http://urlB", "描述B", "类型B");
        Mockito.when(resourceMapper.getAllResources()).thenReturn(Arrays.asList(entity1, entity2));
        List<ResourceEntity> result = resourceController.getAllResources();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("资源A", result.get(0).getResource_name());
        assertEquals("资源B", result.get(1).getResource_name());
    }

} 