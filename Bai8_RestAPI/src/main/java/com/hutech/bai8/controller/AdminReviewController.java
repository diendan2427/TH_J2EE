package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminReviewController {
    private final ReviewService reviewService;
    private final Cart cart;

    // Danh sách tất cả review
    @GetMapping
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        model.addAttribute("pendingCount", reviewService.getPendingReviews().size());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/reviews/list";
    }

    // Danh sách review chờ duyệt
    @GetMapping("/pending")
    public String pendingReviews(Model model) {
        model.addAttribute("reviews", reviewService.getPendingReviews());
        model.addAttribute("pendingCount", reviewService.getPendingReviews().size());
        model.addAttribute("cartCount", cart.getTotalItems());
        model.addAttribute("isPendingPage", true);
        return "admin/reviews/list";
    }

    // Duyệt review
    @GetMapping("/approve/{id}")
    public String approveReview(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.approveReview(id);
            redirectAttributes.addFlashAttribute("success", "Đã duyệt bình luận!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reviews/pending";
    }

    // Từ chối review (xóa)
    @GetMapping("/reject/{id}")
    public String rejectReview(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.rejectReview(id);
            redirectAttributes.addFlashAttribute("success", "Đã từ chối và xóa bình luận!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reviews/pending";
    }

    // Xóa review (từ danh sách tất cả)
    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa bình luận!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reviews";
    }
}
