package com.hutech.bai6;

import com.hutech.bai6.model.Book;
import com.hutech.bai6.model.User;
import com.hutech.bai6.repository.BookRepository;
import com.hutech.bai6.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Bai 6: Tao du lieu mau khi khoi dong ung dung
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(BookRepository bookRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Tao user mau neu chua co
        if (userRepository.count() == 0) {
            System.out.println("Dang tao user mau...");
            
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setFullName("Nguoi dung");
            user.setRole("USER");
            user.setEnabled(true);
            userRepository.save(user);
            
            System.out.println("Da tao user mau: user / user123");
        }
        
        // Tao books mau neu chua co
        if (bookRepository.count() == 0) {
            System.out.println("Dang tao sach mau...");
            
            bookRepository.save(new Book(null, "Lap trinh Java co ban", "Nguyen Van A", 150000.0, "Huong dan lap trinh Java"));
            bookRepository.save(new Book(null, "Spring Boot thuc chien", "Tran Van B", 200000.0, "Xay dung ung dung web"));
            bookRepository.save(new Book(null, "Python cho nguoi moi", "Le Van C", 120000.0, "Hoc Python tu dau"));
            bookRepository.save(new Book(null, "JavaScript hien dai", "Pham Van D", 180000.0, "ES6+ va cac tinh nang moi"));
            bookRepository.save(new Book(null, "Kinh te hoc vi mo", "Hoang Van E", 95000.0, "Nguyen ly kinh te"));
            
            System.out.println("Da tao " + bookRepository.count() + " sach mau!");
        }
        
        System.out.println("=== Du lieu da san sang ===");
        System.out.println("Users: " + userRepository.count());
        System.out.println("Books: " + bookRepository.count());
    }
}
