package com.example.ai_lesson.login.controller;

import com.example.ai_lesson.login.entity.LoginEntity;
import com.example.ai_lesson.login.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private LoginMapper loginMapper;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam int user_id,
            @RequestParam String password) {
        try {
            // 参数验证
            if (password == null || password.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密码不能为空");
            }
            
            LoginEntity result = loginMapper.login(user_id, password);
            Map<String, Object> response = new HashMap<>();
            
            if (result != null) {
                response.put("success", true);
                response.put("message", "登录成功");
                response.put("data", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "用户名或密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "登录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody LoginEntity loginEntity) {
        try {
            // 参数验证
            if (loginEntity == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "注册信息不能为空");
            }
            
            if (loginEntity.getPassword() == null || loginEntity.getPassword().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密码不能为空");
            }
            
            boolean result = loginMapper.register(loginEntity) == 1;
            Map<String, Object> response = new HashMap<>();
            
            if (result) {
                response.put("success", true);
                response.put("message", "注册成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "注册失败");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "注册失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
