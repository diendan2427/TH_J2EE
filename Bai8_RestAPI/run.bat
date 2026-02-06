@echo off
chcp 65001 >nul
echo ╔══════════════════════════════════════════╗
echo ║       BÀI 8: RESTFUL API + AJAX          ║
echo ╠══════════════════════════════════════════╣
echo ║  MongoDB: localhost:27017                ║
echo ║  Database: bookstore_api                 ║
echo ║                                          ║
echo ║  Web UI:  http://localhost:8080/books    ║
echo ║                                          ║
echo ║  API Endpoints:                          ║
echo ║  GET    /api/books                       ║
echo ║  GET    /api/books/id/{id}               ║
echo ║  POST   /api/books                       ║
echo ║  PUT    /api/books/{id}                  ║
echo ║  DELETE /api/books/{id}                  ║
echo ╚══════════════════════════════════════════╝
echo.
echo [YÊU CẦU] MongoDB phải đang chạy tại localhost:27017
echo.
echo Đang khởi động server...
echo.
mvn spring-boot:run
pause
