package com.hutech.bai5.controller;

import com.hutech.bai5.model.Book;
import com.hutech.bai5.model.Category;
import com.hutech.bai5.repository.CategoryRepository;
import com.hutech.bai5.service.BookService;
import com.hutech.bai5.service.CartService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CategoryRepository categoryRepository;
    private final CartService cartService;

    @GetMapping
    public String listBooks(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("books", bookService.searchByKeyword(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        model.addAttribute("cartCount", cartService.getTotalItems());
        return "books/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "books/form";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute Book book, 
                         BindingResult result,
                         @RequestParam String categoryId,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "books/form";
        }
        categoryRepository.findById(categoryId).ifPresent(book::setCategory);
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    model.addAttribute("categories", categoryRepository.findAll());
                    return "books/form";
                })
                .orElse("redirect:/books");
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable String id,
                            @Valid @ModelAttribute Book book,
                            BindingResult result,
                            @RequestParam String categoryId,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "books/form";
        }
        book.setId(id);
        categoryRepository.findById(categoryId).ifPresent(book::setCategory);
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
