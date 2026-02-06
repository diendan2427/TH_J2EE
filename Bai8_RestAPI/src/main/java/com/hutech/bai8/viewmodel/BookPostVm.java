package com.hutech.bai8.viewmodel;

import com.hutech.bai8.model.Book;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * Bai 8: ViewModel de nhan du lieu Book tu API (POST/PUT)
 */
public class BookPostVm {
    @NotBlank(message = "Tieu de khong duoc de trong")
    private String title;
    
    @NotBlank(message = "Tac gia khong duoc de trong")
    private String author;
    
    @Positive(message = "Gia phai la so duong")
    private Double price;
    
    private String description;
    private String imageUrl;
    private Integer stock;
    private String categoryId;

    public BookPostVm() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public Book toEntity() {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setDescription(description);
        book.setImageUrl(imageUrl);
        book.setStock(stock);
        book.setActive(true);
        return book;
    }
}
