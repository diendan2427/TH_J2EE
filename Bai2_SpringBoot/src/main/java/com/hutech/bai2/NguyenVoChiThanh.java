package com.hutech.bai2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BÀI 2: SPRING BOOT - GIAO DIỆN
 * 
 * Chạy: mvn spring-boot:run
 * Truy cập: http://localhost:8080
 * 
 * Các trang:
 * - Trang chủ: http://localhost:8080/
 * - Môn học: http://localhost:8080/subjects  
 * - Liên hệ: http://localhost:8080/contact
 */
@SpringBootApplication
public class NguyenVoChiThanh {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  BÀI 2: SPRING BOOT - GIAO DIỆN WEBSITE  ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  Đang khởi động server...                ║");
        System.out.println("║  Truy cập: http://localhost:8080         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        SpringApplication.run(NguyenVoChiThanh.class, args);
    }
}
