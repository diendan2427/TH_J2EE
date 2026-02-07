package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.Category;
import com.hutech.bai8.model.User;
import com.hutech.bai8.service.BookService;
import com.hutech.bai8.service.CategoryService;
import com.hutech.bai8.service.RecommendationService;
import com.hutech.bai8.service.ReviewService;
import com.hutech.bai8.service.ViewingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final ViewingHistoryService viewingHistoryService;
    private final RecommendationService recommendationService;
    private final Cart cart;
    
    private static final int PAGE_SIZE = 9; // 9 sản phẩm/trang

    @GetMapping({"/", "/home"})
    public String home(Model model, @AuthenticationPrincipal User user) {
        List<Book> books = bookService.getAllBooks();
        List<Category> categories = categoryService.getAllCategories();
        
        // Gợi ý cá nhân hóa cho user
        List<Book> personalizedRecommendations = recommendationService.getPersonalizedRecommendations(user, 8);
        
        // Sản phẩm phổ biến
        List<Book> popularProducts = recommendationService.getPopularProducts(4);
        
        // Sản phẩm mới
        List<Book> newArrivals = recommendationService.getNewArrivals(4);
        
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("personalizedRecommendations", personalizedRecommendations);
        model.addAttribute("popularProducts", popularProducts);
        model.addAttribute("newArrivals", newArrivals);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "home";
    }

    @GetMapping("/books")
    public String listBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        
        // Xây dựng Sort
        Sort sort = Sort.by(sortBy);
        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);
        Page<Book> bookPage;
        
        // Lấy category nếu có
        Category category = null;
        if (categoryId != null && !categoryId.isEmpty()) {
            category = categoryService.getCategoryById(categoryId).orElse(null);
        }
        
        // Tìm kiếm với các bộ lọc
        if (keyword != null && !keyword.isEmpty() || minPrice != null || maxPrice != null || category != null) {
            bookPage = bookService.searchBooksWithFilters(keyword, category, minPrice, maxPrice, pageable);
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }
        
        // Lọc theo rating nếu có
        if (minRating != null && minRating > 0) {
            // TODO: Implement rating filter after review system is fully integrated
        }
        
        List<Category> categories = categoryService.getAllCategories();
        
        // Lấy khoảng giá cho slider
        BookService.PriceRange priceRange = bookService.getPriceRange();
        
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("categories", categories);
        model.addAttribute("priceRange", priceRange);
        
        // Giữ lại các giá trị filter để hiển thị
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minRating", minRating);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "books/list";
    }

    @GetMapping("/books/category/{id}")
    public String booksByCategory(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        
        Category category = categoryService.getCategoryById(id).orElse(null);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("title").ascending());
        Page<Book> bookPage;
        
        if (category != null) {
            bookPage = bookService.getBooksByCategory(category, pageable);
        } else {
            bookPage = bookService.getAllBooks(pageable);
        }
        
        List<Category> categories = categoryService.getAllCategories();
        
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "books/list";
    }

    @GetMapping("/books/detail/{id}")
    public String bookDetail(@PathVariable String id, Model model, 
                            @AuthenticationPrincipal User user) {
        Book book = bookService.getBookById(id).orElse(null);
        if (book == null) {
            return "redirect:/books";
        }
        
        // Lấy danh sách review đã duyệt
        List<com.hutech.bai8.model.Review> reviews = reviewService.getApprovedReviewsByBook(book);
        double avgRating = reviewService.getAverageRating(book);
        long reviewCount = reviewService.countReviews(book);
        
        // Kiểm tra user đã review chưa và ghi lại lịch sử xem
        boolean hasReviewed = false;
        if (user != null) {
            hasReviewed = reviewService.hasReviewed(user, book);
            viewingHistoryService.recordView(user, book);
        }
        
        // Gợi ý sản phẩm liên quan
        List<Book> relatedProducts = recommendationService.getYouMayAlsoLike(book, user, 6);
        
        model.addAttribute("book", book);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("hasReviewed", hasReviewed);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "books/detail";
    }
}
