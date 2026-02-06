package com.hutech.bai5;

import com.hutech.bai5.model.Book;
import com.hutech.bai5.model.Category;
import com.hutech.bai5.repository.BookRepository;
import com.hutech.bai5.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Bai 5: Tao du lieu mau khi khoi dong ung dung
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Chi tao du lieu mau neu chua co
        if (categoryRepository.count() == 0) {
            System.out.println("Dang tao du lieu mau...");
            
            // Tao categories
            Category catLapTrinh = new Category(null, "Lap trinh", "Sach ve lap trinh");
            Category catKinhTe = new Category(null, "Kinh te", "Sach ve kinh te, kinh doanh");
            Category catVanHoc = new Category(null, "Van hoc", "Sach van hoc Viet Nam va nuoc ngoai");
            Category catKhoaHoc = new Category(null, "Khoa hoc", "Sach khoa hoc tu nhien");
            
            catLapTrinh = categoryRepository.save(catLapTrinh);
            catKinhTe = categoryRepository.save(catKinhTe);
            catVanHoc = categoryRepository.save(catVanHoc);
            catKhoaHoc = categoryRepository.save(catKhoaHoc);
            
            // Tao books
            bookRepository.save(createBook("Lap trinh Java co ban", "Nguyen Van A", 150000.0, 
                "Huong dan lap trinh Java tu co ban den nang cao", catLapTrinh));
            bookRepository.save(createBook("Spring Boot thuc chien", "Tran Van B", 200000.0, 
                "Xay dung ung dung web voi Spring Boot", catLapTrinh));
            bookRepository.save(createBook("Python cho nguoi moi bat dau", "Le Van C", 120000.0, 
                "Hoc Python tu dau", catLapTrinh));
            bookRepository.save(createBook("JavaScript hien dai", "Pham Van D", 180000.0, 
                "ES6+ va cac tinh nang moi", catLapTrinh));
            
            bookRepository.save(createBook("Kinh te hoc vi mo", "Hoang Van E", 95000.0, 
                "Nguyen ly kinh te hoc vi mo", catKinhTe));
            bookRepository.save(createBook("Khoi nghiep tinh gon", "Eric Ries", 160000.0, 
                "Phuong phap khoi nghiep hieu qua", catKinhTe));
            
            bookRepository.save(createBook("Truyen Kieu", "Nguyen Du", 85000.0, 
                "Tac pham van hoc kinh dien Viet Nam", catVanHoc));
            bookRepository.save(createBook("Nha gia kim", "Paulo Coelho", 110000.0, 
                "Tieu thuyet noi tieng the gioi", catVanHoc));
            
            bookRepository.save(createBook("Vu tru trong vo hat", "Stephen Hawking", 175000.0, 
                "Kham pha bi an vu tru", catKhoaHoc));
            bookRepository.save(createBook("Luoc su thoi gian", "Stephen Hawking", 145000.0, 
                "Lich su vu tru tu Big Bang", catKhoaHoc));
            
            System.out.println("Da tao xong du lieu mau!");
            System.out.println("- Categories: " + categoryRepository.count());
            System.out.println("- Books: " + bookRepository.count());
        } else {
            System.out.println("Du lieu da ton tai, khong can tao moi.");
        }
    }
    
    private Book createBook(String title, String author, Double price, String description, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setDescription(description);
        book.setCategory(category);
        book.setImageUrl("/images/default-book.png");
        return book;
    }
}
