package com.hutech.bai5.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * Bài 4.5: Lớp Cart (Giỏ hàng) lưu trong Session
 */
@Data
public class Cart {
    private Map<String, Item> items = new HashMap<>();
    
    public void addItem(Book book, int quantity) {
        String bookId = book.getId();
        if (items.containsKey(bookId)) {
            Item item = items.get(bookId);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.put(bookId, new Item(book, quantity));
        }
    }
    
    public void updateQuantity(String bookId, int quantity) {
        if (items.containsKey(bookId)) {
            if (quantity <= 0) {
                items.remove(bookId);
            } else {
                items.get(bookId).setQuantity(quantity);
            }
        }
    }
    
    public void removeItem(String bookId) {
        items.remove(bookId);
    }
    
    public void clear() {
        items.clear();
    }
    
    public double getTotal() {
        return items.values().stream()
                .mapToDouble(Item::getSubtotal)
                .sum();
    }
    
    public int getTotalItems() {
        return items.values().stream()
                .mapToInt(Item::getQuantity)
                .sum();
    }
}
