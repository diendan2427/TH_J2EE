package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Review;
import com.hutech.bai8.model.User;
import com.hutech.bai8.service.BookService;
import com.hutech.bai8.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final BookService bookService;

    // Thêm review mới
    @PostMapping("/add/{bookId}")
    public String addReview(@PathVariable String bookId,
                           @AuthenticationPrincipal User user,
                           @RequestParam @Valid Integer rating,
                           @RequestParam @Valid String comment,
                           RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra rating hợp lệ
            if (rating < 1 || rating > 5) {
                redirectAttributes.addFlashAttribute("error", "Số sao phải từ 1 đến 5!");
                return "redirect:/books/detail/" + bookId;
            }
            
            // Kiểm tra comment không rỗng
            if (comment == null || comment.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập nội dung bình luận!");
                return "redirect:/books/detail/" + bookId;
            }
            
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sách!"));
            
            Review review = reviewService.addReview(user, book, rating, comment.trim());
            redirectAttributes.addFlashAttribute("success", 
                "Cảm ơn bạn đã đánh giá! Bình luận của bạn đang chờ duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/books/detail/" + bookId;
    }

    // Cập nhật review
    @PostMapping("/update/{reviewId}")
    public String updateReview(@PathVariable String reviewId,
                              @RequestParam Integer rating,
                              @RequestParam String comment,
                              @RequestParam String bookId,
                              RedirectAttributes redirectAttributes) {
        try {
            if (rating < 1 || rating > 5) {
                redirectAttributes.addFlashAttribute("error", "Số sao phải từ 1 đến 5!");
                return "redirect:/books/detail/" + bookId;
            }
            
            reviewService.updateReview(reviewId, rating, comment.trim());
            redirectAttributes.addFlashAttribute("success", 
                "Đã cập nhật đánh giá! Bình luận đang chờ duyệt lại.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/books/detail/" + bookId;
    }

    // Xóa review
    @GetMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable String reviewId,
                              @RequestParam String bookId,
                              RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(reviewId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa đánh giá!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/books/detail/" + bookId;
    }
}
