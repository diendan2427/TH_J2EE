package com.hutech.bai1.model;

/**
 * Bài 1.2.2: Quản lý sách
 * Lớp Book với các thuộc tính: id, title, author, price
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private double price;

    // Constructor mặc định
    public Book() {
    }

    // Constructor đầy đủ tham số
    public Book(int id, String title, String author, double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    // Getter và Setter (Alt+Insert trong IntelliJ)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', price=%.2f}", 
                id, title, author, price);
    }
}
