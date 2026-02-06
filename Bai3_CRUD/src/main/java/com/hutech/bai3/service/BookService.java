package com.hutech.bai3.service;

import com.hutech.bai3.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Bài 3: Service quản lý sách (lưu tạm trong bộ nhớ)
 */
@Service
public class BookService {
    private final List<Book> books = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public BookService() {
        // Dữ liệu mẫu
        books.add(new Book(nextId.getAndIncrement(), "Lập trình Java cơ bản", "Nguyễn Văn A", 150000.0, "Sách học Java cho người mới bắt đầu"));
        books.add(new Book(nextId.getAndIncrement(), "Spring Boot thực chiến", "Trần Văn B", 200000.0, "Xây dựng ứng dụng web với Spring Boot"));
        books.add(new Book(nextId.getAndIncrement(), "Cấu trúc dữ liệu và giải thuật", "Lê Văn C", 180000.0, "Kiến thức nền tảng về CTDL"));
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public Book addBook(Book book) {
        book.setId(nextId.getAndIncrement());
        books.add(book);
        return book;
    }

    public Book updateBook(Long id, Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                updatedBook.setId(id);
                books.set(i, updatedBook);
                return updatedBook;
            }
        }
        return null;
    }

    public boolean deleteBook(Long id) {
        return books.removeIf(book -> book.getId().equals(id));
    }
}
