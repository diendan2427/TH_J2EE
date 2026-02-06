import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * BÀI 1: LẬP TRÌNH JAVA CORE
 * Chạy trực tiếp từ VS Code
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       BÀI 1: LẬP TRÌNH JAVA CORE         ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Bài 1.2.1 - Hello World              ║");
        System.out.println("║  2. Bài 1.2.2 - Quản lý sách             ║");
        System.out.println("║  3. Bài 1.2.3 - Kiểm định xe             ║");
        System.out.println("║  0. Thoát                                ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        System.out.print("Chọn bài tập: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        switch (choice) {
            case 1:
                System.out.println("\n=== BÀI 1.2.1: HELLO WORLD ===");
                System.out.println("Hello and welcome!");
                break;
            case 2:
                System.out.println("\n=== BÀI 1.2.2: QUẢN LÝ SÁCH ===");
                runBookManagement(scanner);
                break;
            case 3:
                System.out.println("\n=== BÀI 1.2.3: KIỂM ĐỊNH XE ===");
                runVehicleInspection(scanner);
                break;
            case 0:
                System.out.println("Tạm biệt!");
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
        
        scanner.close();
    }

    // ==================== BÀI 1.2.2: QUẢN LÝ SÁCH ====================
    
    static class Book {
        private int id;
        private String title;
        private String author;
        private double price;

        public Book(int id, String title, String author, double price) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.price = price;
        }

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public double getPrice() { return price; }
        
        public void setTitle(String title) { this.title = title; }
        public void setAuthor(String author) { this.author = author; }
        public void setPrice(double price) { this.price = price; }

        @Override
        public String toString() {
            return String.format("Book{id=%d, title='%s', author='%s', price=%.0f VNĐ}", 
                    id, title, author, price);
        }
    }

    static List<Book> books = new ArrayList<>();
    static int nextBookId = 1;

    static void runBookManagement(Scanner scanner) {
        // Thêm dữ liệu mẫu
        books.add(new Book(nextBookId++, "Lập trình Java cơ bản", "Nguyễn Văn A", 150000));
        books.add(new Book(nextBookId++, "Lập trình Web với Spring Boot", "Trần Văn B", 200000));
        books.add(new Book(nextBookId++, "Cấu trúc dữ liệu và giải thuật", "Lê Văn C", 180000));
        books.add(new Book(nextBookId++, "Lập trình Python", "Phạm Văn D", 120000));

        int choice;
        do {
            System.out.println("\n========== QUẢN LÝ SÁCH ==========");
            System.out.println("1. Thêm một cuốn sách");
            System.out.println("2. Xóa một cuốn sách theo Mã Sách");
            System.out.println("3. Thay đổi thông tin sách");
            System.out.println("4. Xuất danh sách tất cả các sách");
            System.out.println("5. Tìm sách có tên chứa từ 'Lập trình'");
            System.out.println("6. Lấy K cuốn sách có giá <= P");
            System.out.println("7. Tìm kiếm sách theo danh sách tác giả");
            System.out.println("0. Quay lại menu chính");
            System.out.println("===================================");
            
            System.out.print("Chọn chức năng: ");
            choice = getIntInput(scanner);

            switch (choice) {
                case 1: addBook(scanner); break;
                case 2: deleteBook(scanner); break;
                case 3: updateBook(scanner); break;
                case 4: showAllBooks(); break;
                case 5: findBooksContainingLapTrinh(); break;
                case 6: getBooksByPriceLimit(scanner); break;
                case 7: findBooksByAuthors(scanner); break;
                case 0: break;
                default: System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }

    static void addBook(Scanner scanner) {
        System.out.println("\n--- Thêm sách mới ---");
        System.out.print("Nhập tên sách: ");
        String title = scanner.nextLine();
        System.out.print("Nhập tác giả: ");
        String author = scanner.nextLine();
        System.out.print("Nhập giá: ");
        double price = getDoubleInput(scanner);
        
        Book book = new Book(nextBookId++, title, author, price);
        books.add(book);
        System.out.println("Đã thêm sách: " + book);
    }

    static void deleteBook(Scanner scanner) {
        System.out.println("\n--- Xóa sách ---");
        System.out.print("Nhập Mã Sách cần xóa: ");
        int id = getIntInput(scanner);
        
        // Sử dụng Stream API
        Optional<Book> bookToRemove = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst();
        
        if (bookToRemove.isPresent()) {
            books.remove(bookToRemove.get());
            System.out.println("Đã xóa sách có ID: " + id);
        } else {
            System.out.println("Không tìm thấy sách có ID: " + id);
        }
    }

    static void updateBook(Scanner scanner) {
        System.out.println("\n--- Cập nhật sách ---");
        System.out.print("Nhập Mã Sách cần cập nhật: ");
        int id = getIntInput(scanner);
        
        Optional<Book> bookOpt = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst();
        
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            System.out.print("Nhập tên sách mới: ");
            book.setTitle(scanner.nextLine());
            System.out.print("Nhập tác giả mới: ");
            book.setAuthor(scanner.nextLine());
            System.out.print("Nhập giá mới: ");
            book.setPrice(getDoubleInput(scanner));
            System.out.println("Đã cập nhật sách: " + book);
        } else {
            System.out.println("Không tìm thấy sách có ID: " + id);
        }
    }

    // Sử dụng forEach và Method Reference
    static void showAllBooks() {
        System.out.println("\n=== DANH SÁCH SÁCH ===");
        if (books.isEmpty()) {
            System.out.println("Danh sách trống!");
            return;
        }
        books.forEach(System.out::println);
    }

    // Tìm sách có tên chứa từ "Lập trình" (không phân biệt hoa thường)
    static void findBooksContainingLapTrinh() {
        System.out.println("\n--- Sách có tên chứa 'Lập trình' ---");
        List<Book> result = books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains("lập trình"))
                .collect(Collectors.toList());
        
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy sách nào!");
        } else {
            result.forEach(System.out::println);
        }
    }

    // Lấy tối đa K cuốn sách có giá tiền <= P (sử dụng filter và limit)
    static void getBooksByPriceLimit(Scanner scanner) {
        System.out.println("\n--- Lấy K cuốn sách có giá <= P ---");
        System.out.print("Nhập giá tối đa (P): ");
        double maxPrice = getDoubleInput(scanner);
        System.out.print("Nhập số lượng tối đa (K): ");
        int limit = getIntInput(scanner);
        
        List<Book> result = books.stream()
                .filter(b -> b.getPrice() <= maxPrice)
                .limit(limit)
                .collect(Collectors.toList());
        
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy sách nào!");
        } else {
            result.forEach(System.out::println);
        }
    }

    // Tìm kiếm sách theo danh sách tác giả
    static void findBooksByAuthors(Scanner scanner) {
        System.out.println("\n--- Tìm sách theo tác giả ---");
        System.out.print("Nhập danh sách tác giả (cách nhau bởi dấu phẩy): ");
        String input = scanner.nextLine();
        List<String> authors = Arrays.asList(input.split(","));
        
        List<Book> result = books.stream()
                .filter(b -> authors.stream()
                        .anyMatch(a -> b.getAuthor().toLowerCase().contains(a.trim().toLowerCase())))
                .collect(Collectors.toList());
        
        if (result.isEmpty()) {
            System.out.println("Không tìm thấy sách nào!");
        } else {
            result.forEach(System.out::println);
        }
    }

    // ==================== BÀI 1.2.3: KIỂM ĐỊNH XE ====================
    
    static abstract class Xe {
        protected LocalDate ngaySx;
        protected String bienSo;

        public Xe(LocalDate ngaySx, String bienSo) {
            this.ngaySx = ngaySx;
            this.bienSo = bienSo;
        }

        public LocalDate getNgaySx() { return ngaySx; }
        public String getBienSo() { return bienSo; }

        public abstract double tinhPhiDangKiem();
        public abstract int getThoiHanDangKiem();

        @Override
        public String toString() {
            return String.format("Biển số: %s, Ngày SX: %s", bienSo, ngaySx);
        }
    }

    static class XeOto extends Xe {
        private int soGhe;
        private boolean kinhDoanh;

        public XeOto(LocalDate ngaySx, String bienSo, int soGhe, boolean kinhDoanh) {
            super(ngaySx, bienSo);
            this.soGhe = soGhe;
            this.kinhDoanh = kinhDoanh;
        }

        public int getSoGhe() { return soGhe; }
        public boolean isKinhDoanh() { return kinhDoanh; }

        @Override
        public double tinhPhiDangKiem() {
            double phiCoBan = 340000;
            if (kinhDoanh) phiCoBan += 100000;
            if (soGhe > 9) phiCoBan += 50000 * (soGhe - 9);
            return phiCoBan;
        }

        @Override
        public int getThoiHanDangKiem() {
            int tuoiXe = Period.between(ngaySx, LocalDate.now()).getYears();
            if (kinhDoanh) {
                if (tuoiXe < 5) return 12;
                if (tuoiXe < 12) return 6;
                return 3;
            } else {
                if (tuoiXe < 7) return 24;
                if (tuoiXe < 12) return 12;
                return 6;
            }
        }

        @Override
        public String toString() {
            return String.format("XeOto{%s, Số ghế: %d, Kinh doanh: %s, Phí ĐK: %.0f VNĐ, Thời hạn: %d tháng}",
                    super.toString(), soGhe, kinhDoanh ? "Có" : "Không", 
                    tinhPhiDangKiem(), getThoiHanDangKiem());
        }
    }

    static class XeTai extends Xe {
        private int trongTai;

        public XeTai(LocalDate ngaySx, String bienSo, int trongTai) {
            super(ngaySx, bienSo);
            this.trongTai = trongTai;
        }

        public int getTrongTai() { return trongTai; }

        @Override
        public double tinhPhiDangKiem() {
            if (trongTai <= 2000) return 340000;
            if (trongTai <= 7000) return 450000;
            if (trongTai <= 15000) return 560000;
            return 700000;
        }

        @Override
        public int getThoiHanDangKiem() {
            int tuoiXe = Period.between(ngaySx, LocalDate.now()).getYears();
            if (tuoiXe < 7) return 12;
            if (tuoiXe < 12) return 6;
            return 3;
        }

        @Override
        public String toString() {
            return String.format("XeTai{%s, Trọng tải: %d kg, Phí ĐK: %.0f VNĐ, Thời hạn: %d tháng}",
                    super.toString(), trongTai, tinhPhiDangKiem(), getThoiHanDangKiem());
        }
    }

    static List<Xe> danhSachXe = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static void runVehicleInspection(Scanner scanner) {
        // Thêm dữ liệu mẫu
        danhSachXe.add(new XeOto(LocalDate.of(2020, 5, 15), "30A-12345", 5, false));
        danhSachXe.add(new XeOto(LocalDate.of(2018, 3, 10), "51B-67890", 16, true));
        danhSachXe.add(new XeOto(LocalDate.of(2022, 8, 20), "29C-55555", 7, false)); // Biển đẹp
        danhSachXe.add(new XeOto(LocalDate.of(2015, 1, 5), "30D-99999", 45, true)); // Biển đẹp
        danhSachXe.add(new XeTai(LocalDate.of(2019, 6, 25), "51E-11111", 3500)); // Biển đẹp
        danhSachXe.add(new XeTai(LocalDate.of(2021, 4, 12), "29F-24680", 8000));
        danhSachXe.add(new XeTai(LocalDate.of(2017, 9, 30), "30G-13579", 15000));

        int choice;
        do {
            System.out.println("\n========== KIỂM ĐỊNH XE ==========");
            System.out.println("1. Thêm xe ô tô");
            System.out.println("2. Thêm xe tải");
            System.out.println("3. Hiển thị tất cả xe");
            System.out.println("4. Tìm xe ô tô có số chỗ ngồi nhiều nhất");
            System.out.println("5. Sắp xếp xe tải theo trọng tải tăng dần");
            System.out.println("6. Lọc biển số xe đẹp (4+ số giống nhau)");
            System.out.println("0. Quay lại menu chính");
            System.out.println("===================================");
            
            System.out.print("Chọn chức năng: ");
            choice = getIntInput(scanner);

            switch (choice) {
                case 1: addXeOto(scanner); break;
                case 2: addXeTai(scanner); break;
                case 3: showAllXe(); break;
                case 4: findXeOtoSoChoNhieuNhat(); break;
                case 5: sortXeTaiByTrongTai(); break;
                case 6: filterBienSoDep(); break;
                case 0: break;
                default: System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }

    static void addXeOto(Scanner scanner) {
        System.out.println("\n--- Thêm xe ô tô ---");
        System.out.print("Nhập biển số (VD: 30A-12345): ");
        String bienSo = scanner.nextLine();
        LocalDate ngaySx = getDateInput(scanner, "Nhập ngày sản xuất (dd/MM/yyyy): ");
        System.out.print("Nhập số ghế: ");
        int soGhe = getIntInput(scanner);
        System.out.print("Xe kinh doanh? (y/n): ");
        boolean kinhDoanh = scanner.nextLine().trim().equalsIgnoreCase("y");
        
        XeOto xe = new XeOto(ngaySx, bienSo, soGhe, kinhDoanh);
        danhSachXe.add(xe);
        System.out.println("Đã thêm: " + xe);
    }

    static void addXeTai(Scanner scanner) {
        System.out.println("\n--- Thêm xe tải ---");
        System.out.print("Nhập biển số (VD: 51E-67890): ");
        String bienSo = scanner.nextLine();
        LocalDate ngaySx = getDateInput(scanner, "Nhập ngày sản xuất (dd/MM/yyyy): ");
        System.out.print("Nhập trọng tải (kg): ");
        int trongTai = getIntInput(scanner);
        
        XeTai xe = new XeTai(ngaySx, bienSo, trongTai);
        danhSachXe.add(xe);
        System.out.println("Đã thêm: " + xe);
    }

    static void showAllXe() {
        System.out.println("\n=== DANH SÁCH XE ===");
        if (danhSachXe.isEmpty()) {
            System.out.println("Danh sách trống!");
            return;
        }
        danhSachXe.forEach(System.out::println);
    }

    static void findXeOtoSoChoNhieuNhat() {
        System.out.println("\n--- Xe ô tô có số chỗ ngồi nhiều nhất ---");
        Optional<XeOto> result = danhSachXe.stream()
                .filter(xe -> xe instanceof XeOto)
                .map(xe -> (XeOto) xe)
                .max(Comparator.comparingInt(XeOto::getSoGhe));
        
        if (result.isPresent()) {
            System.out.println(result.get());
        } else {
            System.out.println("Không có xe ô tô nào!");
        }
    }

    static void sortXeTaiByTrongTai() {
        System.out.println("\n--- Xe tải sắp xếp theo trọng tải tăng dần ---");
        List<XeTai> xeTaiList = danhSachXe.stream()
                .filter(xe -> xe instanceof XeTai)
                .map(xe -> (XeTai) xe)
                .sorted(Comparator.comparingInt(XeTai::getTrongTai))
                .collect(Collectors.toList());
        
        if (xeTaiList.isEmpty()) {
            System.out.println("Không có xe tải nào!");
        } else {
            xeTaiList.forEach(System.out::println);
        }
    }

    // Lọc biển số xe "đẹp": 5 số cuối có ít nhất 4 số giống nhau (Regex)
    static void filterBienSoDep() {
        System.out.println("\n--- Xe có biển số đẹp (4+ số giống nhau) ---");
        
        List<Xe> result = danhSachXe.stream()
                .filter(xe -> {
                    String bienSo = xe.getBienSo().replaceAll("[^0-9]", "");
                    if (bienSo.length() < 5) return false;
                    String last5 = bienSo.substring(bienSo.length() - 5);
                    return hasAtLeast4SameDigits(last5);
                })
                .collect(Collectors.toList());
        
        if (result.isEmpty()) {
            System.out.println("Không có xe nào có biển số đẹp!");
        } else {
            result.forEach(System.out::println);
        }
    }

    static boolean hasAtLeast4SameDigits(String digits) {
        if (digits.length() != 5) return false;
        int[] count = new int[10];
        for (char c : digits.toCharArray()) {
            count[c - '0']++;
        }
        for (int c : count) {
            if (c >= 4) return true;
        }
        return false;
    }

    // ==================== HELPER METHODS ====================
    
    static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Vui lòng nhập số nguyên hợp lệ: ");
            }
        }
    }

    static double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Vui lòng nhập số thực hợp lệ: ");
            }
        }
    }

    static LocalDate getDateInput(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Vui lòng nhập ngày theo định dạng dd/MM/yyyy!");
            }
        }
    }
}
