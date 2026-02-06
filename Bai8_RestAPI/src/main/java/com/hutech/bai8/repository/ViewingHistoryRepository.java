package com.hutech.bai8.repository;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.User;
import com.hutech.bai8.model.ViewingHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ViewingHistoryRepository extends MongoRepository<ViewingHistory, String> {
    // Lấy lịch sử xem của user, sắp xếp theo thờ gian mới nhất
    List<ViewingHistory> findByUserOrderByViewedAtDesc(User user);
    
    // Lấy lịch sử xem gần đây nhất (giới hạn số lượng)
    List<ViewingHistory> findTop10ByUserOrderByViewedAtDesc(User user);
    
    // Kiểm tra user đã xem sách chưa
    Optional<ViewingHistory> findByUserAndBook(User user, Book book);
    
    // Xóa lịch sử cũ (ví dụ: xóa các bản ghi cũ hơn 30 ngày)
    void deleteByViewedAtBefore(LocalDateTime date);
}
