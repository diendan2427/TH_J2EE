package com.hutech.bai7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BÀI 7: PHÂN QUYỀN ROLE-BASED (ADMIN/USER)
 * 
 * Yêu cầu: MongoDB chạy tại localhost:27017
 * Chạy: mvn spring-boot:run
 * Truy cập: http://localhost:8080
 * 
 * Tài khoản test:
 * - ADMIN: admin / admin123 (Toàn quyền CRUD sách)
 * - USER:  user / user123   (Chỉ xem + thêm giỏ hàng)
 * 
 * Chức năng:
 * - Phân quyền với @PreAuthorize
 * - Ẩn/hiện nút theo role với sec:authorize
 * - Trang lỗi 403, 404 tùy chỉnh
 */
@SpringBootApplication
public class NguyenVoChiThanh {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   BÀI 7: PHÂN QUYỀN ROLE-BASED           ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  MongoDB: localhost:27017                ║");
        System.out.println("║  Database: bookstore_rolebased           ║");
        System.out.println("║                                          ║");
        System.out.println("║  Tài khoản test:                         ║");
        System.out.println("║  - ADMIN: admin / admin123               ║");
        System.out.println("║  - USER:  user / user123                 ║");
        System.out.println("║                                          ║");
        System.out.println("║  Truy cập: http://localhost:8080         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        SpringApplication.run(NguyenVoChiThanh.class, args);
    }
}
