package com.hutech.bai1.app;

import com.hutech.bai1.model.Xe;
import com.hutech.bai1.model.XeOto;
import com.hutech.bai1.model.XeTai;
import com.hutech.bai1.service.XeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Bài 1.2.3: Chương trình kiểm định xe
 */
public class VehicleInspectionApp {
    private static XeService xeService = new XeService();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        // Thêm dữ liệu mẫu
        initSampleData();

        int choice;
        do {
            showMenu();
            choice = getIntInput("Chọn chức năng: ");
            handleChoice(choice);
        } while (choice != 0);

        System.out.println("Cảm ơn bạn đã sử dụng chương trình!");
        scanner.close();
    }

    private static void initSampleData() {
        // Thêm xe ô tô mẫu
        xeService.addXeOto(new XeOto(LocalDate.of(2020, 5, 15), "30A-12345", 5, false));
        xeService.addXeOto(new XeOto(LocalDate.of(2018, 3, 10), "51B-67890", 16, true));
        xeService.addXeOto(new XeOto(LocalDate.of(2022, 8, 20), "29C-55555", 7, false)); // Biển đẹp
        xeService.addXeOto(new XeOto(LocalDate.of(2015, 1, 5), "30D-99999", 45, true)); // Biển đẹp

        // Thêm xe tải mẫu
        xeService.addXeTai(new XeTai(LocalDate.of(2019, 6, 25), "51E-11111", 3500)); // Biển đẹp
        xeService.addXeTai(new XeTai(LocalDate.of(2021, 4, 12), "29F-24680", 8000));
        xeService.addXeTai(new XeTai(LocalDate.of(2017, 9, 30), "30G-13579", 15000));
    }

    private static void showMenu() {
        System.out.println("\n========== KIỂM ĐỊNH XE ==========");
        System.out.println("1. Thêm xe ô tô");
        System.out.println("2. Thêm xe tải");
        System.out.println("3. Hiển thị tất cả xe");
        System.out.println("4. Tìm xe ô tô có số chỗ ngồi nhiều nhất");
        System.out.println("5. Sắp xếp xe tải theo trọng tải tăng dần");
        System.out.println("6. Lọc biển số xe đẹp (4+ số giống nhau)");
        System.out.println("0. Thoát");
        System.out.println("===================================");
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1: addXeOto(); break;
            case 2: addXeTai(); break;
            case 3: xeService.showAllXe(); break;
            case 4: findXeOtoSoChoNhieuNhat(); break;
            case 5: sortXeTaiByTrongTai(); break;
            case 6: filterBienSoDep(); break;
            case 0: break;
            default: System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    private static void addXeOto() {
        System.out.println("\n--- Thêm xe ô tô ---");
        String bienSo = getStringInput("Nhập biển số (VD: 30A-12345): ");
        LocalDate ngaySx = getDateInput("Nhập ngày sản xuất (dd/MM/yyyy): ");
        int soGhe = getIntInput("Nhập số ghế: ");
        boolean kinhDoanh = getStringInput("Xe kinh doanh? (y/n): ").equalsIgnoreCase("y");
        
        xeService.addXeOto(new XeOto(ngaySx, bienSo, soGhe, kinhDoanh));
    }

    private static void addXeTai() {
        System.out.println("\n--- Thêm xe tải ---");
        String bienSo = getStringInput("Nhập biển số (VD: 51E-67890): ");
        LocalDate ngaySx = getDateInput("Nhập ngày sản xuất (dd/MM/yyyy): ");
        int trongTai = getIntInput("Nhập trọng tải (kg): ");
        
        xeService.addXeTai(new XeTai(ngaySx, bienSo, trongTai));
    }

    private static void findXeOtoSoChoNhieuNhat() {
        System.out.println("\n--- Xe ô tô có số chỗ ngồi nhiều nhất ---");
        Optional<XeOto> result = xeService.findXeOtoSoChoNhieuNhat();
        if (result.isPresent()) {
            System.out.println(result.get());
        } else {
            System.out.println("Không có xe ô tô nào!");
        }
    }

    private static void sortXeTaiByTrongTai() {
        System.out.println("\n--- Xe tải sắp xếp theo trọng tải tăng dần ---");
        List<XeTai> result = xeService.sortXeTaiByTrongTai();
        if (result.isEmpty()) {
            System.out.println("Không có xe tải nào!");
        } else {
            result.forEach(System.out::println);
        }
    }

    private static void filterBienSoDep() {
        System.out.println("\n--- Xe có biển số đẹp (4+ số giống nhau) ---");
        List<Xe> result = xeService.filterBienSoDep();
        if (result.isEmpty()) {
            System.out.println("Không có xe nào có biển số đẹp!");
        } else {
            result.forEach(System.out::println);
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Vui lòng nhập ngày theo định dạng dd/MM/yyyy!");
            }
        }
    }
}
