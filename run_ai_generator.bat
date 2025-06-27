@echo off
echo ========================================
echo AI题目生成器 - 选择运行模式
echo ========================================

echo.
echo 请选择运行模式:
echo 1. AIGenerator - 生成题目并显示（不保存到数据库）
echo 2. AIDatabaseSaver - 生成题目并保存到数据库
echo 3. 退出
echo.

set /p choice=请输入选择 (1-3): 

if "%choice%"=="1" (
    echo.
    echo 运行 AIGenerator...
    echo.
    mvn exec:java -Dexec.mainClass="com.example.ai_lesson.AIGenerator"
) else if "%choice%"=="2" (
    echo.
    echo 运行 AIDatabaseSaver...
    echo.
    mvn exec:java -Dexec.mainClass="com.example.ai_lesson.AIDatabaseSaver"
) else if "%choice%"=="3" (
    echo 退出程序
    exit /b 0
) else (
    echo 无效选择，请重新运行脚本
    pause
    exit /b 1
)

echo.
echo ========================================
echo 程序执行完成
echo ========================================
pause 