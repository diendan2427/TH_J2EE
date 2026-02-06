package com.hutech.bai8.controller;

import com.hutech.bai8.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                           @RequestParam(required = false) String logout,
                           @RequestParam(required = false) String registered,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "Ten dang nhap hoac mat khau khong dung!");
        }
        if (logout != null) {
            model.addAttribute("message", "Dang xuat thanh cong!");
        }
        if (registered != null) {
            model.addAttribute("message", "Dang ky thanh cong! Vui long dang nhap.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          @RequestParam String email,
                          @RequestParam String fullName,
                          @RequestParam(required = false) String phone,
                          @RequestParam(required = false) String address,
                          Model model) {
        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Mat khau xac nhan khong khop!");
                return "auth/register";
            }
            if (password.length() < 6) {
                model.addAttribute("error", "Mat khau phai co it nhat 6 ky tu!");
                return "auth/register";
            }
            userService.register(username, password, email, fullName, phone, address);
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
