package com.hutech.bai8.controller;

import com.hutech.bai8.model.Cart;
import com.hutech.bai8.model.Order;
import com.hutech.bai8.model.User;
import com.hutech.bai8.model.Voucher;
import com.hutech.bai8.service.MoMoService;
import com.hutech.bai8.service.OrderService;
import com.hutech.bai8.service.UserService;
import com.hutech.bai8.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CheckoutController {
    private final Cart cart;
    private final OrderService orderService;
    private final UserService userService;
    private final VoucherService voucherService;
    private final MoMoService momoService;

    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal UserDetails userDetails, Model model,
                              RedirectAttributes redirectAttributes) {
        // Kiem tra neu la ADMIN thi khong cho thanh toan
        User user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (user != null && user.hasRole("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Admin khong the thanh toan! Vui long dung tai khoan USER.");
            return "redirect:/books";
        }
        
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("items", cart.getItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("finalPrice", cart.getFinalPrice());
        model.addAttribute("discountAmount", cart.getDiscountAmount());
        model.addAttribute("appliedVoucher", cart.getAppliedVoucher());
        model.addAttribute("hasVoucher", cart.hasVoucher());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "checkout/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String customerName,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam String address,
                                 @RequestParam String paymentMethod,
                                 @RequestParam(required = false) String note,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(userDetails.getUsername()).orElse(null);
        
        // Kiem tra neu la ADMIN thi khong cho thanh toan
        if (user != null && user.hasRole("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Admin khong the thanh toan! Vui long dung tai khoan USER.");
            return "redirect:/books";
        }
        
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }
        
        // Nếu thanh toán MoMo
        if ("momo".equals(paymentMethod)) {
            try {
                String orderId = "ORDER" + System.currentTimeMillis();
                String orderInfo = "Thanh toán đơn hàng " + orderId;
                // Sử dụng giá sau khi giảm giá
                long amount = (long) cart.getFinalPrice();
                
                // Tạo đơn hàng trước với trạng thái PENDING
                Order order = orderService.createOrder(cart, user, customerName, email, phone, address, note, "PENDING", orderId);
                
                // Lưu thông tin voucher vào đơn hàng nếu có
                if (cart.hasVoucher()) {
                    Voucher voucher = cart.getAppliedVoucher();
                    order.setVoucherCode(voucher.getCode());
                    order.setDiscountAmount(cart.getDiscountAmount());
                    order.setOriginalAmount(cart.getTotalPrice());
                }
                
                // Tạo yêu cầu thanh toán MoMo
                String payUrl = momoService.createPaymentRequest(orderId, amount, orderInfo);
                
                // Lưu thông tin MoMo vào order
                order.setPaymentMethod("MOMO");
                order.setPaymentStatus("PENDING");
                orderService.saveOrder(order);
                
                // Redirect đến trang thanh toán MoMo
                return "redirect:" + payUrl;
                
            } catch (Exception e) {
                log.error("Lỗi khi tạo thanh toán MoMo: {}", e.getMessage());
                redirectAttributes.addFlashAttribute("error", "Không thể tạo thanh toán MoMo: " + e.getMessage());
                return "redirect:/checkout";
            }
        }
        
        // Nếu thanh toán COD
        Order order = orderService.createOrder(cart, user, customerName, email, phone, address, note, "PENDING", null);
        order.setPaymentMethod("COD");
        
        // Lưu thông tin voucher vào đơn hàng nếu có
        if (cart.hasVoucher()) {
            Voucher voucher = cart.getAppliedVoucher();
            order.setVoucherCode(voucher.getCode());
            order.setDiscountAmount(cart.getDiscountAmount());
            order.setOriginalAmount(cart.getTotalPrice());
            
            // Tăng số lần sử dụng voucher
            voucherService.incrementVoucherUsage(voucher.getId());
        }
        
        cart.clear();
        
        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công! Mã đơn hàng: " + order.getId());
        return "redirect:/orders/" + order.getId();
    }

    // Xử lý return từ MoMo sau khi thanh toán
    @GetMapping("/momo/return")
    public String momoReturn(@RequestParam String partnerCode,
                            @RequestParam String orderId,
                            @RequestParam String requestId,
                            @RequestParam String amount,
                            @RequestParam String orderInfo,
                            @RequestParam String orderType,
                            @RequestParam String transId,
                            @RequestParam String resultCode,
                            @RequestParam String message,
                            @RequestParam String payType,
                            @RequestParam String responseTime,
                            @RequestParam String extraData,
                            @RequestParam String signature,
                            RedirectAttributes redirectAttributes) {
        
        try {
            // Tìm đơn hàng theo MoMo order ID
            Order order = orderService.findByMomoOrderId(orderId).orElse(null);
            
            if (order == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
                return "redirect:/cart";
            }
            
            // Kiểm tra signature - truyền đúng tham số theo thứ tự mới
            boolean isValid = momoService.verifyIpnSignature(signature, amount, extraData, message,
                    orderId, orderInfo, partnerCode, payType, requestId, orderType,
                    responseTime, transId, resultCode);
            
            if (!isValid) {
                log.error("Invalid MoMo signature for order: {}", orderId);
                redirectAttributes.addFlashAttribute("error", "Chữ ký không hợp lệ!");
                return "redirect:/orders/" + order.getId();
            }
            
            // Xử lý kết quả thanh toán
            if ("0".equals(resultCode)) {
                // Thanh toán thành công
                order.setPaymentStatus("PAID");
                order.setMomoTransId(transId);
                order.setStatus("CONFIRMED");
                orderService.saveOrder(order);
                
                // Tăng số lần sử dụng voucher nếu có
                if (order.getVoucherCode() != null) {
                    voucherService.getVoucherByCode(order.getVoucherCode()).ifPresent(voucher -> {
                        voucherService.incrementVoucherUsage(voucher.getId());
                    });
                }
                
                // Xóa giỏ hàng
                cart.clear();
                
                redirectAttributes.addFlashAttribute("success", "Thanh toán MoMo thành công! Mã giao dịch: " + transId);
            } else {
                // Thanh toán thất bại
                order.setPaymentStatus("FAILED");
                order.setPaymentMessage(message);
                order.setStatus("CANCELLED");
                orderService.saveOrder(order);
                
                redirectAttributes.addFlashAttribute("error", "Thanh toán thất bại: " + message);
            }
            
            return "redirect:/orders/" + order.getId();
            
        } catch (Exception e) {
            log.error("Lỗi xử lý return từ MoMo: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Lỗi xử lý thanh toán!");
            return "redirect:/cart";
        }
    }

    // Xử lý IPN (Instant Payment Notification) từ MoMo
    @PostMapping("/momo/ipn")
    @ResponseBody
    public String momoIpn(@RequestParam String partnerCode,
                          @RequestParam String orderId,
                          @RequestParam String requestId,
                          @RequestParam String amount,
                          @RequestParam String orderInfo,
                          @RequestParam String orderType,
                          @RequestParam String transId,
                          @RequestParam String resultCode,
                          @RequestParam String message,
                          @RequestParam String payType,
                          @RequestParam String responseTime,
                          @RequestParam String extraData,
                          @RequestParam String signature) {
        
        try {
            log.info("MoMo IPN received: orderId={}, resultCode={}", orderId, resultCode);
            
            // Tìm đơn hàng
            Order order = orderService.findByMomoOrderId(orderId).orElse(null);
            
            if (order == null) {
                log.error("Order not found: {}", orderId);
                return "-1"; // Error
            }
            
            // Xử lý thanh toán thành công
            if ("0".equals(resultCode)) {
                order.setPaymentStatus("PAID");
                order.setMomoTransId(transId);
                if ("PENDING".equals(order.getStatus())) {
                    order.setStatus("CONFIRMED");
                }
                orderService.saveOrder(order);
                log.info("Payment successful for order: {}", orderId);
                return "0"; // Success
            } else {
                order.setPaymentStatus("FAILED");
                order.setPaymentMessage(message);
                orderService.saveOrder(order);
                log.warn("Payment failed for order: {}, reason: {}", orderId, message);
                return "-1";
            }
            
        } catch (Exception e) {
            log.error("Lỗi xử lý IPN từ MoMo: {}", e.getMessage());
            return "-1";
        }
    }
}
