@echo off
echo 测试AI生成题目并存入数据库
echo.

echo 1. 测试健康检查端点...
curl -X GET http://localhost:8080/health
echo.
echo.

echo 2. 测试生成题目并保存到数据库...
curl -X POST http://localhost:8080/api/quiz/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"count\":2,\"difficulty\":\"简单\",\"requirement\":\"关于Java基础语法\"}"
echo.
echo.

echo 3. 查看数据库中的所有题目...
curl -X GET http://localhost:8080/api/quiz/all
echo.
echo.

echo 4. 测试生成中等难度题目...
curl -X POST http://localhost:8080/api/quiz/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"count\":1,\"difficulty\":\"中等\",\"requirement\":\"关于Java面向对象编程\"}"
echo.
echo.

echo 5. 再次查看数据库中的所有题目...
curl -X GET http://localhost:8080/api/quiz/all
echo.
echo.

echo 6. 测试根据难度查询题目...
curl -X GET http://localhost:8080/api/quiz/difficulty/简单
echo.
echo.

echo 测试完成！
pause 