package com.hutech.bai8.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@SessionScope
public class ProductComparison {
    private List<Book> products = new ArrayList<>();
    private static final int MAX_PRODUCTS = 4; // Tối đa 4 sản phẩm để so sánh
    
    public void addProduct(Book book) {
        // Kiểm tra sản phẩm đã tồn tại chưa
        if (products.stream().anyMatch(p -> p.getId().equals(book.getId()))) {
            return;
        }
        
        // Nếu đã đầy, xóa sản phẩm đầu tiên
        if (products.size() >= MAX_PRODUCTS) {
            products.remove(0);
        }
        
        products.add(book);
    }
    
    public void removeProduct(String bookId) {
        products.removeIf(p -> p.getId().equals(bookId));
    }
    
    public void clear() {
        products.clear();
    }
    
    public boolean isEmpty() {
        return products.isEmpty();
    }
    
    public int getCount() {
        return products.size();
    }
    
    public boolean isFull() {
        return products.size() >= MAX_PRODUCTS;
    }
    
    public boolean contains(String bookId) {
        return products.stream().anyMatch(p -> p.getId().equals(bookId));
    }
}
