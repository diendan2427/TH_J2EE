package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.service.BookService;
import com.hutech.bai8.service.CategoryService;
import com.hutech.bai8.service.OrderService;
import com.hutech.bai8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final Cart cart;

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        // Statistics
        long totalBooks = bookService.getAllBooks().size();
        long totalCategories = categoryService.getAllCategories().size();
        long totalOrders = orderService.getAllOrders().size();
        long totalUsers = userRepository.count();
        
        // Pending orders count
        long pendingOrders = orderService.getOrdersByStatus("PENDING").size();
        
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("recentOrders", orderService.getAllOrders().stream().limit(5).toArray());
        model.addAttribute("cartCount", cart.getTotalItems());
        
        return "admin/dashboard";
    }

    @GetMapping("/books")
    public String manageBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/books/list";
    }

    @GetMapping("/categories")
    public String manageCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/categories/list";
    }

    @GetMapping("/orders")
    public String manageOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/orders/list";
    }
}
