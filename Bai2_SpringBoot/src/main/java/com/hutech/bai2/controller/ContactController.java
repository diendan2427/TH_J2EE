package com.hutech.bai2.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Bài 2.5: Controller trang liên hệ
 */
@Controller
public class ContactController {

    @GetMapping("/contact")
    public String showContactForm() {
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(
            @RequestParam String hoTen,
            @RequestParam String email,
            @RequestParam String tinNhan,
            Model model) {
        
        model.addAttribute("hoTen", hoTen);
        model.addAttribute("email", email);
        model.addAttribute("tinNhan", tinNhan);
        model.addAttribute("success", true);
        
        return "contact-result";
    }
}
