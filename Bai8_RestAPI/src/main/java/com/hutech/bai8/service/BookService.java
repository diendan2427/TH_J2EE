package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Category;
import com.hutech.bai8.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Phân trang - lấy tất cả sách
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public List<Book> getActiveBooks() {
        return bookRepository.findByActiveTrue();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return bookRepository.existsById(id);
    }

    public List<Book> getBooksByCategory(Category category) {
        return bookRepository.findByCategory(category);
    }

    // Phân trang - lấy sách theo danh mục
    public Page<Book> getBooksByCategory(Category category, Pageable pageable) {
        return bookRepository.findByCategory(category, pageable);
    }

    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.searchByKeyword(keyword.trim());
    }

    // Phân trang - tìm kiếm sách
    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks(pageable);
        }
        return bookRepository.searchByKeyword(keyword.trim(), pageable);
    }
}
