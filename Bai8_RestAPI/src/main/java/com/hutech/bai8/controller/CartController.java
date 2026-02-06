package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.User;
import com.hutech.bai8.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final Cart cart;
    private final BookService bookService;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("items", cart.getItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "cart/cart";
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable String bookId, 
                           @AuthenticationPrincipal User user,
                           RedirectAttributes redirectAttributes) {
        // Kiem tra neu la ADMIN thi khong cho them vao gio hang
        if (user != null && user.hasRole("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Admin khong the them san pham vao gio hang!");
            return "redirect:/books";
        }
        
        Book book = bookService.getBookById(bookId).orElse(null);
        if (book == null) {
            redirectAttributes.addFlashAttribute("error", "Khong tim thay san pham!");
            return "redirect:/books";
        }
        
        // Kiem tra số lượng tồn kho
        if (book.getStock() == null || book.getStock() <= 0) {
            redirectAttributes.addFlashAttribute("error", "San pham \"" + book.getTitle() + "\" da het hang!");
            return "redirect:/books";
        }
        
        // Kiem tra số lượng trong giỏ hàng hiện tại
        int currentQuantityInCart = cart.getItems().stream()
            .filter(item -> item.getBook().getId().equals(bookId))
            .mapToInt(item -> item.getQuantity())
            .sum();
        
        if (currentQuantityInCart + 1 > book.getStock()) {
            redirectAttributes.addFlashAttribute("error", 
                "Khong the them! Chi con " + book.getStock() + " san pham \"" + book.getTitle() + "\" trong kho.");
            return "redirect:/cart";
        }
        
        cart.addItem(book);
        redirectAttributes.addFlashAttribute("success", "Da them \"" + book.getTitle() + "\" vao gio hang!");
        return "redirect:/books";
    }

    @GetMapping("/add/{bookId}")
    public String addToCartGet(@PathVariable String bookId, 
                              @AuthenticationPrincipal User user,
                              RedirectAttributes redirectAttributes) {
        return addToCart(bookId, user, redirectAttributes);
    }

    @PostMapping("/update/{bookId}")
    public String updateQuantity(@PathVariable String bookId, 
                                @RequestParam int quantity,
                                RedirectAttributes redirectAttributes) {
        Book book = bookService.getBookById(bookId).orElse(null);
        
        if (book != null && quantity > 0) {
            // Kiem tra số lượng tồn kho
            if (quantity > book.getStock()) {
                redirectAttributes.addFlashAttribute("error", 
                    "Chi con " + book.getStock() + " san pham \"" + book.getTitle() + "\" trong kho!");
                return "redirect:/cart";
            }
        }
        
        cart.updateQuantity(bookId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable String bookId, RedirectAttributes redirectAttributes) {
        cart.removeItem(bookId);
        redirectAttributes.addFlashAttribute("success", "Da xoa san pham khoi gio hang!");
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        cart.clear();
        redirectAttributes.addFlashAttribute("success", "Da xoa gio hang!");
        return "redirect:/cart";
    }
}
