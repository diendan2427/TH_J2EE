package com.hutech.bai7;

import com.hutech.bai7.model.Book;
import com.hutech.bai7.model.Role;
import com.hutech.bai7.model.User;
import com.hutech.bai7.repository.BookRepository;
import com.hutech.bai7.repository.RoleRepository;
import com.hutech.bai7.repository.UserRepository;
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
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Tạo roles nếu chưa có
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN", "Quản trị viên")));
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER", "Người dùng thường")));

        // Tạo admin mặc định nếu chưa có
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@hutech.edu.vn");
            admin.setFullName("Administrator");
            admin.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            System.out.println("Created admin user: admin/admin123");
        }

        // Tạo user mặc định nếu chưa có
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@hutech.edu.vn");
            user.setFullName("Normal User");
            user.setEnabled(true);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);
            System.out.println("Created normal user: user/user123");
        }

        // Tạo sách mẫu nếu chưa có
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book(null, "Lập trình Java", "Nguyễn Văn A", 150000.0, "Sách Java cơ bản"));
            bookRepository.save(new Book(null, "Spring Boot", "Trần Văn B", 200000.0, "Xây dựng web với Spring"));
            bookRepository.save(new Book(null, "MongoDB", "Lê Văn C", 180000.0, "NoSQL Database"));
            System.out.println("Created sample books");
        }
    }
}
