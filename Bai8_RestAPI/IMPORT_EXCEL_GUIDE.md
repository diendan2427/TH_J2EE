# 📊 HƯỚNG DẪN IMPORT SÁCH TỪ EXCEL

## 📁 Cấu Trúc File Excel

File Excel cần có **7 cột** theo thứ tự sau:

| Cột | Tên trường   | Bắt buộc | Mô tả                              | Ví dụ                    |
| --- | ------------ | -------- | ---------------------------------- | ------------------------ |
| A   | title        | ✅ Có    | Tiêu đề sách                       | Lập trình Java cơ bản    |
| B   | author       | ❌ Không | Tác giả                            | Nguyễn Văn A             |
| C   | description  | ❌ Không | Mô tả sách                         | Sách hướng dẫn Java...     |
| D   | price        | ✅ Có    | Giá tiền (VND)                     | 150000                   |
| E   | stock        | ✅ Có    | Số lượng tồn kho                   | 100                      |
| F   | categoryId   | ✅ Có    | ID danh mục (lấy từ hệ thống)      | 6982c0cea732c155aa614808 |
| G   | imageUrl     | ❌ Không | URL hình ảnh (tùy chọn)            | https://example.com/img.jpg |

## 📝 Mẫu Dữ Liệu

### Sheet 1 (Bắt đầu từ dòng 2, dòng 1 là header):

| title                     | author       | description                        | price  | stock | categoryId               | imageUrl                           |
| ------------------------- | ------------ | ---------------------------------- | ------ | ----- | ------------------------ | ---------------------------------- |
| Lập trình Java cơ bản     | Nguyễn Văn A | Sách hướng dẫn lập trình Java      | 150000 | 100   | 6982c0cea732c155aa614808 | https://picsum.photos/seed/java/300/400 |
| Spring Boot thực chiến    | Trần Văn B   | Xây dựng ứng dụng web              | 200000 | 50    | 6982c0cea732c155aa614808 | https://picsum.photos/seed/spring/300/400 |
| Python cho ngườI mới      | Lê Văn C     | Học Python từ con số 0             | 120000 | 200   | 6982c0cea732c155aa614808 | https://picsum.photos/seed/python/300/400 |
| JavaScript hiện đại       | Phạm Văn D   | ES6 và các framework hiện đại      | 180000 | 80    | 6982c0cea732c155aa614808 | https://picsum.photos/seed/js/300/400 |
| Kinh tế học vi mô         | Hoàng Văn E  | Cơ bản về kinh tế học              | 95000  | 150   | 6982c0cea732c155aa614809 | https://picsum.photos/seed/econ/300/400 |
| Khởi nghiệp tinh gọn      | Eric Ries    | Phương pháp Lean Startup           | 160000 | 75    | 6982c0cea732c155aa614809 | https://picsum.photos/seed/startup/300/400 |

## 🔑 Danh Sách Category ID

| ID                                     | Tên danh mục |
| -------------------------------------- | ------------ |
| 6982c0cea732c155aa614808               | Lập trình    |
| 6982c0cea732c155aa614809               | Kinh tế      |
| 6982c0cea732c155aa61480a               | Văn học      |
| 6982c0cea732c155aa61480b               | Khoa học     |
| 6982c0cea732c155aa61480c               | Kỹ năng sống |

## ⚠️ Lưu Ý Quan Trọng

1. **Header row (dòng 1):** Phải có đúng tên cột: `title, author, description, price, stock, categoryId, imageUrl`

2. **Dữ liệu bắt đầu từ dòng 2** (dòng đầu tiên chứa dữ liệu thực tế)

3. **Kiểu dữ liệu:**
   - `title`: Text
   - `author`: Text (có thể để trống)
   - `description`: Text (có thể để trống)
   - `price`: Số (không dấu phẩy, ví dụ: 150000)
   - `stock`: Số nguyên (ví dụ: 100)
   - `categoryId`: Text (ID phải tồn tại trong hệ thống)
   - `imageUrl`: Text URL (có thể để trống)

4. **Validation:**
   - ❌ Không được để trống: title, price, stock, categoryId
   - ❌ Giá phải > 0
   - ❌ Stock phải >= 0
   - ❌ CategoryId phải tồn tại trong database

## 🚀 Cách Sử Dụng

### Cách 1: Qua Web Interface (Admin)

1. Đăng nhập với tài khoản **Admin**
2. Truy cập: `http://localhost:8080/admin/books/import`
3. Tải file mẫu Excel
4. Điền dữ liệu và lưu file
5. Upload file và nhấn "Import Sách"
6. Xem kết quả (số sách thành công/lỗi)

### Cách 2: Qua Postman (API)

```
POST http://localhost:8080/admin/books/import/api
Authorization: Bearer {admin_token}
Content-Type: multipart/form-data

Form Data:
- file: [chọn file Excel]
```

**Response:**
```json
{
  "successCount": 5,
  "errorCount": 0,
  "totalRows": 5,
  "successMessages": [
    "Dòng 2: Đã thêm sách 'Lập trình Java cơ bản'",
    "Dòng 3: Đã thêm sách 'Spring Boot thực chiến'",
    ...
  ],
  "errors": [],
  "success": true
}
```

### Cách 3: Download Template

```
GET http://localhost:8080/admin/books/import/template
Authorization: Bearer {admin_token}
```

Trả về file Excel mẫu với dữ liệu test.

## 🐛 Xử Lý Lỗi Thường Gặp

### 1. "Danh mục ID không tồn tại"
**Giải pháp:** Kiểm tra lại ID danh mục trong bảng Category ở trên

### 2. "Giá sách phải lớn hơn 0"
**Giải pháp:** Đảm bảo cột price là số dương

### 3. "Tiêu đề sách không được để trống"
**Giải pháp:** Kiểm tra cột title không được để trống

### 4. "Chỉ hỗ trợ file .xlsx, .xls"
**Giải pháp:** Lưu file Excel đúng định dạng .xlsx

### 5. Lỗi đọc file
**Giải pháp:** Đảm bảo file không bị khóa hoặc corrupted

## 📞 Hỗ Trợ

Nếu gặp lỗi không import được:
1. Kiểm tra log ứng dụng
2. Kiểm tra định dạng file Excel
3. Kiểm tra ID danh mục có tồn tại không

---

**Chúc bạn import thành công! 📚✨**
