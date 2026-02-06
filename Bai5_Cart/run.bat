@echo off
chcp 65001 >nul
echo ╔══════════════════════════════════════════╗
echo ║   BÀI 5: GIỎ HÀNG + VALIDATION           ║
echo ╠══════════════════════════════════════════╣
echo ║  MongoDB: localhost:27017                ║
echo ║  Database: bookstore_cart                ║
echo ║  Truy cập: http://localhost:8080/books   ║
echo ║  Giỏ hàng: http://localhost:8080/cart    ║
echo ╚══════════════════════════════════════════╝
echo.
echo [YÊU CẦU] MongoDB phải đang chạy tại localhost:27017
echo.
echo Đang khởi động server...
echo.
mvn spring-boot:run
pause
