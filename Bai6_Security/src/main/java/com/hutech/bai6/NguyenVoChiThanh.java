package com.hutech.bai6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BÀI 6: SPRING SECURITY - ĐĂNG KÝ & ĐĂNG NHẬP
 * 
 * Yêu cầu: MongoDB chạy tại localhost:27017
 * Chạy: mvn spring-boot:run
 * Truy cập: http://localhost:8080
 * 
 * Chức năng:
 * - Đăng ký tài khoản mới
 * - Đăng nhập với BCrypt password
 * - Bảo vệ các trang yêu cầu xác thực
 */
@SpringBootApplication
public class NguyenVoChiThanh {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   BÀI 6: SPRING SECURITY - AUTH          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  MongoDB: localhost:27017                ║");
        System.out.println("║  Database: bookstore_security            ║");
        System.out.println("║                                          ║");
        System.out.println("║  Đăng nhập: http://localhost:8080/login  ║");
        System.out.println("║  Đăng ký:   http://localhost:8080/register║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        SpringApplication.run(NguyenVoChiThanh.class, args);
    }
}
