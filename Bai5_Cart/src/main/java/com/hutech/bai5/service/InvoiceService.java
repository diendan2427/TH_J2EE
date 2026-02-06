package com.hutech.bai5.service;

import com.hutech.bai5.model.*;
import com.hutech.bai5.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CartService cartService;

    public Invoice createInvoice(String customerName, String email, String phone, String address) {
        List<ItemInvoice> items = cartService.getItems().stream()
                .map(item -> new ItemInvoice(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getBook().getPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());

        Invoice invoice = new Invoice();
        invoice.setCustomerName(customerName);
        invoice.setCustomerEmail(email);
        invoice.setCustomerPhone(phone);
        invoice.setShippingAddress(address);
        invoice.setItems(items);
        invoice.setTotalAmount(cartService.getTotal());
        invoice.setOrderDate(LocalDateTime.now());
        invoice.setStatus("PENDING");

        Invoice savedInvoice = invoiceRepository.save(invoice);
        cartService.clearCart();
        
        return savedInvoice;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(String id) {
        return invoiceRepository.findById(id).orElse(null);
    }
}
