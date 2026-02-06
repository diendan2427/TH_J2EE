package com.hutech.bai7.service;

import com.hutech.bai7.model.Book;
import com.hutech.bai7.model.Cart;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

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
}
