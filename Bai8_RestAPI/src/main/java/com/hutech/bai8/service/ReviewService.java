package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Review;
import com.hutech.bai8.model.User;
import com.hutech.bai8.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    
    // Thêm review mới
    public Review addReview(User user, Book book, Integer rating, String comment) {
        // Kiểm tra đã review chưa
        if (reviewRepository.existsByUserAndBook(user, book)) {
            throw new RuntimeException("Bạn đã đánh giá sách này rồi!");
        }
        
        Review review = new Review(user, book, rating, comment);
        return reviewRepository.save(review);
    }
    
    // Cập nhật review
    public Review updateReview(String reviewId, Integer rating, String comment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));
        
        review.setRating(rating);
        review.setComment(comment);
        review.setApproved(false); // Cập nhật lại cần duyệt lại
        review.updateTimestamp();
        
        return reviewRepository.save(review);
    }
    
    // Xóa review
    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }
    
    // Lấy review của sách (đã duyệt)
    public List<Review> getApprovedReviewsByBook(Book book) {
        return reviewRepository.findByBookAndApprovedTrueOrderByCreatedAtDesc(book);
    }
    
    // Lấy review của user
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    // Kiểm tra user đã review chưa
    public boolean hasReviewed(User user, Book book) {
        return reviewRepository.existsByUserAndBook(user, book);
    }
    
    // Lấy review của user cho sách cụ thể
    public Optional<Review> getUserReviewForBook(User user, Book book) {
        return reviewRepository.findByUserAndBook(user, book);
    }
    
    // Tính trung bình số sao
    public Double getAverageRating(Book book) {
        List<Review> reviews = reviewRepository.findRatingsByBook(book);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();
        return sum / reviews.size();
    }
    
    // Đếm số lượng review
    public long countReviews(Book book) {
        return reviewRepository.countByBookAndApprovedTrue(book);
    }
    
    // Admin: Duyệt review
    public Review approveReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá!"));
        
        review.setApproved(true);
        review.updateTimestamp();
        return reviewRepository.save(review);
    }
    
    // Admin: Từ chối review (xóa)
    public void rejectReview(String reviewId) {
        deleteReview(reviewId);
    }
    
    // Admin: Lấy tất cả review chưa duyệt
    public List<Review> getPendingReviews() {
        return reviewRepository.findByApprovedFalseOrderByCreatedAtDesc();
    }
    
    // Admin: Lấy tất cả review
    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }
}
