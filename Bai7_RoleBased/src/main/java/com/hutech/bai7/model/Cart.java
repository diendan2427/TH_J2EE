package com.hutech.bai7.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * Giỏ hàng cho USER
 */
@Data
public class Cart {
    private Map<String, CartItem> items = new HashMap<>();
    
    public void addItem(Book book, int quantity) {
        if (items.containsKey(book.getId())) {
            CartItem item = items.get(book.getId());
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.put(book.getId(), new CartItem(book, quantity));
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
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
    }
    
    public int getTotalItems() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }
}
