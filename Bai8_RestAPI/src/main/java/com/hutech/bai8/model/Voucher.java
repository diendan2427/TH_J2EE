package com.hutech.bai8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vouchers")
public class Voucher {
    @Id
    private String id;
    
    private String code; // Mã voucher (ví dụ: SALE10, NEWUSER20)
    
    private String description; // Mô tả voucher
    
    private Double discountPercent; // Phần trăm giảm giá (ví dụ: 10.0 = 10%)
    
    private Double maxDiscountAmount; // Số tiền giảm tối đa (ví dụ: 100000)
    
    private Double minOrderAmount; // Số tiền đơn hàng tối thiểu để áp dụng
    
    private Integer maxUses; // Số lần sử dụng tối đa
    
    private Integer usedCount; // Số lần đã sử dụng
    
    private LocalDateTime startDate; // Ngày bắt đầu có hiệu lực
    
    private LocalDateTime endDate; // Ngày hết hạn
    
    private boolean active; // Trạng thái kích hoạt
    
    private LocalDateTime createdAt;
    
    // Kiểm tra voucher còn hiệu lực không
    public boolean isValid() {
        if (!active) return false;
        if (maxUses != null && usedCount != null && usedCount >= maxUses) return false;
        
        LocalDateTime now = LocalDateTime.now();
        if (startDate != null && now.isBefore(startDate)) return false;
        if (endDate != null && now.isAfter(endDate)) return false;
        
        return true;
    }
    
    // Kiểm tra đơn hàng đủ điều kiện áp dụng voucher không
    public boolean isApplicable(double orderAmount) {
        if (!isValid()) return false;
        if (minOrderAmount != null && orderAmount < minOrderAmount) return false;
        return true;
    }
    
    // Tính số tiền giảm giá
    public double calculateDiscount(double orderAmount) {
        if (!isApplicable(orderAmount)) return 0;
        
        double discount = orderAmount * (discountPercent / 100);
        
        if (maxDiscountAmount != null && discount > maxDiscountAmount) {
            discount = maxDiscountAmount;
        }
        
        return discount;
    }
    
    // Tăng số lần sử dụng
    public void incrementUsedCount() {
        if (usedCount == null) {
            usedCount = 1;
        } else {
            usedCount++;
        }
    }
}
