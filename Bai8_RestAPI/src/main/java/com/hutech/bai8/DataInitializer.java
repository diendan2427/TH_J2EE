package com.hutech.bai8;

import com.hutech.bai8.model.*;
import com.hutech.bai8.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("=== Khởi tạo dữ liệu mẫu ===");
        
        // Tạo roles
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN", "Quản trị viên")));
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER", "Người dùng")));
        System.out.println("Roles: OK");

        // Tạo admin
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@bookstore.com");
            admin.setFullName("Quản trị viên");
            admin.setPhone("0123456789");
            admin.setAddress("123 Đường ABC, Quận 1, TP.HCM");
            admin.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            System.out.println("Admin: admin / admin123");
        }

        // Tạo user
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@bookstore.com");
            user.setFullName("Nguyễn Văn A");
            user.setPhone("0987654321");
            user.setAddress("456 Đường XYZ, Quận 2, TP.HCM");
            user.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);
            System.out.println("User: user / user123");
        }

        // Tạo categories
        if (categoryRepository.count() == 0) {
            Category catLapTrinh = categoryRepository.save(
                new Category(null, "Lập trình", "Sách về lập trình và công nghệ thông tin", "bi-code-slash"));
            Category catKinhTe = categoryRepository.save(
                new Category(null, "Kinh tế", "Sách về kinh tế và kinh doanh", "bi-graph-up"));
            Category catVanHoc = categoryRepository.save(
                new Category(null, "Văn học", "Sách văn học trong nước và quốc tế", "bi-book"));
            Category catKhoaHoc = categoryRepository.save(
                new Category(null, "Khoa học", "Sách khoa học tự nhiên và ứng dụng", "bi-lightbulb"));
            Category catKyNang = categoryRepository.save(
                new Category(null, "Kỹ năng sống", "Sách phát triển bản thân", "bi-person-check"));
            System.out.println("Categories: " + categoryRepository.count());

            // Tạo books
            // Lập trình
            bookRepository.save(createBook("Lập trình Java cơ bản", "Nguyễn Văn A", 150000.0, 
                "Hướng dẫn lập trình Java từ cơ bản đến nâng cao với nhiều ví dụ thực tế. Phù hợp cho người mới bắt đầu.", 
                "https://picsum.photos/seed/java/300/400", 50, catLapTrinh));
            bookRepository.save(createBook("Spring Boot thực chiến", "Trần Văn B", 200000.0, 
                "Xây dựng ứng dụng web chuyên nghiệp với Spring Boot và MongoDB. Bao gồm REST API, Security, và nhiều hơn nữa.", 
                "https://picsum.photos/seed/spring/300/400", 30, catLapTrinh));
            bookRepository.save(createBook("Python cho người mới", "Lê Văn C", 120000.0, 
                "Học Python từ con số 0, phù hợp cho người mới bắt đầu. Bao gồm các bài tập thực hành.", 
                "https://picsum.photos/seed/python/300/400", 45, catLapTrinh));
            bookRepository.save(createBook("JavaScript hiện đại", "Phạm Văn D", 180000.0, 
                "ES6+, React, Node.js và các framework JavaScript phổ biến. Cập nhật xu hướng mới nhất.", 
                "https://picsum.photos/seed/js/300/400", 25, catLapTrinh));
            
            // Kinh tế
            bookRepository.save(createBook("Kinh tế học vi mô", "Hoàng Văn E", 95000.0, 
                "Nguyên lý kinh tế học vi mô cho sinh viên và người đi làm. Lý thuyết kết hợp thực tiễn.", 
                "https://picsum.photos/seed/economics/300/400", 40, catKinhTe));
            bookRepository.save(createBook("Khởi nghiệp tinh gọn", "Eric Ries", 160000.0, 
                "Phương pháp khởi nghiệp hiệu quả trong thời đại số. Bestseller toàn cầu.", 
                "https://picsum.photos/seed/startup/300/400", 35, catKinhTe));
            bookRepository.save(createBook("Từ tốt đến vĩ đại", "Jim Collins", 185000.0, 
                "Những bí quyết để đưa doanh nghiệp lên tầm cao mới. Nghiên cứu từ hàng trăm công ty.", 
                "https://picsum.photos/seed/business/300/400", 20, catKinhTe));
            
            // Văn học
            bookRepository.save(createBook("Truyện Kiều", "Nguyễn Du", 85000.0, 
                "Tác phẩm văn học kinh điển của Việt Nam. Bản dịch và chú giải đầy đủ.", 
                "https://picsum.photos/seed/kieu/300/400", 60, catVanHoc));
            bookRepository.save(createBook("Nhà giả kim", "Paulo Coelho", 110000.0, 
                "Tiểu thuyết best-seller thế giới về hành trình theo đuổi ước mơ. Dịch bởi nhiều ngôn ngữ.", 
                "https://picsum.photos/seed/alchemist/300/400", 55, catVanHoc));
            bookRepository.save(createBook("Đắc nhân tâm", "Dale Carnegie", 125000.0, 
                "Nghệ thuật đối nhân xử thế và thành công. Cuốn sách self-help bán chạy nhất mọi thời đại.", 
                "https://picsum.photos/seed/carnegie/300/400", 70, catVanHoc));
            
            // Khoa học
            bookRepository.save(createBook("Vũ trụ trong vỏ hạt", "Stephen Hawking", 175000.0, 
                "Khám phá bí ẩn của vũ trụ qua góc nhìn của thiên tài vật lý. Minh họa sinh động.", 
                "https://picsum.photos/seed/universe/300/400", 15, catKhoaHoc));
            bookRepository.save(createBook("Lược sử thời gian", "Stephen Hawking", 145000.0, 
                "Lịch sử vũ trụ từ Big Bang đến hố đen. Được viết cho độc giả phổ thông.", 
                "https://picsum.photos/seed/time/300/400", 22, catKhoaHoc));
            
            // Kỹ năng sống
            bookRepository.save(createBook("7 thói quen thành đạt", "Stephen Covey", 155000.0, 
                "Những thói quen giúp bạn thành công trong cuộc sống. Đã bán hơn 40 triệu bản.", 
                "https://picsum.photos/seed/habits/300/400", 40, catKyNang));
            bookRepository.save(createBook("Nghĩ giàu làm giàu", "Napoleon Hill", 135000.0, 
                "Bí quyết làm giàu từ tư duy và hành động. Cuốn sách kinh điển về tài chính cá nhân.", 
                "https://picsum.photos/seed/rich/300/400", 38, catKyNang));
            
            System.out.println("Books: " + bookRepository.count());
        }
        
        System.out.println("=== Khởi tạo hoàn tất ===");
    }

    private Book createBook(String title, String author, Double price, String description, 
                           String imageUrl, Integer stock, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setDescription(description);
        book.setImageUrl(imageUrl);
        book.setStock(stock);
        book.setCategory(category);
        book.setActive(true);
        return book;
    }
}
