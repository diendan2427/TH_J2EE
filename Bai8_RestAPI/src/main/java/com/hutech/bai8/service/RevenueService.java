package com.hutech.bai8.service;

import com.hutech.bai8.model.Order;
import com.hutech.bai8.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final OrderRepository orderRepository;

    // Thống kê tổng doanh thu
    public RevenueStatistics getRevenueStatistics() {
        List<Order> allOrders = orderRepository.findAll();
        
        double totalRevenue = allOrders.stream()
                .filter(order -> order.getTotalAmount() > 0 && 
                        (order.getStatus().equals("CONFIRMED") || order.getStatus().equals("DELIVERED")))
                .mapToDouble(Order::getTotalAmount)
                .sum();
        
        long totalOrders = allOrders.stream()
                .filter(order -> order.getStatus().equals("CONFIRMED") || order.getStatus().equals("DELIVERED"))
                .count();
        
        double averageOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;
        
        return new RevenueStatistics(totalRevenue, totalOrders, averageOrderValue);
    }

    // Thống kê doanh thu theo tháng
    public List<MonthlyRevenue> getMonthlyRevenue(int year) {
        List<Order> orders = orderRepository.findAll();
        
        Map<Integer, Double> monthlyRevenue = new HashMap<>();
        Map<Integer, Long> monthlyOrders = new HashMap<>();
        
        // Khởi tạo tất cả tháng = 0
        for (int i = 1; i <= 12; i++) {
            monthlyRevenue.put(i, 0.0);
            monthlyOrders.put(i, 0L);
        }
        
        for (Order order : orders) {
            if (order.getCreatedAt() != null && order.getCreatedAt().getYear() == year) {
                if (order.getStatus().equals("CONFIRMED") || order.getStatus().equals("DELIVERED")) {
                    int month = order.getCreatedAt().getMonthValue();
                    monthlyRevenue.put(month, monthlyRevenue.get(month) + order.getTotalAmount());
                    monthlyOrders.put(month, monthlyOrders.get(month) + 1);
                }
            }
        }
        
        List<MonthlyRevenue> result = new ArrayList<>();
        String[] monthNames = {"", "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        
        for (int i = 1; i <= 12; i++) {
            result.add(new MonthlyRevenue(monthNames[i], monthlyRevenue.get(i), monthlyOrders.get(i)));
        }
        
        return result;
    }

    // Top sản phẩm bán chạy
    public List<ProductSales> getTopSellingProducts(int limit) {
        List<Order> orders = orderRepository.findAll();
        
        Map<String, ProductSales> productMap = new HashMap<>();
        
        for (Order order : orders) {
            if (order.getStatus().equals("CONFIRMED") || order.getStatus().equals("DELIVERED")) {
                order.getItems().forEach(item -> {
                    String bookId = item.getBookId();
                    String bookTitle = item.getBookTitle();
                    
                    ProductSales sales = productMap.getOrDefault(bookId, 
                            new ProductSales(bookId, bookTitle, 0, 0.0));
                    
                    sales.setQuantitySold(sales.getQuantitySold() + item.getQuantity());
                    sales.setRevenue(sales.getRevenue() + item.getSubtotal());
                    
                    productMap.put(bookId, sales);
                });
            }
        }
        
        return productMap.values().stream()
                .sorted((a, b) -> Integer.compare(b.getQuantitySold(), a.getQuantitySold()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Doanh thu theo ngày (7 ngày gần nhất)
    public List<DailyRevenue> getDailyRevenue(int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        
        List<Order> orders = orderRepository.findAll();
        
        Map<LocalDate, Double> dailyRevenue = new LinkedHashMap<>();
        Map<LocalDate, Long> dailyOrders = new LinkedHashMap<>();
        
        // Khởi tạo các ngày
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dailyRevenue.put(date, 0.0);
            dailyOrders.put(date, 0L);
        }
        
        for (Order order : orders) {
            if (order.getCreatedAt() != null && 
                !order.getCreatedAt().isBefore(startDate) && 
                !order.getCreatedAt().isAfter(endDate)) {
                
                if (order.getStatus().equals("CONFIRMED") || order.getStatus().equals("DELIVERED")) {
                    LocalDate date = order.getCreatedAt().toLocalDate();
                    dailyRevenue.put(date, dailyRevenue.getOrDefault(date, 0.0) + order.getTotalAmount());
                    dailyOrders.put(date, dailyOrders.getOrDefault(date, 0L) + 1);
                }
            }
        }
        
        List<DailyRevenue> result = new ArrayList<>();
        dailyRevenue.forEach((date, revenue) -> {
            result.add(new DailyRevenue(date.toString(), revenue, dailyOrders.get(date)));
        });
        
        return result;
    }

    // DTO classes
    @Data
    @AllArgsConstructor
    public static class RevenueStatistics {
        private double totalRevenue;
        private long totalOrders;
        private double averageOrderValue;
    }

    @Data
    @AllArgsConstructor
    public static class MonthlyRevenue {
        private String month;
        private double revenue;
        private long orderCount;
    }

    @Data
    @AllArgsConstructor
    public static class DailyRevenue {
        private String date;
        private double revenue;
        private long orderCount;
    }

    @Data
    @AllArgsConstructor
    public static class ProductSales {
        private String bookId;
        private String bookTitle;
        private int quantitySold;
        private double revenue;
    }
}
