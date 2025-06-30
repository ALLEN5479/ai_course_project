package com.example.ai_lesson.study_resources.controller;

import com.example.ai_lesson.study_resources.entity.ResourceEntity;
import com.example.ai_lesson.study_resources.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ResourceController {
    @Autowired
    private ResourceMapper resourceMapper;

    @RequestMapping("/getresources")
    public List<ResourceEntity> getResources(@RequestParam int resource_id) {
        return resourceMapper.getResources(resource_id);
    }

    @RequestMapping("/getAllResources")
    public List<ResourceEntity> getAllResources() {
        return resourceMapper.getAllResources();
    }
}
