package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Favorite;
import com.hutech.bai8.model.User;
import com.hutech.bai8.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    
    // Lấy danh sách yêu thích của user
    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    // Thêm vào yêu thích
    public Favorite addToFavorites(User user, Book book) {
        // Kiểm tra đã tồn tại chưa
        if (favoriteRepository.existsByUserAndBook(user, book)) {
            throw new RuntimeException("Sản phẩm này đã có trong danh sách yêu thích!");
        }
        
        Favorite favorite = new Favorite(user, book);
        return favoriteRepository.save(favorite);
    }
    
    // Xóa khỏi yêu thích
    public void removeFromFavorites(User user, Book book) {
        favoriteRepository.deleteByUserAndBook(user, book);
    }
    
    // Xóa theo ID
    public void removeFavoriteById(String favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
    
    // Kiểm tra đã yêu thích chưa
    public boolean isFavorite(User user, Book book) {
        return favoriteRepository.existsByUserAndBook(user, book);
    }
    
    // Đếm số lượt yêu thích của sách
    public long countFavoritesByBook(Book book) {
        return favoriteRepository.countByBook(book);
    }
}
