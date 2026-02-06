package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Category;
import com.hutech.bai8.repository.BookRepository;
import com.hutech.bai8.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(String id) {
        // Trước khi xóa danh mục, set category = null cho tất cả sách thuộc danh mục này
        // Để đảm bảo sản phẩm không bị mất khi xóa danh mục
        List<Book> booksInCategory = bookRepository.findByCategoryId(id);
        for (Book book : booksInCategory) {
            book.setCategory(null);
            bookRepository.save(book);
        }
        
        // Sau đó mới xóa danh mục
        categoryRepository.deleteById(id);
    }
}
