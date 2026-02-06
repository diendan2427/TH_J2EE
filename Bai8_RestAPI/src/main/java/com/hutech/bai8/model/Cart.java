package com.hutech.bai8.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Component
@SessionScope
public class Cart {
    private List<CartItem> items = new ArrayList<>();
    private Voucher appliedVoucher; // Voucher đã áp dụng
    private double discountAmount; // Số tiền giảm giá
    
    public void addItem(Book book) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            items.add(new CartItem(book, 1));
        }
    }
    
    public void removeItem(String bookId) {
        items.removeIf(item -> item.getBook().getId().equals(bookId));
    }
    
    public void updateQuantity(String bookId, int quantity) {
        items.stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity <= 0) {
                        removeItem(bookId);
                    } else {
                        item.setQuantity(quantity);
                    }
                });
    }
    
    public void clear() {
        items.clear();
        appliedVoucher = null;
        discountAmount = 0;
    }
    
    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
    
    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
    
    // Tổng tiền sau khi giảm giá
    public double getFinalPrice() {
        return getTotalPrice() - discountAmount;
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    // Áp dụng voucher
    public void applyVoucher(Voucher voucher, double discount) {
        this.appliedVoucher = voucher;
        this.discountAmount = discount;
    }
    
    // Xóa voucher
    public void removeVoucher() {
        this.appliedVoucher = null;
        this.discountAmount = 0;
    }
    
    // Kiểm tra đã áp dụng voucher chưa
    public boolean hasVoucher() {
        return appliedVoucher != null;
    }
}
