package com.hutech.bai8.repository;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Favorite;
import com.hutech.bai8.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends MongoRepository<Favorite, String> {
    List<Favorite> findByUserOrderByCreatedAtDesc(User user);
    
    Optional<Favorite> findByUserAndBook(User user, Book book);
    
    boolean existsByUserAndBook(User user, Book book);
    
    void deleteByUserAndBook(User user, Book book);
    
    long countByBook(Book book); // Đếm số lượt yêu thích của một cuốn sách
}
