package com.hutech.bai5.controller;

import com.hutech.bai5.model.Invoice;
import com.hutech.bai5.service.CartService;
import com.hutech.bai5.service.InvoiceService;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Bài 5: Checkout Controller
 */
@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CartService cartService;
    private final InvoiceService invoiceService;

    @GetMapping
    public String showCheckoutForm(Model model) {
        if (cartService.getTotalItems() == 0) {
            return "redirect:/cart";
        }
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        return "cart/checkout";
    }

    @PostMapping
    public String processCheckout(
            @RequestParam @NotBlank String customerName,
            @RequestParam @Email String email,
            @RequestParam @NotBlank String phone,
            @RequestParam @NotBlank String address,
            Model model) {
        
        Invoice invoice = invoiceService.createInvoice(customerName, email, phone, address);
        model.addAttribute("invoice", invoice);
        return "cart/order-success";
    }
}
