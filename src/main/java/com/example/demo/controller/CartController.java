package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart() {
        try {
            Long customerId = getCurrentCustomerId();
            return ResponseEntity.ok(cartService.getCart(customerId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/test")
    public String testEndpoint() {
        return "Cart controller is working!";
    }
    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@RequestParam Long productId,
                                       @RequestParam int quantity) {
        try {
            Long customerId = getCurrentCustomerId();
            return ResponseEntity.ok(cartService.addToCart(customerId, productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<?> updateItemQuantity(@PathVariable Long productId,
                                                @RequestParam int quantity) {
        try {
            Long customerId = getCurrentCustomerId();
            return ResponseEntity.ok(cartService.updateItemQuantity(customerId, productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId) {
        try {
            Long customerId = getCurrentCustomerId();
            cartService.removeFromCart(customerId, productId);
            return ResponseEntity.ok("Item removed from cart");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        try {
            Long customerId = getCurrentCustomerId();
            cartService.clearCart(customerId);
            return ResponseEntity.ok("Cart cleared");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Long getCurrentCustomerId() {
        // In a real application, you would get this from the authentication context
        // For demo purposes, we'll use a fixed customer ID
        return 1L;
    }
}