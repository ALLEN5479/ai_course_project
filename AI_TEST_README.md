# AI题目生成功能测试指南

## 测试方法

### 方法1：使用测试脚本（推荐）

1. **确保AI服务正在运行**
   - 检查Dify AI服务是否在 `http://localhost/v1` 运行
   - 确认API Key: `app-DmumztREf6fuaSQQ8xjHetPs`

2. **启动Spring Boot应用**
   ```bash
   mvn spring-boot:run
   ```

3. **运行API测试脚本**
   ```bash
   test_api.bat
   ```

### 方法2：使用Java测试类

1. **编译项目**
   ```bash
   mvn clean compile
   ```

2. **运行AI测试**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.ai_lesson.AITest"
   ```

### 方法3：使用JUnit测试

1. **运行单元测试**
   ```bash
   mvn test -Dtest=QuizServiceTest
   ```

2. **运行集成测试**
   ```bash
   mvn test -Dtest=QuizServiceIntegrationTest
   ```

## 测试内容

### 1. 基础连接测试
- 测试AI服务连接是否正常
- 验证API Key是否有效

### 2. 题目生成测试
- 生成简单难度题目
- 生成中等难度题目  
- 生成困难难度题目
- 生成多道题目
- 测试不同主题要求

### 3. 格式验证测试
- 验证生成的题目格式是否正确
- 检查选项A-D是否完整
- 验证正确答案格式（A/B/C/D）

## 预期结果

### 成功情况
```
=== AI题目生成测试 ===

1. 测试AI服务连接...
AI服务连接成功！
AI回复: 连接成功

2. 测试题目生成功能...

生成简单题目:
题目 1:
  难度: 简单
  问题: 在Java中，以下哪个是基本数据类型？
  A: String
  B: int
  C: Object
  D: ArrayList
  正确答案: B
  ---

=== 所有测试完成 ===
```

### 失败情况
```
AI服务连接失败: Connection refused
请检查:
1. AI服务是否正在运行
2. API URL是否正确: http://localhost/v1
3. API Key是否有效: app-DmumztREf6fuaSQQ8xjHetPs
```

## 故障排除

### 1. Maven版本问题
如果遇到Maven版本不兼容问题：
```bash
# 使用Maven wrapper
./mvnw spring-boot:run
```

### 2. Java版本问题
确保使用Java 17：
```bash
java -version
```

### 3. AI服务连接问题
- 检查Dify服务是否启动
- 验证API URL和Key
- 检查网络连接

### 4. 数据库连接问题
如果只需要测试AI功能，可以注释掉数据库相关代码

## API端点测试

### 健康检查
```bash
curl -X GET http://localhost:8080/health
```

### 生成题目
```bash
curl -X POST http://localhost:8080/api/quiz/generate \
  -H "Content-Type: application/json" \
  -d '{
    "count": 1,
    "difficulty": "简单",
    "requirement": "关于Java基础语法"
  }'
```

### 获取所有题目
```bash
curl -X GET http://localhost:8080/api/quiz/all
```

## 注意事项

1. **AI服务依赖**: 测试需要真实的AI服务连接
2. **网络连接**: 确保能够访问AI服务
3. **API限制**: 注意AI服务的调用频率限制
4. **数据格式**: 确保生成的题目格式符合预期 