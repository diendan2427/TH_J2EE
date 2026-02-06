package com.hutech.bai1.service;

import com.hutech.bai1.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Bài 1.2.2: Service quản lý sách với Stream API
 */
public class BookService {
    private List<Book> books = new ArrayList<>();
    private int nextId = 1;

    // Thêm một cuốn sách
    public void addBook(String title, String author, double price) {
        Book book = new Book(nextId++, title, author, price);
        books.add(book);
        System.out.println("Đã thêm sách: " + book);
    }

    // Xóa sách theo Mã Sách (sử dụng Stream API)
    public boolean deleteBook(int id) {
        Optional<Book> bookToRemove = books.stream()
                .filter(book -> book.getId() == id)
                .findFirst();
        
        if (bookToRemove.isPresent()) {
            books.remove(bookToRemove.get());
            System.out.println("Đã xóa sách có ID: " + id);
            return true;
        }
        System.out.println("Không tìm thấy sách có ID: " + id);
        return false;
    }

    // Thay đổi thông tin sách
    public boolean updateBook(int id, String title, String author, double price) {
        Optional<Book> bookToUpdate = books.stream()
                .filter(book -> book.getId() == id)
                .findFirst();
        
        if (bookToUpdate.isPresent()) {
            Book book = bookToUpdate.get();
            book.setTitle(title);
            book.setAuthor(author);
            book.setPrice(price);
            System.out.println("Đã cập nhật sách: " + book);
            return true;
        }
        System.out.println("Không tìm thấy sách có ID: " + id);
        return false;
    }

    // Xuất danh sách sách (sử dụng forEach và Method Reference)
    public void showAllBooks() {
        System.out.println("\n=== DANH SÁCH SÁCH ===");
        if (books.isEmpty()) {
            System.out.println("Danh sách trống!");
            return;
        }
        books.forEach(System.out::println);
    }

    // Tìm sách có tên chứa từ "Lập trình" (không phân biệt hoa thường)
    public List<Book> findBooksContainingLapTrinh() {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains("lập trình"))
                .collect(Collectors.toList());
    }

    // Lấy tối đa K cuốn sách có giá tiền <= P (sử dụng filter và limit)
    public List<Book> getBooksByPriceLimit(double maxPrice, int limit) {
        return books.stream()
                .filter(book -> book.getPrice() <= maxPrice)
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Tìm kiếm sách theo danh sách tác giả
    public List<Book> findBooksByAuthors(List<String> authors) {
        return books.stream()
                .filter(book -> authors.stream()
                        .anyMatch(author -> book.getAuthor().toLowerCase()
                                .contains(author.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Lấy sách theo ID
    public Optional<Book> getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst();
    }
}
