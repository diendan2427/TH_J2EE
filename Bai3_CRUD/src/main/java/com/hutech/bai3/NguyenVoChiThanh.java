package com.hutech.bai3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BÀI 3: CRUD BOOK VỚI THYMELEAF
 * 
 * Chạy: mvn spring-boot:run
 * Truy cập: http://localhost:8080/books
 * 
 * Chức năng:
 * - Xem danh sách sách
 * - Thêm sách mới
 * - Sửa thông tin sách
 * - Xóa sách
 */
@SpringBootApplication
public class NguyenVoChiThanh {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║     BÀI 3: CRUD BOOK - THYMELEAF         ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Đang khởi động server...                ║");
        System.out.println("║  Truy cập: http://localhost:8080/books   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        SpringApplication.run(NguyenVoChiThanh.class, args);
    }
}
