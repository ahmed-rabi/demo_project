package com.example.demo.controller;

import com.example.demo.config.JwtUtils;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.CartService;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtils jwtUtils;   // ✅ inject JWT utils

    @Autowired
    private UserRepository userRepository;  // ✅ to fetch user

    @Autowired
    private HttpServletRequest request;  // ✅ to read token from headers

    // ... all your endpoints remain the same

    private Long getCurrentCustomerId() {
        //
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);


        String email = jwtUtils.extractEmail(token);


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }


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

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestParam Long tableId) {
        try {
            Long customerId = getCurrentCustomerId();
            // Access the cart items directly from the service's internal storage
            Map<Long, CartService.CartItem> cart = cartService.getCustomerCarts().getOrDefault(customerId, new java.util.HashMap<>());
            Order order = orderService.createOrder(customerId, tableId, cart);
            cartService.clearCart(customerId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getCustomerOrders() {
        try {
            Long customerId = getCurrentCustomerId();
            return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getCustomerOrder(@PathVariable Long orderId) {
        try {
            Long customerId = getCurrentCustomerId();
            return ResponseEntity.ok(orderService.getCustomerOrderDetails(orderId, customerId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}