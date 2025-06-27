@echo off
echo 正在编译和运行AI测试...
echo.

REM 编译测试类
javac -cp "target/classes;target/dependency/*" src/test/java/com/example/ai_lesson/AITest.java

REM 运行测试
java -cp "target/classes;target/dependency/*;src/test/java" com.example.ai_lesson.AITest

echo.
echo 测试完成！
pause 