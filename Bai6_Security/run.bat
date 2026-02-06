@echo off
chcp 65001 >nul
echo ╔══════════════════════════════════════════╗
echo ║   BÀI 6: SPRING SECURITY - AUTH          ║
echo ╠══════════════════════════════════════════╣
echo ║  MongoDB: localhost:27017                ║
echo ║  Database: bookstore_security            ║
echo ║                                          ║
echo ║  Đăng nhập: http://localhost:8080/login  ║
echo ║  Đăng ký:   http://localhost:8080/register
echo ╚══════════════════════════════════════════╝
echo.
echo [YÊU CẦU] MongoDB phải đang chạy tại localhost:27017
echo.
echo Đang khởi động server...
echo.
mvn spring-boot:run
pause
