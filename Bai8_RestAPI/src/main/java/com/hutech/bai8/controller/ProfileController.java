package com.hutech.bai8.controller;

import com.hutech.bai8.model.User;
import com.hutech.bai8.service.CustomOAuth2User;
import com.hutech.bai8.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Xem thong tin profile
     */
    @GetMapping
    public String viewProfile(@AuthenticationPrincipal Object principal, Model model) {
        User user = getUserFromPrincipal(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("isOAuth2User", user.getProvider() != null && !"local".equals(user.getProvider()));
        return "profile/view";
    }

    /**
     * Form chinh sua thong tin
     */
    @GetMapping("/edit")
    public String editProfile(@AuthenticationPrincipal Object principal, Model model) {
        User user = getUserFromPrincipal(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("isOAuth2User", user.getProvider() != null && !"local".equals(user.getProvider()));
        return "profile/edit";
    }

    /**
     * Xu ly cap nhat thong tin
     */
    @PostMapping("/edit")
    public String updateProfile(@AuthenticationPrincipal Object principal,
                               @RequestParam String fullName,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String address,
                               RedirectAttributes redirectAttributes) {
        User user = getUserFromPrincipal(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            userService.updateProfile(user, fullName, phone, address);
            redirectAttributes.addFlashAttribute("success", "Cap nhat thong tin thanh cong!");
            log.info("User {} updated profile", user.getUsername());
        } catch (Exception e) {
            log.error("Error updating profile: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Loi khi cap nhat thong tin: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }

    /**
     * Form doi mat khau
     */
    @GetMapping("/change-password")
    public String changePasswordForm(@AuthenticationPrincipal Object principal, Model model) {
        User user = getUserFromPrincipal(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        // Neu la OAuth2 user, khong cho doi mat khau
        if (user.getProvider() != null && !"local".equals(user.getProvider())) {
            model.addAttribute("error", "Tai khoan dang nhap bang " + user.getProvider() + " khong the doi mat khau!");
            return "redirect:/profile";
        }
        
        model.addAttribute("user", user);
        return "profile/change-password";
    }

    /**
     * Xu ly doi mat khau
     */
    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal Object principal,
                                @RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        User user = getUserFromPrincipal(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        // Kiem tra OAuth2 user
        if (user.getProvider() != null && !"local".equals(user.getProvider())) {
            redirectAttributes.addFlashAttribute("error", "Tai khoan dang nhap bang " + user.getProvider() + " khong the doi mat khau!");
            return "redirect:/profile";
        }
        
        // Kiem tra mat khau hien tai
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mat khau hien tai khong dung!");
            return "redirect:/profile/change-password";
        }
        
        // Kiem tra mat khau moi va xac nhan
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mat khau moi va xac nhan khong khop!");
            return "redirect:/profile/change-password";
        }
        
        // Kiem tra do dai mat khau
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mat khau moi phai co it nhat 6 ky tu!");
            return "redirect:/profile/change-password";
        }
        
        try {
            userService.changePassword(user, newPassword);
            redirectAttributes.addFlashAttribute("success", "Doi mat khau thanh cong!");
            log.info("User {} changed password", user.getUsername());
        } catch (Exception e) {
            log.error("Error changing password: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Loi khi doi mat khau: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }

    /**
     * Helper method de lay User tu principal (ho tro ca form login va OAuth2)
     */
    private User getUserFromPrincipal(Object principal) {
        if (principal instanceof User) {
            return (User) principal;
        } else if (principal instanceof CustomOAuth2User) {
            return ((CustomOAuth2User) principal).getUser();
        }
        return null;
    }
}
