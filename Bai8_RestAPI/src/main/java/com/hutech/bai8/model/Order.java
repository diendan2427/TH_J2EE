package com.hutech.bai8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    
    @DBRef
    private User user;
    
    private List<OrderItem> items = new ArrayList<>();
    
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private String note;
    
    private double totalAmount;
    private double originalAmount; // Tổng tiền gốc trước khi giảm giá
    private double discountAmount; // Số tiền giảm giá
    private String voucherCode; // Mã voucher đã sử dụng
    private String status; // PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED
    
    // Payment fields
    private String paymentMethod; // COD, MOMO
    private String paymentStatus; // PENDING, PAID, FAILED
    private String momoOrderId; // MoMo Order ID
    private String momoTransId; // MoMo Transaction ID
    private String paymentMessage;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
