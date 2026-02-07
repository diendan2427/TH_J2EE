package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.model.Category;
import com.hutech.bai8.repository.BookRepository;
import com.hutech.bai8.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelImportService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Import books from Excel file
     * 
     * @param file Excel file (.xlsx)
     * @return Map with import results
     */
    public Map<String, Object> importBooksFromExcel(MultipartFile file) throws IOException {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> successMessages = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row (row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    Book book = parseBookFromRow(row, i + 1);
                    if (book != null) {
                        bookRepository.save(book);
                        successCount++;
                        successMessages.add("Dòng " + (i + 1) + ": Đã thêm sách '" + book.getTitle() + "'");
                    }
                } catch (Exception e) {
                    errorCount++;
                    errors.add("Dòng " + (i + 1) + ": " + e.getMessage());
                    log.error("Error importing row {}: {}", i + 1, e.getMessage());
                }
            }
        }

        result.put("successCount", successCount);
        result.put("errorCount", errorCount);
        result.put("totalRows", successCount + errorCount);
        result.put("successMessages", successMessages);
        result.put("errors", errors);
        result.put("success", errorCount == 0);

        return result;
    }

    /**
     * Parse book data from Excel row
     */
    private Book parseBookFromRow(Row row, int rowNum) throws Exception {
        Book book = new Book();

        // Column 0: title (required)
        Cell titleCell = row.getCell(0);
        if (titleCell == null || getCellValueAsString(titleCell).trim().isEmpty()) {
            throw new Exception("Tiêu đề sách không được để trống");
        }
        book.setTitle(getCellValueAsString(titleCell).trim());

        // Column 1: author
        Cell authorCell = row.getCell(1);
        book.setAuthor(authorCell != null ? getCellValueAsString(authorCell).trim() : "");

        // Column 2: description
        Cell descCell = row.getCell(2);
        book.setDescription(descCell != null ? getCellValueAsString(descCell).trim() : "");

        // Column 3: price (required)
        Cell priceCell = row.getCell(3);
        if (priceCell == null) {
            throw new Exception("Giá sách không được để trống");
        }
        double price = getCellValueAsDouble(priceCell);
        if (price <= 0) {
            throw new Exception("Giá sách phải lớn hơn 0");
        }
        book.setPrice(price);

        // Column 4: stock (required)
        Cell stockCell = row.getCell(4);
        if (stockCell == null) {
            throw new Exception("Số lượng tồn kho không được để trống");
        }
        double stock = getCellValueAsDouble(stockCell);
        if (stock < 0) {
            throw new Exception("Số lượng tồn kho không được âm");
        }
        book.setStock((int) stock);

        // Column 5: categoryId (required)
        Cell categoryCell = row.getCell(5);
        if (categoryCell == null || getCellValueAsString(categoryCell).trim().isEmpty()) {
            throw new Exception("Danh mục không được để trống");
        }
        String categoryId = getCellValueAsString(categoryCell).trim();
        
        // Validate category exists
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (!categoryOpt.isPresent()) {
            throw new Exception("Danh mục ID '" + categoryId + "' không tồn tại trong hệ thống");
        }
        book.setCategory(categoryOpt.get());

        // Column 6: imageUrl (optional)
        Cell imageCell = row.getCell(6);
        if (imageCell != null) {
            String imageUrl = getCellValueAsString(imageCell).trim();
            if (!imageUrl.isEmpty()) {
                book.setImageUrl(imageUrl);
            }
        }

        return book;
    }

    /**
     * Get cell value as String
     */
    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Get cell value as Double
     */
    private double getCellValueAsDouble(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().replace(",", ""));
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    /**
     * Download template Excel file
     */
    public byte[] generateTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); 
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Books");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"title", "author", "description", "price", "stock", "categoryId", "imageUrl"};
            
            // Create cell style for header
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Add sample data rows with Vietnamese text
            String[][] sampleData = {
                {"Lập trình Java cơ bản", "Nguyễn Văn A", "Sách hướng dẫn lập trình Java từ cơ bản đến nâng cao", "150000", "100", "6982c0cea732c155aa614808", "https://picsum.photos/seed/java/300/400"},
                {"Spring Boot thực chiến", "Trần Văn B", "Xây dựng ứng dụng web với Spring Boot", "200000", "50", "6982c0cea732c155aa614808", "https://picsum.photos/seed/spring/300/400"},
                {"Python cho ngườI mới", "Lê Văn C", "Học Python từ con số 0", "120000", "200", "6982c0cea732c155aa614808", "https://picsum.photos/seed/python/300/400"},
                {"JavaScript hiện đại", "Phạm Văn D", "ES6 và các framework hiện đại", "180000", "80", "6982c0cea732c155aa614808", "https://picsum.photos/seed/js/300/400"},
                {"Kinh tế học vi mô", "Hoàng Văn E", "Cơ bản về kinh tế học", "95000", "150", "6982c0cea732c155aa614809", "https://picsum.photos/seed/econ/300/400"},
                {"Khởi nghiệp tinh gọn", "Eric Ries", "Phương pháp Lean Startup", "160000", "75", "6982c0cea732c155aa614809", "https://picsum.photos/seed/startup/300/400"},
                {"Từ tốt đến vĩ đại", "Jim Collins", "Nghiên cứu về các công ty thành công", "185000", "60", "6982c0cea732c155aa614809", "https://picsum.photos/seed/business/300/400"},
                {"Truyện Kiều", "Nguyễn Du", "Tác phẩm văn học kinh điển", "85000", "300", "6982c0cea732c155aa61480a", "https://picsum.photos/seed/kieu/300/400"},
                {"Nhật ký trong tù", "Hồ Chí Minh", "Hồi ký của Bác Hồ", "75000", "250", "6982c0cea732c155aa61480a", "https://picsum.photos/seed/nhatky/300/400"},
                {"Vật lý đại cương", "Nguyễn Văn F", "Giáo trình vật lý đại học", "110000", "120", "6982c0cea732c155aa61480b", "https://picsum.photos/seed/physics/300/400"},
                {"Hóa học hữu cơ", "Trần Văn G", "Sách hóa học nâng cao", "135000", "90", "6982c0cea732c155aa61480b", "https://picsum.photos/seed/chemistry/300/400"},
                {"7 thói quen hiệu quả", "Stephen Covey", "Sách phát triển bản thân", "145000", "180", "6982c0cea732c155aa61480c", "https://picsum.photos/seed/habits/300/400"},
                {"Đắc nhân tâm", "Dale Carnegie", "Nghệ thuật thu phục lòng ngườI", "95000", "500", "6982c0cea732c155aa61480c", "https://picsum.photos/seed/dacnhantam/300/400"},
                {"Nhà giả kim", "Paulo Coelho", "Tiểu thuyết triết lý sống", "88000", "400", "6982c0cea732c155aa61480a", "https://picsum.photos/seed/nhagiakim/300/400"}
            };
            
            for (int i = 0; i < sampleData.length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < sampleData[i].length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(sampleData[i][j]);
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Validate Excel file structure
     */
    public Map<String, Object> validateExcelFile(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet.getLastRowNum() < 1) {
                errors.add("File Excel không có dữ liệu (chỉ có header)");
            }
            
            // Validate header
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                errors.add("File Excel không có header");
            } else {
                String[] expectedHeaders = {"title", "author", "description", "price", "stock", "categoryId", "imageUrl"};
                for (int i = 0; i < expectedHeaders.length; i++) {
                    Cell cell = headerRow.getCell(i);
                    if (cell == null || !expectedHeaders[i].equalsIgnoreCase(getCellValueAsString(cell).trim())) {
                        errors.add("Cột " + (i + 1) + " phải là '" + expectedHeaders[i] + "'");
                    }
                }
            }
            
            result.put("valid", errors.isEmpty());
            result.put("errors", errors);
            result.put("totalRows", sheet.getLastRowNum());
            
        } catch (IOException e) {
            errors.add("Không thể đọc file Excel: " + e.getMessage());
            result.put("valid", false);
            result.put("errors", errors);
        }

        return result;
    }
}
