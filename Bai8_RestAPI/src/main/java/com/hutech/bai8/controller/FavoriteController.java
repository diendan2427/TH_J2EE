package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Favorite;
import com.hutech.bai8.model.User;
import com.hutech.bai8.service.BookService;
import com.hutech.bai8.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final BookService bookService;

    // Hiển thị danh sách yêu thích
    @GetMapping
    public String viewFavorites(@AuthenticationPrincipal User user, Model model) {
        List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
        model.addAttribute("favorites", favorites);
        model.addAttribute("cartCount", 0); // Cần inject Cart nếu muốn hiển thị
        return "favorites/list";
    }

    // Thêm vào yêu thích
    @PostMapping("/add/{bookId}")
    public String addToFavorites(@PathVariable String bookId,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sách!"));
            
            favoriteService.addToFavorites(user, book);
            redirectAttributes.addFlashAttribute("success", "Đã thêm vào danh sách yêu thích!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/books";
    }

    // Xóa khỏi yêu thích
    @PostMapping("/remove/{bookId}")
    public String removeFromFavorites(@PathVariable String bookId,
                                     @AuthenticationPrincipal User user,
                                     RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(bookId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sách!"));
            
            favoriteService.removeFromFavorites(user, book);
            redirectAttributes.addFlashAttribute("success", "Đã xóa khỏi danh sách yêu thích!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/favorites";
    }

    // Xóa theo ID (từ trang danh sách yêu thích)
    @GetMapping("/delete/{id}")
    public String deleteFavorite(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            favoriteService.removeFavoriteById(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa khỏi danh sách yêu thích!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa: " + e.getMessage());
        }
        return "redirect:/favorites";
    }
}
