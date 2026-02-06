package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Category;
import com.hutech.bai8.model.Order;
import com.hutech.bai8.model.User;
import com.hutech.bai8.repository.BookRepository;
import com.hutech.bai8.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final ViewingHistoryService viewingHistoryService;
    private final FavoriteService favoriteService;

    // Gợi ý sản phẩm cùng danh mục
    public List<Book> getRelatedProducts(Book currentBook, int limit) {
        if (currentBook.getCategory() == null) {
            return bookRepository.findAll(PageRequest.of(0, limit)).getContent();
        }
        
        List<Book> relatedBooks = bookRepository.findByCategory(currentBook.getCategory());
        
        // Loại bỏ sách hiện tại và trả về số lượng cần thiết
        return relatedBooks.stream()
                .filter(book -> !book.getId().equals(currentBook.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Gợi ý sản phẩm dựa trên lịch sử xem của user
    public List<Book> getPersonalizedRecommendations(User user, int limit) {
        if (user == null) {
            return getPopularProducts(limit);
        }
        
        // Lấy danh sách sách user đã xem
        List<Book> viewedBooks = viewingHistoryService.getViewedBooks(user, 10);
        
        if (viewedBooks.isEmpty()) {
            return getPopularProducts(limit);
        }
        
        // Lấy các danh mục user đã quan tâm
        Set<Category> interestedCategories = viewedBooks.stream()
                .map(Book::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        // Tìm sách trong các danh mục đó mà user chưa xem
        List<Book> recommendations = new ArrayList<>();
        Set<String> viewedBookIds = viewedBooks.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
        
        for (Category category : interestedCategories) {
            List<Book> booksInCategory = bookRepository.findByCategory(category);
            for (Book book : booksInCategory) {
                if (!viewedBookIds.contains(book.getId()) && !recommendations.contains(book)) {
                    recommendations.add(book);
                    if (recommendations.size() >= limit) {
                        break;
                    }
                }
            }
            if (recommendations.size() >= limit) {
                break;
            }
        }
        
        // Nếu chưa đủ, thêm sản phẩm phổ biến
        if (recommendations.size() < limit) {
            List<Book> popularProducts = getPopularProducts(limit);
            for (Book book : popularProducts) {
                if (!viewedBookIds.contains(book.getId()) && !recommendations.contains(book)) {
                    recommendations.add(book);
                    if (recommendations.size() >= limit) {
                        break;
                    }
                }
            }
        }
        
        return recommendations;
    }

    // Sản phẩm bán chạy (dựa trên đơn hàng)
    public List<Book> getPopularProducts(int limit) {
        List<Order> allOrders = orderRepository.findAll();
        
        Map<String, Integer> bookSalesCount = new HashMap<>();
        
        for (Order order : allOrders) {
            if (order.getStatus().equals("CONFIRMED") || order.getStatus().equals("DELIVERED")) {
                order.getItems().forEach(item -> {
                    bookSalesCount.merge(item.getBookId(), item.getQuantity(), Integer::sum);
                });
            }
        }
        
        // Sắp xếp theo số lượng bán và lấy top
        List<String> topBookIds = bookSalesCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        
        List<Book> popularBooks = new ArrayList<>();
        for (String bookId : topBookIds) {
            bookRepository.findById(bookId).ifPresent(popularBooks::add);
        }
        
        // Nếu chưa đủ, thêm sản phẩm mới nhất
        if (popularBooks.size() < limit) {
            List<Book> allBooks = bookRepository.findAll();
            for (Book book : allBooks) {
                if (!popularBooks.contains(book)) {
                    popularBooks.add(book);
                    if (popularBooks.size() >= limit) {
                        break;
                    }
                }
            }
        }
        
        return popularBooks;
    }

    // Gợi ý sản phẩm "Có thể bạn cũng thích" (cho trang chi tiết sản phẩm)
    public List<Book> getYouMayAlsoLike(Book currentBook, User user, int limit) {
        List<Book> recommendations = new ArrayList<>();
        
        // 1. Sản phẩm cùng danh mục
        recommendations.addAll(getRelatedProducts(currentBook, limit / 2));
        
        // 2. Sản phẩm user đã xem nhưng chưa mua
        if (user != null) {
            List<Book> viewedBooks = viewingHistoryService.getViewedBooks(user, 10);
            Set<String> existingIds = recommendations.stream()
                    .map(Book::getId)
                    .collect(Collectors.toSet());
            existingIds.add(currentBook.getId());
            
            for (Book book : viewedBooks) {
                if (!existingIds.contains(book.getId())) {
                    recommendations.add(book);
                    existingIds.add(book.getId());
                    if (recommendations.size() >= limit) {
                        break;
                    }
                }
            }
        }
        
        // 3. Nếu chưa đủ, thêm sản phẩm phổ biến
        if (recommendations.size() < limit) {
            List<Book> popular = getPopularProducts(limit);
            Set<String> existingIds = recommendations.stream()
                    .map(Book::getId)
                    .collect(Collectors.toSet());
            existingIds.add(currentBook.getId());
            
            for (Book book : popular) {
                if (!existingIds.contains(book.getId())) {
                    recommendations.add(book);
                    if (recommendations.size() >= limit) {
                        break;
                    }
                }
            }
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }

    // Sản phẩm mới nhất
    public List<Book> getNewArrivals(int limit) {
        return bookRepository.findAll(PageRequest.of(0, limit)).getContent();
    }
}
