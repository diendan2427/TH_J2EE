package com.hutech.bai8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BÀI 8: RESTFUL API + AJAX
 * 
 * Yêu cầu: MongoDB chạy tại localhost:27017
 * Chạy: mvn spring-boot:run
 * Truy cập: http://localhost:8080/books
 * 
 * API Endpoints:
 * - GET    /api/books         - Lấy tất cả sách (JSON)
 * - GET    /api/books/id/{id} - Lấy sách theo ID
 * - POST   /api/books         - Thêm sách mới
 * - PUT    /api/books/{id}    - Cập nhật sách
 * - DELETE /api/books/{id}    - Xóa sách
 * 
 * Demo AJAX: Xóa sách không cần reload trang
 */
@SpringBootApplication
public class NguyenVoChiThanh {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       BÀI 8: RESTFUL API + AJAX          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  MongoDB: localhost:27017                ║");
        System.out.println("║  Database: bookstore_api                 ║");
        System.out.println("║                                          ║");
        System.out.println("║  Web UI:  http://localhost:8080/books    ║");
        System.out.println("║                                          ║");
        System.out.println("║  API Endpoints:                          ║");
        System.out.println("║  GET    /api/books                       ║");
        System.out.println("║  GET    /api/books/id/{id}               ║");
        System.out.println("║  POST   /api/books                       ║");
        System.out.println("║  PUT    /api/books/{id}                  ║");
        System.out.println("║  DELETE /api/books/{id}                  ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        SpringApplication.run(NguyenVoChiThanh.class, args);
    }
}
