package com.example.ai_lesson.aiquestion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 配置测试类
 * 验证测试环境配置是否正确
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("配置测试")
class ConfigurationTest {

    @Autowired
    private DataSource dataSource;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${custom.ai.api.url}")
    private String aiApiUrl;

    @Value("${custom.ai.api.key}")
    private String aiApiKey;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriver;



    @Test
    @DisplayName("测试AI服务配置")
    void testAiServiceConfiguration() {
        assertEquals("http://localhost/v1", aiApiUrl);
        assertEquals("app-oQmb8SeVQN5bS4SA3FmaFNeI", aiApiKey);
    }

    @Test
    @DisplayName("测试数据库配置")
    void testDatabaseConfiguration() {
        assertNotNull(dataSource);
        assertEquals("com.mysql.cj.jdbc.Driver", datasourceDriver);
        assertTrue(datasourceUrl.contains("202.199.6.19:3306/ai_lesson_project"));
    }

    @Test
    @DisplayName("测试数据源连接")
    void testDataSourceConnection() {
        assertDoesNotThrow(() -> {
            try (var connection = dataSource.getConnection()) {
                assertTrue(connection.isValid(5));
                System.out.println("数据库连接成功: " + connection.getMetaData().getURL());
            }
        });
    }


} 