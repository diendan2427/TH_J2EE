package com.hutech.bai5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bài 4.5: Lớp Item trong giỏ hàng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Book book;
    private int quantity;
    
    public double getSubtotal() {
        return book.getPrice() * quantity;
    }
}
