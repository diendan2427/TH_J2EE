package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin/inventory")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class InventoryController {
    private final PdfService pdfService;
    private final Cart cart;

    // Trang quản lý tồn kho
    @GetMapping
    public String inventoryPage(Model model) {
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/inventory/index";
    }

    // Xuất báo cáo tồn kho PDF
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportInventoryPdf() {
        try {
            byte[] pdfBytes = pdfService.exportInventoryReport();
            
            String filename = "BaoCaoTonKho_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xuất PDF: " + e.getMessage());
        }
    }

    // Import sách từ PDF
    @PostMapping("/import")
    public String importBooksFromPdf(@RequestParam("pdfFile") MultipartFile pdfFile,
                                     RedirectAttributes redirectAttributes) {
        if (pdfFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file PDF!");
            return "redirect:/admin/inventory";
        }
        
        if (!pdfFile.getContentType().equals("application/pdf")) {
            redirectAttributes.addFlashAttribute("error", "File phải là định dạng PDF!");
            return "redirect:/admin/inventory";
        }
        
        try {
            List<PdfService.BookImportResult> results = pdfService.importBooksFromPdf(pdfFile);
            
            long successCount = results.stream().filter(PdfService.BookImportResult::isSuccess).count();
            long failCount = results.size() - successCount;
            
            redirectAttributes.addFlashAttribute("importResults", results);
            redirectAttributes.addFlashAttribute("success", "Import hoàn tất! Thành công: " + successCount + ", Thất bại: " + failCount);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi import PDF: " + e.getMessage());
        }
        
        return "redirect:/admin/inventory";
    }
}
