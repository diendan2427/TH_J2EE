package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookApiController {
    private final BookService bookService;

    // API cho autocomplete tìm kiếm
    @GetMapping("/autocomplete")
    public ResponseEntity<List<Map<String, Object>>> autocomplete(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "5") int limit) {
        
        List<Book> books = bookService.searchForAutocomplete(keyword, limit);
        
        List<Map<String, Object>> results = books.stream()
                .map(book -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", book.getId());
                    item.put("title", book.getTitle());
                    item.put("author", book.getAuthor());
                    item.put("price", book.getPrice());
                    item.put("imageUrl", book.getImageUrl());
                    return item;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(results);
    }
    
    // API lấy khoảng giá
    @GetMapping("/price-range")
    public ResponseEntity<BookService.PriceRange> getPriceRange() {
        return ResponseEntity.ok(bookService.getPriceRange());
    }
}
