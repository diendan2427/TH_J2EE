package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.Voucher;
import com.hutech.bai8.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/vouchers")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminVoucherController {
    private final VoucherService voucherService;
    private final Cart cart;

    @GetMapping
    public String listVouchers(Model model) {
        model.addAttribute("vouchers", voucherService.getAllVouchers());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/vouchers/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/vouchers/form";
    }

    @PostMapping("/add")
    public String addVoucher(@RequestParam String code,
                            @RequestParam String description,
                            @RequestParam Double discountPercent,
                            @RequestParam(required = false) Double maxDiscountAmount,
                            @RequestParam(required = false) Double minOrderAmount,
                            @RequestParam(required = false) Integer maxUses,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                            @RequestParam(defaultValue = "true") boolean active,
                            RedirectAttributes redirectAttributes) {
        
        // Kiểm tra code đã tồn tại chưa
        if (voucherService.existsByCode(code)) {
            redirectAttributes.addFlashAttribute("error", "Mã voucher '" + code + "' đã tồn tại!");
            return "redirect:/admin/vouchers/add";
        }
        
        // Validate discount percent
        if (discountPercent <= 0 || discountPercent > 100) {
            redirectAttributes.addFlashAttribute("error", "Phần trăm giảm giá phải từ 1% đến 100%!");
            return "redirect:/admin/vouchers/add";
        }
        
        Voucher voucher = new Voucher();
        voucher.setCode(code);
        voucher.setDescription(description);
        voucher.setDiscountPercent(discountPercent);
        voucher.setMaxDiscountAmount(maxDiscountAmount);
        voucher.setMinOrderAmount(minOrderAmount != null ? minOrderAmount : 0);
        voucher.setMaxUses(maxUses);
        voucher.setStartDate(startDate);
        voucher.setEndDate(endDate);
        voucher.setActive(active);
        
        voucherService.saveVoucher(voucher);
        redirectAttributes.addFlashAttribute("success", "Thêm voucher thành công!");
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Voucher voucher = voucherService.getVoucherById(id).orElse(null);
        if (voucher == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy voucher!");
            return "redirect:/admin/vouchers";
        }
        
        model.addAttribute("voucher", voucher);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "admin/vouchers/form";
    }

    @PostMapping("/edit/{id}")
    public String updateVoucher(@PathVariable String id,
                               @RequestParam String code,
                               @RequestParam String description,
                               @RequestParam Double discountPercent,
                               @RequestParam(required = false) Double maxDiscountAmount,
                               @RequestParam(required = false) Double minOrderAmount,
                               @RequestParam(required = false) Integer maxUses,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                               @RequestParam(defaultValue = "true") boolean active,
                               RedirectAttributes redirectAttributes) {
        
        Voucher existingVoucher = voucherService.getVoucherById(id).orElse(null);
        if (existingVoucher == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy voucher!");
            return "redirect:/admin/vouchers";
        }
        
        // Kiểm tra code mới có trùng với voucher khác không
        if (!code.equalsIgnoreCase(existingVoucher.getCode()) && voucherService.existsByCode(code)) {
            redirectAttributes.addFlashAttribute("error", "Mã voucher '" + code + "' đã tồn tại!");
            return "redirect:/admin/vouchers/edit/" + id;
        }
        
        // Validate discount percent
        if (discountPercent <= 0 || discountPercent > 100) {
            redirectAttributes.addFlashAttribute("error", "Phần trăm giảm giá phải từ 1% đến 100%!");
            return "redirect:/admin/vouchers/edit/" + id;
        }
        
        existingVoucher.setCode(code);
        existingVoucher.setDescription(description);
        existingVoucher.setDiscountPercent(discountPercent);
        existingVoucher.setMaxDiscountAmount(maxDiscountAmount);
        existingVoucher.setMinOrderAmount(minOrderAmount != null ? minOrderAmount : 0);
        existingVoucher.setMaxUses(maxUses);
        existingVoucher.setStartDate(startDate);
        existingVoucher.setEndDate(endDate);
        existingVoucher.setActive(active);
        
        voucherService.saveVoucher(existingVoucher);
        redirectAttributes.addFlashAttribute("success", "Cập nhật voucher thành công!");
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable String id, RedirectAttributes redirectAttributes) {
        voucherService.deleteVoucher(id);
        redirectAttributes.addFlashAttribute("success", "Xóa voucher thành công!");
        return "redirect:/admin/vouchers";
    }
    
    @GetMapping("/toggle/{id}")
    public String toggleVoucherStatus(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Voucher voucher = voucherService.getVoucherById(id).orElse(null);
        if (voucher != null) {
            voucher.setActive(!voucher.isActive());
            voucherService.saveVoucher(voucher);
            String status = voucher.isActive() ? "kích hoạt" : "vô hiệu hóa";
            redirectAttributes.addFlashAttribute("success", "Đã " + status + " voucher!");
        }
        return "redirect:/admin/vouchers";
    }
}
