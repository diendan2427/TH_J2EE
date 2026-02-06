package com.hutech.bai8.repository;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Review;
import com.hutech.bai8.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    // Lấy tất cả review của một cuốn sách (đã duyệt)
    List<Review> findByBookAndApprovedTrueOrderByCreatedAtDesc(Book book);
    
    // Lấy tất cả review của một user
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    
    // Kiểm tra user đã review sách chưa
    Optional<Review> findByUserAndBook(User user, Book book);
    
    boolean existsByUserAndBook(User user, Book book);
    
    // Lấy tất cả review chưa duyệt (cho admin)
    List<Review> findByApprovedFalseOrderByCreatedAtDesc();
    
    // Lấy tất cả review đã duyệt (cho admin)
    List<Review> findByApprovedTrueOrderByCreatedAtDesc();
    
    // Lấy tất cả review
    List<Review> findAllByOrderByCreatedAtDesc();
    
    // Đếm số lượng review của sách
    long countByBookAndApprovedTrue(Book book);
    
    // Tính trung bình số sao
    @Query(value = "{ 'book': ?0, 'approved': true }", fields = "{ 'rating': 1 }")
    List<Review> findRatingsByBook(Book book);
}
