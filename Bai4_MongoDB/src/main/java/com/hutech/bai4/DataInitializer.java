package com.hutech.bai4;

import com.hutech.bai4.model.Book;
import com.hutech.bai4.model.Category;
import com.hutech.bai4.repository.BookRepository;
import com.hutech.bai4.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Khởi tạo dữ liệu mẫu khi ứng dụng khởi động
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        // Chỉ khởi tạo nếu chưa có dữ liệu
        if (categoryRepository.count() == 0) {
            Category programming = categoryRepository.save(new Category(null, "Lập trình", "Sách về lập trình và công nghệ"));
            Category database = categoryRepository.save(new Category(null, "Cơ sở dữ liệu", "Sách về CSDL và quản trị dữ liệu"));
            Category algorithm = categoryRepository.save(new Category(null, "Giải thuật", "Sách về thuật toán và CTDL"));

            bookRepository.save(new Book(null, "Lập trình Java cơ bản", "Nguyễn Văn A", 150000.0, "Sách học Java cho người mới", null, programming));
            bookRepository.save(new Book(null, "Spring Boot thực chiến", "Trần Văn B", 200000.0, "Xây dựng ứng dụng web với Spring Boot", null, programming));
            bookRepository.save(new Book(null, "MongoDB cơ bản", "Lê Văn C", 180000.0, "Học MongoDB từ đầu", null, database));
            bookRepository.save(new Book(null, "Cấu trúc dữ liệu", "Phạm Văn D", 160000.0, "CTDL và giải thuật", null, algorithm));

            System.out.println("Đã khởi tạo dữ liệu mẫu thành công!");
        }
    }
}
