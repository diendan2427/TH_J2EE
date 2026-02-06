package com.hutech.bai8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private String id;
    
    @NotBlank(message = "Tieu de khong duoc de trong")
    private String title;
    
    @NotBlank(message = "Tac gia khong duoc de trong")
    private String author;
    
    @Positive(message = "Gia phai la so duong")
    private Double price;
    
    private String description;
    private String imageUrl;
    private Integer stock;
    private boolean active = true;
    
    @DBRef
    private Category category;
}
