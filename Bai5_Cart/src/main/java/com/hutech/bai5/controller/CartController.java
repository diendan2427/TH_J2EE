package com.hutech.bai5.controller;

import com.hutech.bai5.service.BookService;
import com.hutech.bai5.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Bài 4.5: CartController - Điều khiển giỏ hàng
 */
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final BookService bookService;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("totalItems", cartService.getTotalItems());
        return "cart/cart";
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable String bookId, 
                           @RequestParam(defaultValue = "1") int quantity) {
        bookService.getBookById(bookId).ifPresent(book -> 
            cartService.addToCart(book, quantity));
        return "redirect:/books";
    }

    @PostMapping("/update/{bookId}")
    public String updateQuantity(@PathVariable String bookId, 
                                @RequestParam int quantity) {
        cartService.updateQuantity(bookId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable String bookId) {
        cartService.removeFromCart(bookId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
