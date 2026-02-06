package com.hutech.bai8.viewmodel;

import com.hutech.bai8.model.Book;

/**
 * Bai 8: ViewModel de tra ve du lieu Book qua API
 */
public class BookGetVm {
    private String id;
    private String title;
    private String author;
    private Double price;
    private String description;
    private String imageUrl;
    private Integer stock;
    private String categoryName;

    public BookGetVm() {}

    public BookGetVm(String id, String title, String author, Double price, String description, 
                     String imageUrl, Integer stock, String categoryName) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
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

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public static BookGetVm from(Book book) {
        return new BookGetVm(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getPrice(),
            book.getDescription(),
            book.getImageUrl(),
            book.getStock(),
            book.getCategory() != null ? book.getCategory().getName() : null
        );
    }
}
