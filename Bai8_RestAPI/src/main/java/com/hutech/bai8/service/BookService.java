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
    
    // Tìm kiếm nâng cao với lọc giá
    public Page<Book> searchBooksWithFilters(String keyword, Category category, Double minPrice, 
                                             Double maxPrice, Pageable pageable) {
        // Xử lý giá mặc định
        double min = (minPrice != null) ? minPrice : 0;
        double max = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;
        
        // Nếu có keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            return bookRepository.searchByKeywordAndPriceBetween(keyword.trim(), min, max, pageable);
        }
        
        // Nếu có category
        if (category != null) {
            return bookRepository.findByCategoryAndPriceBetween(category, min, max, pageable);
        }
        
        // Chỉ lọc theo giá
        return bookRepository.findByPriceBetween(min, max, pageable);
    }
    
    // Lọc theo khoảng giá
    public Page<Book> getBooksByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        double min = (minPrice != null) ? minPrice : 0;
        double max = (maxPrice != null) ? maxPrice : Double.MAX_VALUE;
        return bookRepository.findByPriceBetween(min, max, pageable);
    }
    
    // Lấy khoảng giá min max của tất cả sách
    public PriceRange getPriceRange() {
        List<Book> allBooks = bookRepository.findAll();
        if (allBooks.isEmpty()) {
            return new PriceRange(0, 1000000);
        }
        
        double min = allBooks.stream().mapToDouble(Book::getPrice).min().orElse(0);
        double max = allBooks.stream().mapToDouble(Book::getPrice).max().orElse(1000000);
        
        // Làm tròn lên cho đẹp
        min = Math.floor(min / 10000) * 10000;
        max = Math.ceil(max / 10000) * 10000;
        
        return new PriceRange(min, max);
    }
    
    // Tìm kiếm autocomplete
    public List<Book> searchForAutocomplete(String keyword, int limit) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        Page<Book> results = bookRepository.searchByKeyword(keyword.trim(), 
            Pageable.ofSize(limit));
        return results.getContent();
    }
    
    // DTO cho khoảng giá
    public static class PriceRange {
        private final double minPrice;
        private final double maxPrice;
        
        public PriceRange(double minPrice, double maxPrice) {
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }
        
        public double getMinPrice() { return minPrice; }
        public double getMaxPrice() { return maxPrice; }
    }
}
