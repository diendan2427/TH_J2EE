package com.hutech.bai5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BÀI 5: GIỎ HÀNG + VALIDATION + CHECKOUT
 * 
 * Yêu cầu: MongoDB chạy tại localhost:27017
 * Chạy: mvn spring-boot:run
 * Truy cập: http://localhost:8080/books
 * 
 * Chức năng:
 * - Thêm sách vào giỏ hàng
 * - Cập nhật số lượng, xóa khỏi giỏ
 * - Validation dữ liệu với @NotBlank, @Size, @Positive
 * - Custom Validator @ValidCategoryId
 * - Thanh toán (Checkout) và lưu Invoice
 */
@SpringBootApplication
public class NguyenVoChiThanh {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   BÀI 5: GIỎ HÀNG + VALIDATION + CHECKOUT║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  MongoDB: localhost:27017                ║");
        System.out.println("║  Database: bookstore_cart                ║");
        System.out.println("║  Truy cập: http://localhost:8080/books   ║");
        System.out.println("║  Giỏ hàng: http://localhost:8080/cart    ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        SpringApplication.run(NguyenVoChiThanh.class, args);
    }
}
