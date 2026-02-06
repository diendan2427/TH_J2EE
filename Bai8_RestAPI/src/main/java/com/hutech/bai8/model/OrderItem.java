package com.hutech.bai8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private double price;
    private int quantity;
    
    public double getSubtotal() {
        return price * quantity;
    }
    
    public static OrderItem fromCartItem(CartItem cartItem) {
        return new OrderItem(
            cartItem.getBook().getId(),
            cartItem.getBook().getTitle(),
            cartItem.getBook().getAuthor(),
            cartItem.getBook().getPrice(),
            cartItem.getQuantity()
        );
    }
}
