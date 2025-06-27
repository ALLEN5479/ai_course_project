# 数据库集成测试指南

## 测试目标
验证AI生成的题目能够成功保存到数据库中，并能够进行增删改查操作。

## 测试步骤

### 1. 启动应用
```bash
mvn spring-boot:run
```

### 2. 运行数据库测试脚本
```bash
test_database.bat
```

### 3. 运行JUnit测试
```bash
mvn test -Dtest=QuizServiceDatabaseTest
```

## 测试内容

### 1. 基础数据库连接测试
- 验证数据库连接是否正常
- 检查数据库表结构是否正确

### 2. AI生成并保存测试
- 生成题目并保存到数据库
- 验证保存的数据完整性
- 检查ID自动生成

### 3. 不同难度题目测试
- 生成简单、中等、困难三种难度的题目
- 验证每种难度都能正确保存
- 测试按难度查询功能

### 4. CRUD操作测试
- **Create**: 创建新题目
- **Read**: 根据ID查询题目
- **Update**: 更新题目内容
- **Delete**: 删除题目

## 预期结果

### 成功情况
```
=== 测试AI生成题目并保存到数据库 ===

1. 生成2道简单题目...
生成的题目 1:
  问题: 在Java中，以下哪个是基本数据类型？
  难度: 简单
  ---

2. 保存题目到数据库...
保存成功

3. 从数据库查询所有题目...
找到保存的题目: 在Java中，以下哪个是基本数据类型？
  ID: 1
  难度: 简单
  分类: 编程
  ---

=== 数据库保存测试完成 ===
```

### 失败情况
```
数据库连接失败: Communications link failure
请检查:
1. MySQL服务是否启动
2. 数据库连接配置是否正确
3. 数据库表是否已创建
```

## 数据库表结构

确保数据库中有以下表结构：

```sql
CREATE TABLE quiz_questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_text TEXT NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_answer VARCHAR(10) NOT NULL,
    category VARCHAR(100),
    difficulty VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## API端点测试

### 1. 生成并保存题目
```bash
curl -X POST http://localhost:8080/api/quiz/generate \
  -H "Content-Type: application/json" \
  -d '{
    "count": 2,
    "difficulty": "简单",
    "requirement": "关于Java基础语法"
  }'
```

### 2. 查询所有题目
```bash
curl -X GET http://localhost:8080/api/quiz/all
```

### 3. 根据难度查询
```bash
curl -X GET http://localhost:8080/api/quiz/difficulty/简单
```

### 4. 根据ID查询
```bash
curl -X GET http://localhost:8080/api/quiz/1
```

### 5. 更新题目
```bash
curl -X PUT http://localhost:8080/api/quiz/1 \
  -H "Content-Type: application/json" \
  -d '{
    "questionText": "更新后的题目",
    "optionA": "选项A",
    "optionB": "选项B",
    "optionC": "选项C",
    "optionD": "选项D",
    "correctAnswer": "A",
    "difficulty": "中等",
    "category": "编程"
  }'
```

### 6. 删除题目
```bash
curl -X DELETE http://localhost:8080/api/quiz/1
```

## 故障排除

### 1. 数据库连接问题
- 检查MySQL服务状态
- 验证数据库连接配置
- 确认数据库和表是否存在

### 2. 表结构问题
- 确保表结构与实体类匹配
- 检查字段类型和约束

### 3. MyBatis配置问题
- 检查Mapper接口和XML配置
- 验证SQL语句语法

### 4. 事务问题
- 检查事务配置
- 验证数据一致性

## 验证要点

1. **数据完整性**: 生成的题目所有字段都正确保存
2. **ID生成**: 自动生成的ID正确递增
3. **时间戳**: created_at字段正确设置
4. **查询功能**: 能够正确查询和过滤数据
5. **更新功能**: 能够正确更新题目内容
6. **删除功能**: 能够正确删除题目

## 性能测试

### 批量生成测试
```bash
# 生成10道题目
curl -X POST http://localhost:8080/api/quiz/generate \
  -H "Content-Type: application/json" \
  -d '{
    "count": 10,
    "difficulty": "中等",
    "requirement": "关于Java集合框架"
  }'
```

### 查询性能测试
```bash
# 测试查询性能
time curl -X GET http://localhost:8080/api/quiz/all
```

## 注意事项

1. **数据备份**: 测试前备份重要数据
2. **事务回滚**: 使用@Transactional注解确保测试数据不污染数据库
3. **并发测试**: 考虑多用户同时操作的场景
4. **数据验证**: 确保AI生成的数据格式正确
5. **错误处理**: 测试各种异常情况的处理 