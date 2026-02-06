package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.Voucher;
import com.hutech.bai8.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {
    private final Cart cart;
    private final VoucherService voucherService;

    @PostMapping("/apply")
    public String applyVoucher(@RequestParam String voucherCode, RedirectAttributes redirectAttributes) {
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }

        String code = voucherCode.trim().toUpperCase();
        double orderAmount = cart.getTotalPrice();
        
        VoucherService.VoucherValidationResult result = voucherService.validateAndApplyVoucher(code, orderAmount);
        
        if (result.isValid()) {
            cart.applyVoucher(result.getVoucher(), result.getDiscountAmount());
            redirectAttributes.addFlashAttribute("success", 
                "Đã áp dụng voucher " + code + "! Giảm " + String.format("%,.0f", result.getDiscountAmount()) + " VND");
        } else {
            redirectAttributes.addFlashAttribute("error", result.getMessage());
        }
        
        return "redirect:/cart";
    }

    @GetMapping("/remove")
    public String removeVoucher(RedirectAttributes redirectAttributes) {
        if (cart.hasVoucher()) {
            String code = cart.getAppliedVoucher().getCode();
            cart.removeVoucher();
            redirectAttributes.addFlashAttribute("success", "Đã xóa voucher " + code);
        }
        return "redirect:/cart";
    }
}
