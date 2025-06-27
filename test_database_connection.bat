@echo off
echo ========================================
echo 测试数据库连接
echo ========================================

echo.
echo 测试Spring Boot应用启动和数据库连接...
echo.

mvn spring-boot:run -Dspring-boot.run.arguments="--spring.main.web-application-type=none" -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=test"

echo.
echo ========================================
echo 测试完成
echo ========================================
pause 