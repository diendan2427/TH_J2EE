# HƯỚNG DẪN CHẠY 8 BÀI THỰC HÀNH JAVA

## YÊU CẦU HỆ THỐNG

### 1. Java JDK 8+ (BẮT BUỘC)
Tất cả các bài đều tương thích với Java 8 (đã cài sẵn trên máy bạn).

**Kiểm tra:** Mở CMD và gõ `java -version`

### 2. Maven (BẮT BUỘC cho Bài 2-8)
**Cách cài Maven:**
1. Tải từ: https://maven.apache.org/download.cgi (chọn file `apache-maven-x.x.x-bin.zip`)
2. Giải nén vào `C:\maven`
3. Thêm vào PATH:
   - Nhấn `Win + R`, gõ `sysdm.cpl`, nhấn Enter
   - Tab **Advanced** → **Environment Variables**
   - Phần **System variables** → Chọn **Path** → **Edit** → **New**
   - Thêm: `C:\maven\bin`
   - OK tất cả các cửa sổ
4. Mở CMD mới và kiểm tra: `mvn -version`

### 3. MongoDB (BẮT BUỘC cho Bài 4-8)
**Cách cài:**
1. Tải MongoDB Community: https://www.mongodb.com/try/download/community
2. Chọn **Windows** → **msi** → Download
3. Cài đặt với tùy chọn "Install MongoDB as a Service"
4. MongoDB sẽ tự chạy khi Windows khởi động

**Kiểm tra:** Mở CMD và gõ `mongosh` hoặc truy cập http://localhost:27017

---

## CÁCH CHẠY TRONG VS CODE

### Bước 1: Cài Extension Pack for Java
1. Mở VS Code
2. Nhấn `Ctrl+Shift+X` (mở Extensions)
3. Tìm: **Extension Pack for Java** (của Microsoft)
4. Nhấn **Install**

Extension này bao gồm:
- Language Support for Java
- Debugger for Java
- Maven for Java
- Project Manager for Java
- Test Runner for Java

### Bước 2: Mở từng bài
1. **File** → **Open Folder**
2. Chọn thư mục bài cần chạy (ví dụ: `D:\Hutech\THJ2EE\Bai1_JavaCore`)
3. Đợi VS Code load xong project (góc dưới phải hiện "Java: Ready")

### Bước 3: Chạy từng bài

#### BÀI 1 - Java Core (Console)
1. Mở folder `Bai1_JavaCore`
2. Mở file `src/Main.java`
3. Nhấn `F5` hoặc click **Run** phía trên hàm `main()`
4. Chọn **Java** nếu được hỏi
5. Ứng dụng chạy trong TERMINAL

#### BÀI 2-8 - Spring Boot
1. Mở folder bài tương ứng (ví dụ: `Bai2_SpringBoot`)
2. Đợi VS Code load Maven project (lần đầu sẽ lâu vì tải dependencies)
3. Mở file `src/main/java/com/hutech/baiX/Main.java`
4. Nhấn `F5` hoặc click **Run** phía trên hàm `main()`
5. Đợi Spring Boot khởi động (thấy log "Started Main in ... seconds")
6. Truy cập: http://localhost:8080

**Cách khác (dùng Terminal):**
```bash
# Mở Terminal trong VS Code (Ctrl+`)
mvn spring-boot:run
```

---

## CẤU TRÚC 8 BÀI

| Bài | Folder          | Mô tả                              | MongoDB |
|-----|-----------------|--------------------------------------|---------|
| 1   | Bai1_JavaCore   | Console App (Quản lý sách, Kiểm định xe) | Không |
| 2   | Bai2_SpringBoot | Web (Thông tin SV, Môn học, Liên hệ) | Không |
| 3   | Bai3_CRUD       | CRUD Book với Thymeleaf Fragments    | Không |
| 4   | Bai4_MongoDB    | Spring Data MongoDB (Book, Category) | **Có** |
| 5   | Bai5_Cart       | Giỏ hàng, Validation, Checkout       | **Có** |
| 6   | Bai6_Security   | Spring Security (Đăng ký, Đăng nhập) | **Có** |
| 7   | Bai7_RoleBased  | Phân quyền ADMIN/USER                | **Có** |
| 8   | Bai8_RestAPI    | RESTful API + AJAX                   | **Có** |

---

## TÀI KHOẢN TEST (Bài 6-7)

| Bài | Username | Password | Quyền |
|-----|----------|----------|-------|
| 6   | (Tự đăng ký) | - | User |
| 7   | admin    | admin123 | ADMIN |
| 7   | user     | user123  | USER  |

---

## LƯU Ý QUAN TRỌNG

1. **Chỉ chạy 1 bài Spring Boot tại 1 thời điểm** (vì cùng port 8080)
   - Để dừng: nhấn `Ctrl+C` trong Terminal hoặc click nút Stop

2. **Bật MongoDB trước khi chạy Bài 4-8**
   - Kiểm tra service: `Win+R` → `services.msc` → Tìm "MongoDB Server"

3. **Lần đầu chạy Spring Boot sẽ lâu** vì Maven cần tải dependencies

4. **Nếu gặp lỗi "Port 8080 already in use"**:
   - Tắt ứng dụng đang chạy ở bài trước
   - Hoặc mở CMD: `netstat -ano | findstr :8080` rồi `taskkill /PID <pid> /F`

---

## THÔNG TIN KỸ THUẬT

- **Java Version**: 1.8 (Java 8)
- **Spring Boot Version**: 2.7.18
- **MongoDB**: Localhost, port 27017
- **Database Name**: Tự động tạo theo từng bài (bai4_db, bai5_db, ...)
