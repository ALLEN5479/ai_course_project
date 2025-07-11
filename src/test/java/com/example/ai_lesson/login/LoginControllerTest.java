package com.example.ai_lesson.login;

import com.example.ai_lesson.login.controller.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import com.example.ai_lesson.login.entity.LoginEntity;
import com.example.ai_lesson.login.mapper.LoginMapper;
import com.example.ai_lesson.common.AjaxResult;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootTest
public class LoginControllerTest {
    @Autowired
    private LoginController loginController;

    @MockBean
    private LoginMapper loginMapper;

    @Test
    void contextLoads() {
        assertNotNull(loginController);
    }

    @Test
    void testLoginSuccess() {
        LoginEntity entity = new LoginEntity("1001", "张三", 1, "123456");
        Mockito.when(loginMapper.login("1001", "123456")).thenReturn(entity);
        AjaxResult result = loginController.login("1001", "123456");
        assertNotNull(result);
        assertEquals(200, result.get(AjaxResult.CODE_TAG));
        assertEquals(entity, result.get(AjaxResult.DATA_TAG));
    }

    @Test
    void testLoginFail() {
        Mockito.when(loginMapper.login("1002", "wrong")).thenReturn(null);
        AjaxResult result = loginController.login("1002", "wrong");
        assertNotNull(result);
        assertEquals(500, result.get(AjaxResult.CODE_TAG));
        assertEquals("用户名或密码错误", result.get(AjaxResult.MSG_TAG));
    }

    @Test
    void testRegisterSuccess() {
        LoginEntity entity = new LoginEntity("1003", "李四", 1, "abc123");
        Mockito.when(loginMapper.register(entity)).thenReturn(1);
        boolean result = loginController.register(entity);
        assertTrue(result);
    }

    @Test
    void testRegisterFail() {
        LoginEntity entity = new LoginEntity("1004", "王五", 1, "pass");
        Mockito.when(loginMapper.register(entity)).thenReturn(0);
        boolean result = loginController.register(entity);
        assertFalse(result);
    }

} 