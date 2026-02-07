# 📄 HƯỚNG DẪN MỞ FILE CSV KHÔNG BỊ LỖI FONT TIẾNG VIỆT

## ❌ Vấn Đề

Khi mở file CSV bằng Excel trực tiếp, tiếng Việt bị lỗi font:
- **Lỗi:** `Tá»« tá»‘t Ä‘áº¿n vÄ© Ä‘áº¡i` 
- **Đúng:** `Từ tốt đến vĩ đại`

## ✅ Cách Khắc Phục

### Cách 1: Import CSV vào Excel (Khuyên dùng)

1. **Mở Excel** (không click đúp vào file CSV)
2. **Vào tab Data** → **Get Data** → **From File** → **From Text/CSV**
3. **Chọn file CSV** cần import
4. **Trong hộp thoại Import:**
   - File Origin: Chọn **Unicode (UTF-8)** hoặc **65001: Unicode (UTF-8)**
   - Delimiter: Chọn **Comma**
5. **Click Load**

### Cách 2: Dùng Notepad++ chuyển encoding

1. **Mở file CSV** bằng Notepad++
2. **Vào menu Encoding** → Chọn **Convert to UTF-8-BOM**
3. **Save lại file**
4. **Mở bằng Excel** - lần này sẽ đọc đúng

### Cách 3: Dùng Google Sheets (Dễ nhất)

1. **Vào Google Drive** → **New** → **File upload**
2. **Upload file CSV**
3. **Click chuột phải** vào file → **Open with** → **Google Sheets**
4. Tiếng Việt sẽ hiển thị đúng
5. Có thể **Download as** → **Microsoft Excel (.xlsx)** nếu cần

### Cách 4: Import trong Excel (Excel 2010+)

1. **Mở Excel mới**
2. **Vào Data** → **From Text** (hoặc **Get External Data** → **From Text**)
3. **Chọn file CSV**
4. **Trong Text Import Wizard:**
   - Step 1: Chọn **Delimited** → **Next**
   - Step 2: Chọn **Comma** → **Next**
   - Step 3: Click vào cột có tiếng Việt → Chọn **Text** ở dưới → **Finish**

### Cách 5: Dùng OpenOffice Calc hoặc LibreOffice

1. **Mở OpenOffice/LibreOffice Calc**
2. **File** → **Open** → Chọn file CSV
3. **Trong hộp thoại:**
   - Character set: Chọn **Unicode (UTF-8)**
   - Separator options: Chọn **Comma**
4. **Click OK**

## 🎯 Khuyến nghị

### **Tốt nhất:** Dùng file Excel (.xlsx) thay vì CSV
- Tải template từ web: `http://localhost:8080/admin/books/import/template`
- File này đã là .xlsx và hiển thị tiếng Việt đúng

### **Nếu phải dùng CSV:**
1. Dùng **Cách 3 (Google Sheets)** - dễ nhất
2. Hoặc **Cách 1 (Excel Import)** - chuẩn nhất

## 💡 Lưu Ý khi lưu file Excel thành CSV

Nếu bạn đã có file Excel và muốn lưu thành CSV:

1. **File** → **Save As**
2. **Save as type:** Chọn **CSV UTF-8 (Comma delimited) (*.csv)**
3. **Quan trọng:** Phải chọn UTF-8 chứ không phải CSV thường

## 🆘 Vẫn bị lỗi?

Nếu đã thử các cách trên mà vẫn lỗi:
1. **Copy dữ liệu** từ CSV
2. **Paste vào Excel** (dùng Paste Special → Text)
3. Hoặc liên hệ admin để được hỗ trợ

---

**Tóm lại:** Nên dùng file `.xlsx` tải từ web template thay vì CSV để tránh lỗi font! 📊
