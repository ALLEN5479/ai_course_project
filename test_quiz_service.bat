@echo off
echo ========================================
echo 测试QuizService功能
echo ========================================

echo.
echo 1. 测试生成问题API
echo 发送POST请求到 /api/quiz/generate
echo.

curl -X POST http://localhost:8080/api/quiz/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"count\": 2, \"difficulty\": \"简单\", \"requirement\": \"关于Java基础\"}" ^
  -w "\nHTTP状态码: %%{http_code}\n"

echo.
echo 2. 测试获取所有问题API
echo 发送GET请求到 /api/quiz/all
echo.

curl -X GET http://localhost:8080/api/quiz/all ^
  -w "\nHTTP状态码: %%{http_code}\n"

echo.
echo 3. 测试根据难度获取问题API
echo 发送GET请求到 /api/quiz/difficulty/简单
echo.

curl -X GET http://localhost:8080/api/quiz/difficulty/简单 ^
  -w "\nHTTP状态码: %%{http_code}\n"

echo.
echo ========================================
echo 测试完成
echo ========================================
pause 