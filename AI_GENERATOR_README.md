# AI题目生成器使用指南

## 🚀 功能概述

本项目提供了两种AI题目生成模式：

1. **AIGenerator** - 生成题目并显示（不保存到数据库）
2. **AIDatabaseSaver** - 生成题目并保存到数据库

## 📋 前置要求

### 1. 环境要求
- Java 17+
- Maven 3.6.3+
- MySQL 数据库
- AI服务（Dify）

### 2. 数据库配置
确保MySQL数据库已启动，并创建了 `questions` 表：

```sql
create table questions (
    id int auto_increment primary key,
    question_text varchar(1000) not null,
    option_a varchar(1000) not null,
    option_b varchar(1000) not null,
    option_c varchar(1000) not null,
    option_d varchar(1000) not null,
    correct_answer varchar(1) not null,
    category varchar(100) null,
    difficulty varchar(20) null,
    created_at timestamp default CURRENT_TIMESTAMP null
);
```

### 3. 配置文件
确保 `application.properties` 中的数据库配置正确：

```properties
spring.datasource.url=jdbc:mysql://202.199.6.19:3306/ai_lesson_project?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=123456
custom.ai.api.url=http://localhost/v1
custom.ai.api.key=app-DmumztREf6fuaSQQ8xjHetPs
```

## 🎯 使用方法

### 方法1：使用批处理脚本（推荐）

运行 `run_ai_generator.bat` 脚本：

```bash
run_ai_generator.bat
```

然后选择运行模式：
- 选择 `1` - 运行AIGenerator（仅生成题目）
- 选择 `2` - 运行AIDatabaseSaver（生成并保存到数据库）
- 选择 `3` - 退出

### 方法2：直接使用Maven命令

#### 仅生成题目（不保存到数据库）：
```bash
mvn exec:java -Dexec.mainClass="com.example.ai_lesson.AIGenerator"
```

#### 生成题目并保存到数据库：
```bash
mvn exec:java -Dexec.mainClass="com.example.ai_lesson.AIDatabaseSaver"
```

### 方法3：测试数据库连接
```bash
test_database_connection.bat
```

## 📊 功能说明

### AIGenerator 功能
- ✅ 测试AI服务连接
- ✅ 生成不同难度的题目
- ✅ 显示生成的题目内容
- ❌ 不保存到数据库

### AIDatabaseSaver 功能
- ✅ 测试AI服务连接
- ✅ 生成不同难度的题目
- ✅ 自动保存到数据库
- ✅ 验证保存结果
- ✅ 显示保存的题目详情

## 🔧 生成的题目类型

程序会自动生成以下类型的题目：

1. **简单难度** - Java基础语法
2. **中等难度** - Java面向对象编程
3. **困难难度** - Java多线程编程

每种难度会生成2-3道题目。

## 🐛 故障排除

### 1. AI服务连接失败
**错误信息**: `AI服务连接失败`
**解决方案**:
- 检查AI服务是否正在运行
- 验证API URL: `http://localhost/v1`
- 确认API Key: `app-DmumztREf6fuaSQQ8xjHetPs`

### 2. 数据库连接失败
**错误信息**: `数据库连接失败`
**解决方案**:
- 检查MySQL服务是否启动
- 验证数据库连接配置
- 确认数据库表是否存在

### 3. Maven构建失败
**错误信息**: `Maven构建失败`
**解决方案**:
- 检查Java版本是否为17+
- 检查Maven版本是否为3.6.3+
- 运行 `mvn clean install` 重新构建

### 4. 端口冲突
**错误信息**: `端口8080已被占用`
**解决方案**:
- 修改 `application.properties` 中的端口号
- 或者关闭占用端口的其他应用

## 📝 示例输出

### AIGenerator 输出示例：
```
=== AI题目生成器 ===
1. 测试AI服务连接...
✓ AI服务连接成功！
AI回复: AI服务连接正常

2. 生成简单难度题目...
生成并保存 3 道简单难度题目:
==================================================
题目 1 (ID: 1):
难度: 简单
问题: 在Java中，以下哪个是基本数据类型？
A: String
B: int
C: Object
D: ArrayList
正确答案: B
分类: 编程
创建时间: 2024-01-15 10:30:00
--------------------------------------------------
```

### AIDatabaseSaver 输出示例：
```
=== AI题目生成器 - 数据库保存版 ===
1. 测试AI服务连接...
✓ AI服务连接成功！

2. 生成题目并保存到数据库...
生成简单难度题目...
✓ 保存了 2 道简单题目
生成中等难度题目...
✓ 保存了 2 道中等题目
生成困难难度题目...
✓ 保存了 1 道困难题目

3. 验证数据库保存结果...
数据库中总共有 5 道题目

=== 题目生成和保存完成 ===
总共生成了 5 道题目
所有题目已成功保存到数据库！
```

## 🔍 验证结果

生成题目后，可以通过以下方式验证：

1. **查看数据库**:
```sql
SELECT * FROM questions ORDER BY created_at DESC;
```

2. **使用API查询**:
```bash
curl -X GET http://localhost:8080/api/quiz/all
```

3. **按难度查询**:
```bash
curl -X GET http://localhost:8080/api/quiz/difficulty/简单
```

## 📞 技术支持

如果遇到问题，请检查：
1. 所有前置要求是否满足
2. 错误日志信息
3. 数据库连接状态
4. AI服务状态 