package com.hutech.bai8.repository;

import com.hutech.bai8.model.Order;
import com.hutech.bai8.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    List<Order> findAllByOrderByCreatedAtDesc();
    List<Order> findByStatus(String status);
    Optional<Order> findByMomoOrderId(String momoOrderId);
}
