package com.hutech.bai8.controller;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.Category;
import com.hutech.bai8.service.BookService;
import com.hutech.bai8.service.CategoryService;
import com.hutech.bai8.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final FileUploadService fileUploadService;
    private final Cart cart;

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/books/form";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addBook(@ModelAttribute Book book, 
                         @RequestParam(required = false) String categoryId,
                         @RequestParam(required = false) MultipartFile imageFile,
                         RedirectAttributes redirectAttributes) {
        try {
            // Upload image if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = fileUploadService.uploadFile(imageFile);
                book.setImageUrl(imageUrl);
            }
            
            if (categoryId != null && !categoryId.isEmpty()) {
                Category category = categoryService.getCategoryById(categoryId).orElse(null);
                book.setCategory(category);
            }
            book.setActive(true);
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("success", "Them sach thanh cong!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Loi upload anh: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable String id, Model model) {
        Book book = bookService.getBookById(id).orElse(null);
        if (book == null) {
            return "redirect:/admin/books";
        }
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/books/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateBook(@PathVariable String id, 
                            @ModelAttribute Book book,
                            @RequestParam(required = false) String categoryId,
                            @RequestParam(required = false) MultipartFile imageFile,
                            RedirectAttributes redirectAttributes) {
        try {
            Book existingBook = bookService.getBookById(id).orElse(null);
            book.setId(id);
            
            // Upload new image if provided
            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete old image if exists
                if (existingBook != null && existingBook.getImageUrl() != null) {
                    fileUploadService.deleteFile(existingBook.getImageUrl());
                }
                String imageUrl = fileUploadService.uploadFile(imageFile);
                book.setImageUrl(imageUrl);
            } else if (existingBook != null) {
                // Keep existing image
                book.setImageUrl(existingBook.getImageUrl());
            }
            
            if (categoryId != null && !categoryId.isEmpty()) {
                Category category = categoryService.getCategoryById(categoryId).orElse(null);
                book.setCategory(category);
            }
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("success", "Cap nhat sach thanh cong!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Loi upload anh: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBook(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Book book = bookService.getBookById(id).orElse(null);
        if (book != null && book.getImageUrl() != null) {
            fileUploadService.deleteFile(book.getImageUrl());
        }
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("success", "Xoa sach thanh cong!");
        return "redirect:/admin/books";
    }
}
