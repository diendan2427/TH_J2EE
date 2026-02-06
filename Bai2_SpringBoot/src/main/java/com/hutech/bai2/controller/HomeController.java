package com.hutech.bai2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Bài 2.5: Controller trang chủ và danh sách môn học
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Thông tin sinh viên HUTECH
        model.addAttribute("mssv", "2280602953");
        model.addAttribute("hoTen", "Nguyễn Võ Chí Thành");
        model.addAttribute("lop", "22DTHA6);
        model.addAttribute("khoa", "Công nghệ thông tin");
        model.addAttribute("email", "thanhnguyen848878@gmail.com");
        return "index";
    }

    @GetMapping("/subjects")
    public String subjects(Model model) {
        // Danh sách môn học với hình ảnh minh họa
        List<Map<String, String>> subjects = Arrays.asList(
            Map.of("maMH", "CMP3025", "tenMH", "Thực hành lập trình Java", "image", "java.png"),
            Map.of("maMH", "CMP3026", "tenMH", "Lập trình Web", "image", "web.png"),
            Map.of("maMH", "CMP3027", "tenMH", "Cơ sở dữ liệu", "image", "database.png"),
            Map.of("maMH", "CMP3028", "tenMH", "Cấu trúc dữ liệu và giải thuật", "image", "algorithm.png"),
            Map.of("maMH", "CMP3029", "tenMH", "Lập trình di động", "image", "mobile.png")
        );
        model.addAttribute("subjects", subjects);
        return "subjects";
    }
}
