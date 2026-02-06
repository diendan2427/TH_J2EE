package com.hutech.bai1.service;

import com.hutech.bai1.model.Xe;
import com.hutech.bai1.model.XeOto;
import com.hutech.bai1.model.XeTai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Bài 1.2.3: Service quản lý xe với Stream API và Regular Expression
 */
public class XeService {
    private List<Xe> danhSachXe = new ArrayList<>();

    // Thêm xe ô tô
    public void addXeOto(XeOto xeOto) {
        danhSachXe.add(xeOto);
        System.out.println("Đã thêm: " + xeOto);
    }

    // Thêm xe tải
    public void addXeTai(XeTai xeTai) {
        danhSachXe.add(xeTai);
        System.out.println("Đã thêm: " + xeTai);
    }

    // Lấy danh sách xe ô tô
    public List<XeOto> getDanhSachXeOto() {
        return danhSachXe.stream()
                .filter(xe -> xe instanceof XeOto)
                .map(xe -> (XeOto) xe)
                .collect(Collectors.toList());
    }

    // Lấy danh sách xe tải
    public List<XeTai> getDanhSachXeTai() {
        return danhSachXe.stream()
                .filter(xe -> xe instanceof XeTai)
                .map(xe -> (XeTai) xe)
                .collect(Collectors.toList());
    }

    // Tìm xe ô tô có số chỗ ngồi nhiều nhất
    public Optional<XeOto> findXeOtoSoChoNhieuNhat() {
        return getDanhSachXeOto().stream()
                .max(Comparator.comparingInt(XeOto::getSoGhe));
    }

    // Sắp xếp xe tải theo trọng tải tăng dần
    public List<XeTai> sortXeTaiByTrongTai() {
        return getDanhSachXeTai().stream()
                .sorted(Comparator.comparingInt(XeTai::getTrongTai))
                .collect(Collectors.toList());
    }

    /**
     * Lọc biển số xe "đẹp": 5 số cuối có ít nhất 4 số giống nhau
     * Sử dụng Regular Expression
     * Ví dụ: 30A-12222, 51B-99999, 29C-55551
     */
    public List<Xe> filterBienSoDep() {
        // Pattern: 5 số cuối có ít nhất 4 số giống nhau
        // Giải thích: 
        // - (\\d)\\1{3,}: một số lặp lại ít nhất 4 lần liên tiếp
        // - \\d?(\\d)\\1{3}: 1 số bất kỳ + 4 số giống nhau
        // - (\\d)\\1{2}\\d\\1: 3 số + 1 số khác + 1 số giống 3 số đầu
        
        String regex = ".*((\\d)\\2{3,}|\\d(\\d)\\3{3}|(\\d)\\4{2}\\d\\4)$";
        Pattern pattern = Pattern.compile(regex);
        
        return danhSachXe.stream()
                .filter(xe -> {
                    String bienSo = xe.getBienSo().replaceAll("[^0-9]", "");
                    if (bienSo.length() < 5) return false;
                    String last5 = bienSo.substring(bienSo.length() - 5);
                    return hasAtLeast4SameDigits(last5);
                })
                .collect(Collectors.toList());
    }

    // Kiểm tra 5 số có ít nhất 4 số giống nhau
    private boolean hasAtLeast4SameDigits(String digits) {
        if (digits.length() != 5) return false;
        
        int[] count = new int[10];
        for (char c : digits.toCharArray()) {
            count[c - '0']++;
        }
        
        for (int c : count) {
            if (c >= 4) return true;
        }
        return false;
    }

    // Hiển thị tất cả xe
    public void showAllXe() {
        System.out.println("\n=== DANH SÁCH XE ===");
        if (danhSachXe.isEmpty()) {
            System.out.println("Danh sách trống!");
            return;
        }
        danhSachXe.forEach(System.out::println);
    }
}
