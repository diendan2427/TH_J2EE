package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.service.BookService;
import com.hutech.bai8.viewmodel.BookGetVm;
import com.hutech.bai8.viewmodel.BookPostVm;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bài 8: RESTful API Controller
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class ApiController {
    private final BookService bookService;

    /**
     * GET /api/books - Lấy danh sách sách dưới dạng JSON
     */
    @GetMapping
    public ResponseEntity<List<BookGetVm>> getAllBooks() {
        List<BookGetVm> books = bookService.getAllBooks().stream()
                .map(BookGetVm::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    /**
     * GET /api/books/id/{id} - Lấy thông tin chi tiết sách
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<BookGetVm> getBookById(@PathVariable String id) {
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(BookGetVm.from(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/books - Thêm sách mới
     */
    @PostMapping
    public ResponseEntity<BookGetVm> createBook(@Valid @RequestBody BookPostVm bookPostVm) {
        Book book = bookPostVm.toEntity();
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookGetVm.from(savedBook));
    }

    /**
     * PUT /api/books/{id} - Cập nhật sách
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookGetVm> updateBook(@PathVariable String id, 
                                                 @Valid @RequestBody BookPostVm bookPostVm) {
        if (!bookService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Book book = bookPostVm.toEntity();
        book.setId(id);
        Book updatedBook = bookService.saveBook(book);
        return ResponseEntity.ok(BookGetVm.from(updatedBook));
    }

    /**
     * DELETE /api/books/{id} - Xóa sách thông qua API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        if (!bookService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
