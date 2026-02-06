package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.Order;
import com.hutech.bai8.model.User;
import com.hutech.bai8.service.OrderService;
import com.hutech.bai8.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final Cart cart;

    @GetMapping
    public String myOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        List<Order> orders = orderService.getOrdersByUser(user);
        
        model.addAttribute("orders", orders);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable String id, 
                             @AuthenticationPrincipal UserDetails userDetails, 
                             Model model) {
        Order order = orderService.getOrderById(id).orElse(null);
        if (order == null) {
            return "redirect:/orders";
        }
        
        // Check if user owns this order or is admin
        User user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user != null && !order.getUser().getId().equals(user.getId()) && !user.hasRole("ADMIN")) {
            return "redirect:/orders";
        }
        
        model.addAttribute("order", order);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "orders/detail";
    }

    // Admin functions
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String allOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "orders/admin-list";
    }

    @PostMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStatus(@PathVariable String id, 
                              @RequestParam String status,
                              RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Cap nhat trang thai thanh cong!");
        return "redirect:/orders/admin";
    }
}
