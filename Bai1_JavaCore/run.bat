@echo off
chcp 65001 >nul
echo ╔══════════════════════════════════════════╗
echo ║       BÀI 1: LẬP TRÌNH JAVA CORE         ║
echo ╚══════════════════════════════════════════╝
echo.
echo Đang biên dịch và chạy...
echo.
mvn compile exec:java -Dexec.mainClass="com.hutech.bai1.Main" -q
pause
