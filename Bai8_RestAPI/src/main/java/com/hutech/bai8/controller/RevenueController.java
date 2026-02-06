package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/revenue")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class RevenueController {
    private final RevenueService revenueService;
    private final Cart cart;

    @GetMapping
    public String revenueDashboard(Model model) {
        // Thống kê tổng quan
        RevenueService.RevenueStatistics stats = revenueService.getRevenueStatistics();
        model.addAttribute("stats", stats);
        
        // Doanh thu theo tháng (năm hiện tại)
        int currentYear = LocalDate.now().getYear();
        List<RevenueService.MonthlyRevenue> monthlyRevenue = revenueService.getMonthlyRevenue(currentYear);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("currentYear", currentYear);
        
        // Doanh thu 7 ngày gần nhất
        List<RevenueService.DailyRevenue> dailyRevenue = revenueService.getDailyRevenue(7);
        model.addAttribute("dailyRevenue", dailyRevenue);
        
        // Top sản phẩm bán chạy
        List<RevenueService.ProductSales> topProducts = revenueService.getTopSellingProducts(10);
        model.addAttribute("topProducts", topProducts);
        
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/revenue/dashboard";
    }

    @GetMapping("/monthly")
    @ResponseBody
    public List<RevenueService.MonthlyRevenue> getMonthlyRevenue(@RequestParam int year) {
        return revenueService.getMonthlyRevenue(year);
    }

    @GetMapping("/daily")
    @ResponseBody
    public List<RevenueService.DailyRevenue> getDailyRevenue(@RequestParam(defaultValue = "7") int days) {
        return revenueService.getDailyRevenue(days);
    }

    @GetMapping("/top-products")
    @ResponseBody
    public List<RevenueService.ProductSales> getTopProducts(@RequestParam(defaultValue = "10") int limit) {
        return revenueService.getTopSellingProducts(limit);
    }
}
