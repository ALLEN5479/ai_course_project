package com.example.ai_lesson.study_resources.controller;

import com.example.ai_lesson.study_resources.mapper.NodeLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class NodeLinkController {

    @Autowired
    NodeLinkMapper nodeLinkMapper;

    @RequestMapping("/getResourceId")
    public List<Integer> getResourceId(@RequestParam int nodeId) {
        return nodeLinkMapper.getResourceId(nodeId);
    }
}
