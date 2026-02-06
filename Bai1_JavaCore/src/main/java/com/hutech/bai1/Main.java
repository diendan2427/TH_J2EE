package com.hutech.bai1;

import com.hutech.bai1.app.BookManagementApp;
import com.hutech.bai1.app.VehicleInspectionApp;

import java.util.Scanner;

/**
 * BÀI 1: LẬP TRÌNH JAVA CORE
 * Main class để chọn chạy các bài tập
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       BÀI 1: LẬP TRÌNH JAVA CORE         ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Bài 1.2.1 - Hello World              ║");
        System.out.println("║  2. Bài 1.2.2 - Quản lý sách             ║");
        System.out.println("║  3. Bài 1.2.3 - Kiểm định xe             ║");
        System.out.println("║  0. Thoát                                ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        System.out.print("Chọn bài tập: ");
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                System.out.println("\n=== BÀI 1.2.1: HELLO WORLD ===");
                System.out.println("Hello and welcome!");
                break;
            case 2:
                System.out.println("\n=== BÀI 1.2.2: QUẢN LÝ SÁCH ===");
                BookManagementApp.main(args);
                break;
            case 3:
                System.out.println("\n=== BÀI 1.2.3: KIỂM ĐỊNH XE ===");
                VehicleInspectionApp.main(args);
                break;
            case 0:
                System.out.println("Tạm biệt!");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }
}
