package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.User;
import com.hutech.bai8.model.ViewingHistory;
import com.hutech.bai8.repository.ViewingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewingHistoryService {
    private final ViewingHistoryRepository viewingHistoryRepository;
    
    // Ghi lại lịch sử xem
    public void recordView(User user, Book book) {
        // Kiểm tra đã có trong lịch sử chưa
        Optional<ViewingHistory> existing = viewingHistoryRepository.findByUserAndBook(user, book);
        
        if (existing.isPresent()) {
            // Cập nhật thờ gian xem
            ViewingHistory history = existing.get();
            history.setViewedAt(LocalDateTime.now());
            viewingHistoryRepository.save(history);
        } else {
            // Tạo mới
            ViewingHistory history = new ViewingHistory(user, book);
            viewingHistoryRepository.save(history);
        }
    }
    
    // Lấy lịch sử xem của user
    public List<ViewingHistory> getUserViewingHistory(User user) {
        return viewingHistoryRepository.findByUserOrderByViewedAtDesc(user);
    }
    
    // Lấy lịch sử xem gần đây (giới hạn số lượng)
    public List<ViewingHistory> getRecentViewingHistory(User user, int limit) {
        List<ViewingHistory> allHistory = viewingHistoryRepository.findByUserOrderByViewedAtDesc(user);
        return allHistory.stream().limit(limit).collect(Collectors.toList());
    }
    
    // Lấy danh sách sách đã xem (không trùng lặp)
    public List<Book> getViewedBooks(User user, int limit) {
        List<ViewingHistory> history = getRecentViewingHistory(user, limit);
        return history.stream()
                .map(ViewingHistory::getBook)
                .distinct()
                .collect(Collectors.toList());
    }
    
    // Xóa một bản ghi lịch sử
    public void removeFromHistory(String historyId) {
        viewingHistoryRepository.deleteById(historyId);
    }
    
    // Xóa toàn bộ lịch sử của user
    public void clearUserHistory(User user) {
        List<ViewingHistory> userHistory = viewingHistoryRepository.findByUserOrderByViewedAtDesc(user);
        viewingHistoryRepository.deleteAll(userHistory);
    }
    
    // Dọn dẹp lịch sử cũ (giữ lại 30 ngày gần nhất)
    public void cleanupOldHistory() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        viewingHistoryRepository.deleteByViewedAtBefore(thirtyDaysAgo);
    }
    
    // Kiểm tra user đã xem sách chưa
    public boolean hasViewed(User user, Book book) {
        return viewingHistoryRepository.findByUserAndBook(user, book).isPresent();
    }
}
