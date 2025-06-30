-- 插入测试数据到mission_template表
INSERT INTO mission_template (mission_name, mission_description, content, teacher_id, mission_type, status) VALUES
('Python基础练习', '学习Python基础语法', '完成Python基础语法练习，包括变量、数据类型、控制结构等', 1, 'self', 1),
('Java面向对象编程', '掌握Java面向对象编程概念', '学习Java类的定义、继承、多态等面向对象概念', 1, 'self', 1),
('数据结构与算法测试', '测试数据结构与算法基础知识', '包含数组、链表、栈、队列等基础数据结构的测试题目', 1, 'quiz', 1),
('软件工程报告', '撰写软件工程课程报告', '根据课程要求撰写软件工程相关报告，包括需求分析、设计文档等', 1, 'report', 1),
('机器学习入门', '机器学习基础知识学习', '学习机器学习的基本概念、算法原理和应用场景', 1, 'self', 1),
('数据库设计测验', '数据库设计原理测试', '测试数据库设计、SQL查询、索引优化等相关知识', 1, 'quiz', 1)
ON DUPLICATE KEY UPDATE mission_name = VALUES(mission_name); 