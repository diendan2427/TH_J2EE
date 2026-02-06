@echo off
chcp 65001 >nul
echo ╔══════════════════════════════════════════╗
echo ║   BÀI 7: PHÂN QUYỀN ROLE-BASED           ║
echo ╠══════════════════════════════════════════╣
echo ║  MongoDB: localhost:27017                ║
echo ║  Database: bookstore_rolebased           ║
echo ║                                          ║
echo ║  Tài khoản test:                         ║
echo ║  - ADMIN: admin / admin123               ║
echo ║  - USER:  user / user123                 ║
echo ║                                          ║
echo ║  Truy cập: http://localhost:8080         ║
echo ╚══════════════════════════════════════════╝
echo.
echo [YÊU CẦU] MongoDB phải đang chạy tại localhost:27017
echo.
echo Đang khởi động server...
echo.
mvn spring-boot:run
pause
