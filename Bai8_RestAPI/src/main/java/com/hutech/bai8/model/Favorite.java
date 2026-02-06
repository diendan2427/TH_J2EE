package com.hutech.bai8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "favorites")
@CompoundIndexes({
    @CompoundIndex(name = "user_book_unique", def = "{'user': 1, 'book': 1}", unique = true)
})
public class Favorite {
    @Id
    private String id;
    
    @DBRef
    private User user; // Ngườ dùng yêu thích
    
    @DBRef
    private Book book; // Sách được yêu thích
    
    private LocalDateTime createdAt; // Thờ gian thêm vào yêu thích
    
    public Favorite(User user, Book book) {
        this.user = user;
        this.book = book;
        this.createdAt = LocalDateTime.now();
    }
}
