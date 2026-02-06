package com.hutech.bai4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BÀI 4: SPRING DATA MONGODB
 * 
 * Yêu cầu: MongoDB chạy tại localhost:27017
 * Chạy: mvn spring-boot:run
 * Truy cập: http://localhost:8080/books
 * 
 * Chức năng:
 * - CRUD Book với MongoDB
 * - Quản lý Category
 * - Phân trang, tìm kiếm
 */
@SpringBootApplication
public class NguyenVoChiThanh {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      BÀI 4: SPRING DATA MONGODB          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  MongoDB: localhost:27017                ║");
        System.out.println("║  Database: bookstore                     ║");
        System.out.println("║  Truy cập: http://localhost:8080/books   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        SpringApplication.run(NguyenVoChiThanh.class, args);
    }
}
