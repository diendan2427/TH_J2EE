package com.hutech.bai8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
@CompoundIndexes({
    @CompoundIndex(name = "user_book_review_unique", def = "{'user': 1, 'book': 1}", unique = true)
})
public class Review {
    @Id
    private String id;
    
    @DBRef
    private User user; // Ngườ dùng đánh giá
    
    @DBRef
    private Book book; // Sách được đánh giá
    
    @NotNull(message = "Vui lòng chọn số sao đánh giá")
    @Min(value = 1, message = "Số sao tối thiểu là 1")
    @Max(value = 5, message = "Số sao tối đa là 5")
    private Integer rating; // Số sao (1-5)
    
    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String comment; // Nội dung bình luận
    
    private boolean approved; // Trạng thái duyệt (admin phải duyệt mới hiển thị)
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Review(User user, Book book, Integer rating, String comment) {
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.comment = comment;
        this.approved = false; // Mặc định chưa duyệt
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Cập nhật thờ gian
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
