package com.hutech.bai4.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Bài 4: Entity Book với MongoDB
 * Mối quan hệ @DBRef với Category (tương tự @ManyToOne trong JPA)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private Double price;
    private String description;
    private String imageUrl;
    
    @DBRef
    private Category category;
}
