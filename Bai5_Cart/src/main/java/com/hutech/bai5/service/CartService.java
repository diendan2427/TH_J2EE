package com.hutech.bai5.service;

import com.hutech.bai5.model.Book;
import com.hutech.bai5.model.Cart;
import com.hutech.bai5.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Bài 4.5: CartService - Quản lý giỏ hàng trong Session
 */
@Service
@SessionScope
public class CartService {
    private final Cart cart = new Cart();
    
    public Cart getCart() {
        return cart;
    }
    
    public void addToCart(Book book, int quantity) {
        cart.addItem(book, quantity);
    }
    
    public void updateQuantity(String bookId, int quantity) {
        cart.updateQuantity(bookId, quantity);
    }
    
    public void removeFromCart(String bookId) {
        cart.removeItem(bookId);
    }
    
    public void clearCart() {
        cart.clear();
    }
    
    public double getTotal() {
        return cart.getTotal();
    }
    
    public int getTotalItems() {
        return cart.getTotalItems();
    }
    
    public java.util.Collection<Item> getItems() {
        return cart.getItems().values();
    }
}
