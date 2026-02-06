package com.hutech.bai4.controller;

import com.hutech.bai4.model.Book;
import com.hutech.bai4.service.BookService;
import com.hutech.bai4.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            Model model) {
        
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("books", bookService.searchByKeyword(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            Page<Book> bookPage = bookService.getAllBooks(PageRequest.of(page, size));
            model.addAttribute("books", bookPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", bookPage.getTotalPages());
        }
        
        return "books/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "books/form";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, @RequestParam String categoryId) {
        categoryService.getCategoryById(categoryId).ifPresent(book::setCategory);
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    model.addAttribute("categories", categoryService.getAllCategories());
                    return "books/form";
                })
                .orElse("redirect:/books");
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable String id, @ModelAttribute Book book, @RequestParam String categoryId) {
        book.setId(id);
        categoryService.getCategoryById(categoryId).ifPresent(book::setCategory);
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
