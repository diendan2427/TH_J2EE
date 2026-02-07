package com.hutech.bai8.controller;

import com.hutech.bai8.service.ExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/admin/books/import")
@RequiredArgsConstructor
public class ImportController {

    private final ExcelImportService excelImportService;

    /**
     * Show import page
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String showImportPage(Model model) {
        model.addAttribute("activeTab", "import");
        return "admin/books/import";
    }

    /**
     * Upload and import Excel file
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> importBooks(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result;
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Vui lòng chọn file Excel")
                );
            }
            
            // Check file extension
            String filename = file.getOriginalFilename();
            if (filename == null || !(filename.endsWith(".xlsx") || filename.endsWith(".xls") || filename.endsWith(".csv"))) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Chỉ hỗ trợ file Excel (.xlsx, .xls) hoặc CSV (.csv)")
                );
            }
            
            // TODO: Add CSV import support if needed
            if (filename.endsWith(".csv")) {
                return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Hiện tại chưa hỗ trợ import CSV. Vui lòng chuyển sang Excel (.xlsx)")
                );
            }
            
            // Import books
            result = excelImportService.importBooksFromExcel(file);
            return ResponseEntity.ok(result);
            
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "message", "Lỗi đọc file: " + e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "message", "Lỗi: " + e.getMessage())
            );
        }
    }

    /**
     * Download template Excel file (.xlsx)
     */
    @GetMapping("/template")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            byte[] template = excelImportService.generateTemplate();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "book_import_template.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(template);
                    
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Download template CSV file with UTF-8 BOM
     */
    @GetMapping("/template-csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> downloadTemplateCsv() {
        try {
            // Read CSV file and add UTF-8 BOM
            String csvContent = "title,author,description,price,stock,categoryId,imageUrl\n" +
                    "Lập trình Java cơ bản,Nguyễn Văn A,Sách hướng dẫn lập trình Java từ cơ bản đến nâng cao,150000,100,6982c0cea732c155aa614808,https://picsum.photos/seed/java/300/400\n" +
                    "Spring Boot thực chiến,Trần Văn B,Xây dựng ứng dụng web với Spring Boot,200000,50,6982c0cea732c155aa614808,https://picsum.photos/seed/spring/300/400\n" +
                    "Python cho ngườI mới,Lê Văn C,Học Python từ con số 0,120000,200,6982c0cea732c155aa614808,https://picsum.photos/seed/python/300/400\n" +
                    "JavaScript hiện đại,Phạm Văn D,ES6 và các framework hiện đại,180000,80,6982c0cea732c155aa614808,https://picsum.photos/seed/js/300/400\n" +
                    "Kinh tế học vi mô,Hoàng Văn E,Cơ bản về kinh tế học,95000,150,6982c0cea732c155aa614809,https://picsum.photos/seed/econ/300/400\n" +
                    "Khởi nghiệp tinh gọn,Eric Ries,Phương pháp Lean Startup,160000,75,6982c0cea732c155aa614809,https://picsum.photos/seed/startup/300/400\n" +
                    "Từ tốt đến vĩ đại,Jim Collins,Nghiên cứu về các công ty thành công,185000,60,6982c0cea732c155aa614809,https://picsum.photos/seed/business/300/400\n" +
                    "Truyện Kiều,Nguyễn Du,Tác phẩm văn học kinh điển,85000,300,6982c0cea732c155aa61480a,https://picsum.photos/seed/kieu/300/400\n" +
                    "Nhật ký trong tù,Hồ Chí Minh,Hồi ký của Bác Hồ,75000,250,6982c0cea732c155aa61480a,https://picsum.photos/seed/nhatky/300/400\n" +
                    "Vật lý đại cương,Nguyễn Văn F,Giáo trình vật lý đại học,110000,120,6982c0cea732c155aa61480b,https://picsum.photos/seed/physics/300/400\n" +
                    "Hóa học hữu cơ,Trần Văn G,Sách hóa học nâng cao,135000,90,6982c0cea732c155aa61480b,https://picsum.photos/seed/chemistry/300/400\n" +
                    "7 thói quen hiệu quả,Stephen Covey,Sách phát triển bản thân,145000,180,6982c0cea732c155aa61480c,https://picsum.photos/seed/habits/300/400\n" +
                    "Đắc nhân tâm,Dale Carnegie,Nghệ thuật thu phục lòng ngườI,95000,500,6982c0cea732c155aa61480c,https://picsum.photos/seed/dacnhantam/300/400\n" +
                    "Nhà giả kim,Paulo Coelho,Tiểu thuyết triết lý sống,88000,400,6982c0cea732c155aa61480a,https://picsum.photos/seed/nhagiakim/300/400\n";
            
            // Add UTF-8 BOM (Byte Order Mark) to help Excel recognize UTF-8 encoding
            byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
            byte[] csvBytes = csvContent.getBytes("UTF-8");
            byte[] result = new byte[bom.length + csvBytes.length];
            System.arraycopy(bom, 0, result, 0, bom.length);
            System.arraycopy(csvBytes, 0, result, bom.length, csvBytes.length);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
            headers.setContentDispositionFormData("attachment", "book_import_template_utf8.csv");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(result);
                    
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * API endpoint for importing books (for Postman testing)
     */
    @PostMapping("/api")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> importBooksApi(@RequestParam("file") MultipartFile file) {
        return importBooks(file);
    }

    /**
     * Validate Excel file structure
     */
    @PostMapping("/validate")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<?> validateExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                Map.of("valid", false, "message", "Vui lòng chọn file")
            );
        }
        
        Map<String, Object> result = excelImportService.validateExcelFile(file);
        return ResponseEntity.ok(result);
    }
}
