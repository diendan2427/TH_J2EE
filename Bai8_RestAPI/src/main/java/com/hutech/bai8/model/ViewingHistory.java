package com.hutech.bai8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "viewing_history")
public class ViewingHistory {
    @Id
    private String id;
    
    @DBRef
    @Indexed
    private User user; // Ngườ dùng đã xem
    
    @DBRef
    private Book book; // Sách đã xem
    
    @Indexed
    private LocalDateTime viewedAt; // Thờ gian xem
    
    public ViewingHistory(User user, Book book) {
        this.user = user;
        this.book = book;
        this.viewedAt = LocalDateTime.now();
    }
}
