package com.hutech.bai8.service;

import com.hutech.bai8.model.Voucher;
import com.hutech.bai8.repository.VoucherRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }
    
    public Optional<Voucher> getVoucherById(String id) {
        return voucherRepository.findById(id);
    }
    
    public Optional<Voucher> getVoucherByCode(String code) {
        return voucherRepository.findByCode(code.toUpperCase());
    }
    
    public Voucher saveVoucher(Voucher voucher) {
        if (voucher.getId() == null) {
            voucher.setCreatedAt(LocalDateTime.now());
            voucher.setUsedCount(0);
        }
        // Chuyển code thành uppercase để dễ tìm kiếm
        voucher.setCode(voucher.getCode().toUpperCase());
        return voucherRepository.save(voucher);
    }
    
    public void deleteVoucher(String id) {
        voucherRepository.deleteById(id);
    }
    
    public boolean existsByCode(String code) {
        return voucherRepository.existsByCode(code.toUpperCase());
    }
    
    // Kiểm tra và áp dụng voucher
    public VoucherValidationResult validateAndApplyVoucher(String code, double orderAmount) {
        Optional<Voucher> voucherOpt = getVoucherByCode(code);
        
        if (voucherOpt.isEmpty()) {
            return new VoucherValidationResult(false, "Mã voucher không tồn tại!", 0, null);
        }
        
        Voucher voucher = voucherOpt.get();
        
        if (!voucher.isValid()) {
            if (!voucher.isActive()) {
                return new VoucherValidationResult(false, "Voucher này đã bị vô hiệu hóa!", 0, null);
            }
            if (voucher.getMaxUses() != null && voucher.getUsedCount() >= voucher.getMaxUses()) {
                return new VoucherValidationResult(false, "Voucher này đã hết lượt sử dụng!", 0, null);
            }
            if (voucher.getEndDate() != null && LocalDateTime.now().isAfter(voucher.getEndDate())) {
                return new VoucherValidationResult(false, "Voucher này đã hết hạn!", 0, null);
            }
            if (voucher.getStartDate() != null && LocalDateTime.now().isBefore(voucher.getStartDate())) {
                return new VoucherValidationResult(false, "Voucher này chưa có hiệu lực!", 0, null);
            }
            return new VoucherValidationResult(false, "Voucher không hợp lệ!", 0, null);
        }
        
        if (!voucher.isApplicable(orderAmount)) {
            return new VoucherValidationResult(false, 
                "Đơn hàng tối thiểu phải từ " + String.format("%,.0f", voucher.getMinOrderAmount()) + " VND!", 
                0, null);
        }
        
        double discount = voucher.calculateDiscount(orderAmount);
        return new VoucherValidationResult(true, "Áp dụng voucher thành công!", discount, voucher);
    }
    
    // Tăng số lần sử dụng voucher
    public void incrementVoucherUsage(String voucherId) {
        Optional<Voucher> voucherOpt = voucherRepository.findById(voucherId);
        voucherOpt.ifPresent(voucher -> {
            voucher.incrementUsedCount();
            voucherRepository.save(voucher);
        });
    }
    
    // Class để trả về kết quả validation (thay thế cho record của Java 14+)
    @Data
    @AllArgsConstructor
    public static class VoucherValidationResult {
        private boolean valid;
        private String message;
        private double discountAmount;
        private Voucher voucher;
    }
}
