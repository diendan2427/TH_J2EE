package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.User;
import com.hutech.bai8.model.ViewingHistory;
import com.hutech.bai8.service.ViewingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
public class ViewingHistoryController {
    private final ViewingHistoryService viewingHistoryService;
    private final Cart cart;

    // Xem lịch sử đã xem
    @GetMapping
    public String viewHistory(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }
        
        List<ViewingHistory> history = viewingHistoryService.getUserViewingHistory(user);
        model.addAttribute("history", history);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "history/view";
    }

    // Xóa một bản ghi lịch sử
    @GetMapping("/delete/{id}")
    public String deleteHistoryItem(@PathVariable String id, 
                                    @AuthenticationPrincipal User user,
                                    RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            viewingHistoryService.removeFromHistory(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa khỏi lịch sử!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }
        
        return "redirect:/history";
    }

    // Xóa toàn bộ lịch sử
    @GetMapping("/clear")
    public String clearHistory(@AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes) {
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            viewingHistoryService.clearUserHistory(user);
            redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ lịch sử xem!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }
        
        return "redirect:/history";
    }
}
