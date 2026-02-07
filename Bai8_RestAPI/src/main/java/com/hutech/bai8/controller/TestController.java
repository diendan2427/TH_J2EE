package com.hutech.bai8.controller;

import com.hutech.bai8.model.User;
import com.hutech.bai8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    // Test endpoint - kiểm tra DB có user không
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // Test endpoint - kiểm tra user cụ thể
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Test endpoint - kiểm tra token có hợp lệ không
    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Token không hợp lệ hoặc đã hết hạn!");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Token hợp lệ!");
        response.put("username", userDetails.getUsername());
        response.put("authorities", userDetails.getAuthorities());
        
        return ResponseEntity.ok(response);
    }

    // Test endpoint - kiểm tra user role
    @GetMapping("/check-role")
    public ResponseEntity<?> checkRole(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Chưa đăng nhập!");
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Map<String, Object> response = new HashMap<>();
        response.put("username", userDetails.getUsername());
        response.put("isAdmin", isAdmin);
        response.put("roles", userDetails.getAuthorities());
        
        return ResponseEntity.ok(response);
    }

    // Test endpoint - kiểm tra DB connection
    @GetMapping("/db-status")
    public ResponseEntity<?> checkDbStatus() {
        try {
            long count = userRepository.count();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "OK");
            response.put("message", "Database connected!");
            response.put("userCount", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("message", "Database connection failed: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
