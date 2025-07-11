# AI课程管理系统 (AI Lesson Management System)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.5.13-4FC08D.svg)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-2.9.2-409EFF.svg)](https://element-plus.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📖 项目简介

AI课程管理系统是一个基于Spring Boot + Vue.js的全栈Web应用，旨在为教育机构提供智能化的课程管理、学生管理、任务分配和学习分析功能。系统集成了AI技术，支持智能问答、知识图谱构建、能力评估等先进功能。

## ✨ 主要功能

### 🎓 课程管理
- **课程信息管理**: 完整的课程CRUD操作
- **课程资源上传**: 支持多种格式的教学资源
- **课程浏览与搜索**: 便捷的课程查找功能
- **课程详情展示**: 丰富的课程信息展示

### 👨‍🎓 学生管理
- **学生信息管理**: 学生档案的完整管理
- **学生课程关联**: 学生与课程的关联管理
- **学习进度跟踪**: 实时监控学生学习状态
- **个人资料管理**: 学生个人信息维护

### 📋 任务管理
- **任务库管理**: 丰富的任务模板库
- **任务发布**: 教师可发布各类学习任务
- **任务提交**: 学生在线提交作业
- **任务评分**: 教师在线批改和评分
- **任务统计**: 详细的任务完成情况统计

### 🤖 AI智能功能
- **智能问答**: 基于AI的智能答疑系统
- **知识图谱**: 可视化知识结构展示
- **能力评估**: 学生能力雷达图分析
- **学习报告**: AI生成的学习分析报告

### 📊 学习资源
- **资源管理**: 教学资源的上传和管理
- **资源分类**: 按类型和主题分类管理
- **学习记录**: 学生学习行为记录
- **资源推荐**: 个性化学习资源推荐

### 📈 数据分析
- **学习统计**: 详细的学习数据统计
- **能力分析**: 学生能力多维度分析
- **进度监控**: 学习进度实时监控
- **报告生成**: 自动生成学习报告

## 🛠 技术栈

### 后端技术
- **Java 17**: 核心编程语言
- **Spring Boot 3.4.6**: 主框架
- **MyBatis**: ORM框架
- **MySQL**: 数据库
- **Maven**: 项目管理工具

### 前端技术
- **Vue.js 3.5.13**: 前端框架
- **TypeScript**: 类型安全的JavaScript
- **Element Plus 2.9.2**: UI组件库
- **Vue Router**: 路由管理
- **Pinia**: 状态管理
- **ECharts**: 数据可视化
- **Vite**: 构建工具

### AI集成
- **Dify Java Client**: AI服务集成
- **Apache Tika**: 文档解析
- **Apache POI**: Office文档处理
- **PDFBox**: PDF文档处理

## 📦 环境要求

- **JDK**: 17+
- **Node.js**: 18+
- **MySQL**: 8.0+
- **Maven**: 3.6+
- **npm**: 8+

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/your-username/ai-course-project.git
cd ai-course-project
```

### 2. 后端部署

#### 2.1 数据库配置
```sql
-- 创建数据库
CREATE DATABASE ai_lesson_project CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入数据库脚本
-- 请参考 src/main/resources/sql/ 目录下的SQL文件
```

#### 2.2 配置文件修改
编辑 `src/main/resources/application.properties`:

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://202.199.6.19:3306/ai_lesson_project?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=123456
```

#### 2.3 启动后端服务
```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 3. 前端部署

#### 3.1 安装依赖
```bash
cd ailesson_ui
npm install
```

#### 3.2 配置API地址
编辑 `src/utils/request.ts` 中的API基础URL。

#### 3.3 启动开发服务器
```bash
npm run dev
```

#### 3.4 构建生产版本
```bash
npm run build
```

## 📁 项目结构

```
ai_course_project/
├── src/main/java/com/example/ai_lesson/
│   ├── ability/                 # 能力评估模块
│   ├── aiquestion/             # AI问答模块
│   ├── knowledgeNode/          # 知识图谱模块
│   ├── login/                  # 登录认证模块
│   ├── mission/                # 任务管理模块
│   ├── student_courses/        # 学生课程模块
│   ├── study_resources/        # 学习资源模块
│   └── common/                 # 公共组件
├── ailesson_ui/                # Vue.js前端项目
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── views/             # 页面组件
│   │   ├── router/            # 路由配置
│   │   └── utils/             # 工具函数
│   └── public/                # 静态资源
├── src/main/resources/        # 配置文件
└── pom.xml                    # Maven配置
```

## 🔧 配置说明

### 数据库配置
- 支持MySQL 8.0+
- 字符集：utf8mb4
- 时区：GMT+8

### 文件上传配置
- 支持多种文档格式：PDF、Word、PPT、Excel
- 文件大小限制：可配置
- 存储路径：可自定义

### AI服务配置
- 支持Dify AI平台集成
- 可配置API密钥和端点
- 支持多种AI模型

## 📖 使用指南

### 教师端功能
1. **登录系统**: 使用教师账号登录
2. **课程管理**: 创建和管理课程信息
3. **任务发布**: 发布学习任务和作业
4. **学生管理**: 查看和管理学生信息
5. **评分批改**: 在线批改学生作业
6. **数据分析**: 查看学习统计和报告

### 学生端功能
1. **登录系统**: 使用学生账号登录
2. **课程学习**: 浏览和参与课程
3. **任务完成**: 查看和提交作业
4. **学习资源**: 访问学习材料
5. **能力评估**: 查看个人能力分析
6. **学习报告**: 查看学习进度报告

## 🧪 测试

### 后端测试
```bash
# 运行单元测试
mvn test

# 生成测试覆盖率报告
mvn jacoco:report
```

### 前端测试
```bash
cd ailesson_ui
npm run test
```

## 📊 性能优化

- **数据库优化**: 使用索引优化查询性能
- **缓存策略**: 实现多级缓存机制
- **前端优化**: 代码分割和懒加载
- **静态资源**: CDN加速和压缩

## 🔒 安全特性

- **身份认证**: JWT token认证
- **权限控制**: 基于角色的访问控制
- **数据加密**: 敏感数据加密存储
- **SQL注入防护**: 参数化查询
- **XSS防护**: 输入验证和输出编码

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📝 更新日志

### v1.0.0 (2025-7-11)
- ✨ 初始版本发布
- 🎯 基础功能实现
- 🤖 AI功能集成
- 📊 数据可视化

## 👥 团队

- **开发团队**: AI课程项目组
- **技术支持**: 如有问题请联系技术支持

## 📞 联系方式

- **项目地址**: [GitHub Repository](https://github.com/ALLEN5479/ai_course_project.git)
- **邮箱**: 811827671@qq.com

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户！

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！ 