package com.hutech.bai7.controller;

import com.hutech.bai7.repository.BookRepository;
import com.hutech.bai7.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final BookRepository bookRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String viewCart(Model model) {
        model.addAttribute("items", cartService.getCart().getItems().values());
        model.addAttribute("total", cartService.getTotal());
        return "cart/cart";
    }

    @PostMapping("/add/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String addToCart(@PathVariable String bookId) {
        bookRepository.findById(bookId).ifPresent(book -> 
            cartService.addToCart(book, 1));
        return "redirect:/books";
    }

    @GetMapping("/remove/{bookId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String removeFromCart(@PathVariable String bookId) {
        cartService.removeFromCart(bookId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
