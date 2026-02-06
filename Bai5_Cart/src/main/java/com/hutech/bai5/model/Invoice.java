package com.hutech.bai5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bài 5: Entity Invoice (Hóa đơn)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private List<ItemInvoice> items;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED
}
