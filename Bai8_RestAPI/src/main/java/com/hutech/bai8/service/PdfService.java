package com.hutech.bai8.service;

import com.hutech.bai8.model.Book;
import com.hutech.bai8.repository.BookRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;
import java.awt.Color;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    // Xuất báo cáo tồn kho ra PDF
    public byte[] exportInventoryReport() throws DocumentException {
        List<Book> books = bookRepository.findAll();
        
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Tiêu đề
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(0, 51, 102));
        Paragraph title = new Paragraph("BÁO CÁO TỒN KHO SÁCH", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        // Ngày xuất báo cáo
        Font dateFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.GRAY);
        Paragraph date = new Paragraph("Ngày xuất: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), dateFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(20);
        document.add(date);
        
        // Tạo bảng
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        
        // Header
        String[] headers = {"STT", "Mã sách", "Tiêu đề", "Tác giả", "Danh mục", "Giá (VND)", "Tồn kho", "Trạng thái"};
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Color headerBg = new Color(0, 51, 102);
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(headerBg);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            table.addCell(cell);
        }
        
        // Dữ liệu
        Font dataFont = new Font(Font.HELVETICA, 9, Font.NORMAL);
        Font dataFontBold = new Font(Font.HELVETICA, 9, Font.BOLD);
        
        int stt = 1;
        int totalStock = 0;
        double totalValue = 0;
        
        for (Book book : books) {
            // STT
            PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(stt++), dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            // Mã sách
            cell = new PdfPCell(new Phrase(book.getId(), dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            // Tiêu đề
            cell = new PdfPCell(new Phrase(book.getTitle(), dataFontBold));
            table.addCell(cell);
            
            // Tác giả
            cell = new PdfPCell(new Phrase(book.getAuthor(), dataFont));
            table.addCell(cell);
            
            // Danh mục
            String categoryName = book.getCategory() != null ? book.getCategory().getName() : "N/A";
            cell = new PdfPCell(new Phrase(categoryName, dataFont));
            table.addCell(cell);
            
            // Giá
            cell = new PdfPCell(new Phrase(String.format("%,.0f", book.getPrice()), dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            
            // Tồn kho
            int stock = book.getStock() != null ? book.getStock() : 0;
            cell = new PdfPCell(new Phrase(String.valueOf(stock), dataFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (stock <= 0) {
                cell.setBackgroundColor(new Color(255, 200, 200));
            } else if (stock <= 10) {
                cell.setBackgroundColor(new Color(255, 255, 200));
            } else {
                cell.setBackgroundColor(new Color(200, 255, 200));
            }
            table.addCell(cell);
            
            // Trạng thái
            String status;
            Color statusColor;
            if (stock <= 0) {
                status = "Hết hàng";
                statusColor = Color.RED;
            } else if (stock <= 10) {
                status = "Sắp hết";
                statusColor = new Color(255, 128, 0);
            } else {
                status = "Còn hàng";
                statusColor = new Color(0, 128, 0);
            }
            Font statusFont = new Font(Font.HELVETICA, 9, Font.BOLD, statusColor);
            cell = new PdfPCell(new Phrase(status, statusFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            totalStock += stock;
            totalValue += book.getPrice() * stock;
        }
        
        document.add(table);
        
        // Tổng kết
        document.add(new Paragraph(" ", new Font(Font.HELVETICA, 10)));
        
        Font summaryFont = new Font(Font.HELVETICA, 11, Font.BOLD);
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(50);
        summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        PdfPCell labelCell = new PdfPCell(new Phrase("Tổng số sách:", summaryFont));
        labelCell.setBorder(0);
        summaryTable.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(String.valueOf(books.size()), summaryFont));
        valueCell.setBorder(0);
        summaryTable.addCell(valueCell);
        
        labelCell = new PdfPCell(new Phrase("Tổng tồn kho:", summaryFont));
        labelCell.setBorder(0);
        summaryTable.addCell(labelCell);
        
        valueCell = new PdfPCell(new Phrase(String.valueOf(totalStock), summaryFont));
        valueCell.setBorder(0);
        summaryTable.addCell(valueCell);
        
        labelCell = new PdfPCell(new Phrase("Tổng giá trị tồn kho:", summaryFont));
        labelCell.setBorder(0);
        summaryTable.addCell(labelCell);
        
        valueCell = new PdfPCell(new Phrase(String.format("%,.0f VND", totalValue), summaryFont));
        valueCell.setBorder(0);
        summaryTable.addCell(valueCell);
        
        document.add(summaryTable);
        
        document.close();
        
        return baos.toByteArray();
    }
    
    // Import sách từ file PDF
    public List<BookImportResult> importBooksFromPdf(MultipartFile pdfFile) throws IOException {
        List<BookImportResult> results = new ArrayList<>();
        
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            // Phân tích text để tìm thông tin sách
            // Format mong đợi: Tiêu đề | Tác giả | Giá | Số lượng | Mô tả
            String[] lines = text.split("\\r?\\n");
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("BÁO CÁO") || line.startsWith("Ngày") || line.startsWith("STT")) {
                    continue;
                }
                
                // Tách các trường bằng dấu | hoặc tab
                String[] fields = line.split("\\|");
                if (fields.length < 3) {
                    fields = line.split("\\t");
                }
                
                if (fields.length >= 3) {
                    try {
                        Book book = new Book();
                        book.setTitle(fields[0].trim());
                        book.setAuthor(fields[1].trim());
                        book.setPrice(Double.parseDouble(fields[2].trim().replaceAll("[^\\d.]", "")));
                        
                        if (fields.length > 3) {
                            book.setStock(Integer.parseInt(fields[3].trim()));
                        } else {
                            book.setStock(0);
                        }
                        
                        if (fields.length > 4) {
                            book.setDescription(fields[4].trim());
                        }
                        
                        book.setActive(true);
                        
                        // Lưu vào database
                        Book savedBook = bookRepository.save(book);
                        results.add(new BookImportResult(true, "Đã thêm: " + savedBook.getTitle(), savedBook));
                    } catch (Exception e) {
                        results.add(new BookImportResult(false, "Lỗi: " + e.getMessage() + " - Dòng: " + line, null));
                    }
                }
            }
        }
        
        return results;
    }
    
    // Class kết quả import
    public static class BookImportResult {
        private boolean success;
        private String message;
        private Book book;
        
        public BookImportResult(boolean success, String message, Book book) {
            this.success = success;
            this.message = message;
            this.book = book;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Book getBook() { return book; }
    }
}
