package com.hutech.bai5.repository;

import com.hutech.bai5.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    List<Invoice> findByCustomerEmailOrderByOrderDateDesc(String email);
}
