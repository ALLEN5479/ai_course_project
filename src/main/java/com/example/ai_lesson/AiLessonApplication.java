package com.example.ai_lesson;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({
    "com.example.ai_lesson.ability.Mapper",
    "com.example.ai_lesson.knowledgeNode.Mapper",
    "com.example.ai_lesson.login.mapper",
    "com.example.ai_lesson.mission.Mapper",
    "com.example.ai_lesson.student_courses.mapper",
    "com.example.ai_lesson.aiquestion.mapper",
    "com.example.ai_lesson.study_resources.mapper"
})
@SpringBootApplication
public class AiLessonApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiLessonApplication.class, args);
    }

}
