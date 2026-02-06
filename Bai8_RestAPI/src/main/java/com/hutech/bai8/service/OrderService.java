package com.hutech.bai8.service;

import com.hutech.bai8.model.*;
import com.hutech.bai8.repository.BookRepository;
import com.hutech.bai8.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Order createOrder(Cart cart, User user, String customerName, String email, 
                            String phone, String address, String note) {
        // Kiem tra va cap nhat so luong ton kho
        for (CartItem cartItem : cart.getItems()) {
            Book book = bookRepository.findById(cartItem.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Khong tim thay san pham: " + cartItem.getBook().getTitle()));
            
            if (book.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("San pham \"" + book.getTitle() + "\" chi con " + book.getStock() + " trong kho!");
            }
            
            // Tru so luong ton kho
            book.setStock(book.getStock() - cartItem.getQuantity());
            bookRepository.save(book);
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setCustomerEmail(email);
        order.setCustomerPhone(phone);
        order.setShippingAddress(address);
        order.setNote(note);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(OrderItem::fromCartItem)
                .collect(Collectors.toList());
        order.setItems(orderItems);
        order.calculateTotal();
        
        return orderRepository.save(order);
    }

    @Transactional
    public Order createOrder(Cart cart, User user, String customerName, String email, 
                            String phone, String address, String note, String status, String momoOrderId) {
        // Kiem tra va cap nhat so luong ton kho
        for (CartItem cartItem : cart.getItems()) {
            Book book = bookRepository.findById(cartItem.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Khong tim thay san pham: " + cartItem.getBook().getTitle()));
            
            if (book.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("San pham \"" + book.getTitle() + "\" chi con " + book.getStock() + " trong kho!");
            }
            
            // Tru so luong ton kho
            book.setStock(book.getStock() - cartItem.getQuantity());
            bookRepository.save(book);
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setCustomerEmail(email);
        order.setCustomerPhone(phone);
        order.setShippingAddress(address);
        order.setNote(note);
        order.setStatus(status);
        order.setPaymentStatus("PENDING");
        order.setMomoOrderId(momoOrderId);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(OrderItem::fromCartItem)
                .collect(Collectors.toList());
        order.setItems(orderItems);
        order.calculateTotal();
        
        return orderRepository.save(order);
    }

    public Order saveOrder(Order order) {
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByMomoOrderId(String momoOrderId) {
        return orderRepository.findByMomoOrderId(momoOrderId);
    }

    @Transactional
    public Order updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        
        // Neu chuyen sang trang thai CANCELLED, hoan tra so luong ton kho
        if ("CANCELLED".equals(status) && !"CANCELLED".equals(order.getStatus())) {
            restoreStock(order);
        }
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
    
    // Hoan tra so luong ton kho khi huy don
    @Transactional
    public void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Book book = bookRepository.findById(item.getBookId())
                    .orElse(null);
            if (book != null) {
                book.setStock(book.getStock() + item.getQuantity());
                bookRepository.save(book);
            }
        }
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
