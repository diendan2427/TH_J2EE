package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.ProductComparison;
import com.hutech.bai8.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/compare")
@RequiredArgsConstructor
public class ComparisonController {
    private final BookService bookService;
    private final ProductComparison comparison;
    private final Cart cart;

    // Xem trang so sánh
    @GetMapping
    public String viewComparison(Model model) {
        model.addAttribute("products", comparison.getProducts());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "comparison/compare";
    }

    // Thêm sản phẩm vào so sánh
    @PostMapping("/add/{bookId}")
    public String addToComparison(@PathVariable String bookId, 
                                  RedirectAttributes redirectAttributes) {
        if (comparison.isFull()) {
            redirectAttributes.addFlashAttribute("error", 
                "Bạn chỉ có thể so sánh tối đa 4 sản phẩm! Vui lòng xóa bớt sản phẩm.");
            return "redirect:/books";
        }
        
        Book book = bookService.getBookById(bookId).orElse(null);
        if (book != null) {
            comparison.addProduct(book);
            redirectAttributes.addFlashAttribute("success", 
                "Đã thêm \"" + book.getTitle() + "\" vào so sánh!");
        }
        
        return "redirect:/books";
    }

    // Xóa sản phẩm khỏi so sánh
    @GetMapping("/remove/{bookId}")
    public String removeFromComparison(@PathVariable String bookId,
                                       RedirectAttributes redirectAttributes) {
        comparison.removeProduct(bookId);
        redirectAttributes.addFlashAttribute("success", "Đã xóa khỏi danh sách so sánh!");
        return "redirect:/compare";
    }

    // Xóa tất cả
    @GetMapping("/clear")
    public String clearComparison(RedirectAttributes redirectAttributes) {
        comparison.clear();
        redirectAttributes.addFlashAttribute("success", "Đã xóa tất cả sản phẩm so sánh!");
        return "redirect:/books";
    }
}
