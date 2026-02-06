package com.hutech.bai1.app;

import com.hutech.bai1.model.Book;
import com.hutech.bai1.service.BookService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Bài 1.2.2: Ứng dụng quản lý sách với Menu
 */
public class BookManagementApp {
    private static BookService bookService = new BookService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Thêm dữ liệu mẫu
        initSampleData();

        int choice;
        do {
            showMenu();
            choice = getIntInput("Chọn chức năng: ");
            handleChoice(choice);
        } while (choice != 0);

        System.out.println("Cảm ơn bạn đã sử dụng chương trình!");
        scanner.close();
    }

    private static void initSampleData() {
        bookService.addBook("Lập trình Java cơ bản", "Nguyễn Văn A", 150000);
        bookService.addBook("Lập trình Web với Spring Boot", "Trần Văn B", 200000);
        bookService.addBook("Cấu trúc dữ liệu và giải thuật", "Lê Văn C", 180000);
        bookService.addBook("Lập trình Python", "Phạm Văn D", 120000);
    }

    private static void showMenu() {
        System.out.println("\n========== QUẢN LÝ SÁCH ==========");
        System.out.println("1. Thêm một cuốn sách");
        System.out.println("2. Xóa một cuốn sách theo Mã Sách");
        System.out.println("3. Thay đổi thông tin sách");
        System.out.println("4. Xuất danh sách tất cả các sách");
        System.out.println("5. Tìm sách có tên chứa từ 'Lập trình'");
        System.out.println("6. Lấy K cuốn sách có giá <= P");
        System.out.println("7. Tìm kiếm sách theo danh sách tác giả");
        System.out.println("0. Thoát");
        System.out.println("===================================");
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1: addBook(); break;
            case 2: deleteBook(); break;
            case 3: updateBook(); break;
            case 4: bookService.showAllBooks(); break;
            case 5: findBooksContainingLapTrinh(); break;
            case 6: getBooksByPriceLimit(); break;
            case 7: findBooksByAuthors(); break;
            case 0: break;
            default: System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    private static void addBook() {
        System.out.println("\n--- Thêm sách mới ---");
        String title = getStringInput("Nhập tên sách: ");
        String author = getStringInput("Nhập tác giả: ");
        double price = getDoubleInput("Nhập giá: ");
        bookService.addBook(title, author, price);
    }

    private static void deleteBook() {
        System.out.println("\n--- Xóa sách ---");
        int id = getIntInput("Nhập Mã Sách cần xóa: ");
        bookService.deleteBook(id);
    }

    private static void updateBook() {
        System.out.println("\n--- Cập nhật sách ---");
        int id = getIntInput("Nhập Mã Sách cần cập nhật: ");
        if (bookService.getBookById(id).isEmpty()) {
            System.out.println("Không tìm thấy sách có ID: " + id);
            return;
        }
        String title = getStringInput("Nhập tên sách mới: ");
        String author = getStringInput("Nhập tác giả mới: ");
        double price = getDoubleInput("Nhập giá mới: ");
        bookService.updateBook(id, title, author, price);
    }

    private static void findBooksContainingLapTrinh() {
        System.out.println("\n--- Sách có tên chứa 'Lập trình' ---");
        List<Book> result = bookService.findBooksContainingLapTrinh();
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy sách nào!");
        } else {
            result.forEach(System.out::println);
        }
    }

    private static void getBooksByPriceLimit() {
        System.out.println("\n--- Lấy K cuốn sách có giá <= P ---");
        double maxPrice = getDoubleInput("Nhập giá tối đa (P): ");
        int limit = getIntInput("Nhập số lượng tối đa (K): ");
        List<Book> result = bookService.getBooksByPriceLimit(maxPrice, limit);
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy sách nào!");
        } else {
            result.forEach(System.out::println);
        }
    }

    private static void findBooksByAuthors() {
        System.out.println("\n--- Tìm sách theo tác giả ---");
        String input = getStringInput("Nhập danh sách tác giả (cách nhau bởi dấu phẩy): ");
        List<String> authors = Arrays.asList(input.split(","));
        List<Book> result = bookService.findBooksByAuthors(authors);
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy sách nào!");
        } else {
            result.forEach(System.out::println);
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số thực hợp lệ!");
            }
        }
    }
}
