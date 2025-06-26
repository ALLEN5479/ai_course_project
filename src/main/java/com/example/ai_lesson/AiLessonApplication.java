package com.example.ai_lesson;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({
    "com.example.ai_lesson.ability.Mapper",
    "com.example.ai_lesson.knowledgeNode.Mapper",
    "com.example.ai_lesson.login.mapper",
    "com.example.ai_lesson.mission.Mapper"
})
@SpringBootApplication
public class AiLessonApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiLessonApplication.class, args);
    }

}
