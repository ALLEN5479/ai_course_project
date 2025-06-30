package com.example.ai_lesson.study_resources.controller;

import com.example.ai_lesson.study_resources.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RecordController {
    @Autowired
    private RecordMapper recordMapper;

    @RequestMapping("/updateRecord")
    int updateRecord(@RequestParam String student_id, @RequestParam int resource_id, @RequestParam double actual_time, @RequestParam int jump_time){
        return recordMapper.updateRecord(student_id, resource_id, actual_time, jump_time);
    }
}
