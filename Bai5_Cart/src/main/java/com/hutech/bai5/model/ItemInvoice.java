package com.hutech.bai5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bài 5: Chi tiết hóa đơn
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInvoice {
    private String bookId;
    private String bookTitle;
    private Double price;
    private Integer quantity;
    private Double subtotal;
}
