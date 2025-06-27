@echo off
echo 测试AI题目生成API
echo.

echo 1. 测试健康检查端点...
curl -X GET http://localhost:8080/health
echo.
echo.

echo 2. 测试Quiz API测试端点...
curl -X GET http://localhost:8080/api/quiz/test
echo.
echo.

echo 3. 测试生成题目API...
curl -X POST http://localhost:8080/api/quiz/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"count\":1,\"difficulty\":\"简单\",\"requirement\":\"关于Java基础语法\"}"
echo.
echo.

echo 4. 测试生成多道题目...
curl -X POST http://localhost:8080/api/quiz/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"count\":2,\"difficulty\":\"中等\",\"requirement\":\"关于Java面向对象编程\"}"
echo.
echo.

echo 测试完成！
pause 