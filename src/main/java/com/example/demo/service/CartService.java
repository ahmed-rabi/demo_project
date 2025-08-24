package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    // In-memory cart storage
    private final Map<Long, Map<Long, CartItem>> customerCarts = new HashMap<>();
    // Add this method to CartService.java
    public Map<Long, Map<Long, CartItem>> getCustomerCarts() {
        return customerCarts;
    }
    public Map<String, Object> getCart(Long customerId) {
        Map<Long, CartItem> cart = customerCarts.getOrDefault(customerId, new HashMap<>());
        return convertToResponse(cart);
    }
    public Map<Long, CartItem> getCartMap(Long customerId) {
        return customerCarts.getOrDefault(customerId, new HashMap<>());
    }
    public Map<String, Object> addToCart(Long customerId, Long productId, int quantity) {
        Map<Long, CartItem> cart = customerCarts.computeIfAbsent(customerId, k -> new HashMap<>());

        if (cart.containsKey(productId)) {
            CartItem existingItem = cart.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setSubtotal(existingItem.getPrice() * existingItem.getQuantity());
        } else {
            // For demo, we'll create a simple cart item
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setProductName("Product " + productId);
            newItem.setProductImage("/images/product" + productId + ".jpg");
            newItem.setPrice(10 * productId.intValue()); // Demo price
            newItem.setQuantity(quantity);
            newItem.setSubtotal(newItem.getPrice() * quantity);
            cart.put(productId, newItem);
        }

        return convertToResponse(cart);
    }

    public Map<String, Object> updateItemQuantity(Long customerId, Long productId, int quantity) {
        if (!customerCarts.containsKey(customerId)) {
            throw new RuntimeException("Cart not found for customer");
        }

        Map<Long, CartItem> cart = customerCarts.get(customerId);

        if (!cart.containsKey(productId)) {
            throw new RuntimeException("Item not found in cart");
        }

        if (quantity <= 0) {
            cart.remove(productId); // Remove item if quantity is 0 or less
        } else {
            CartItem item = cart.get(productId);
            item.setQuantity(quantity);
            item.setSubtotal(item.getPrice() * quantity);
        }

        return convertToResponse(cart);
    }

    public void removeFromCart(Long customerId, Long productId) {
        if (!customerCarts.containsKey(customerId)) {
            throw new RuntimeException("Cart not found for customer");
        }

        Map<Long, CartItem> cart = customerCarts.get(customerId);

        if (!cart.containsKey(productId)) {
            throw new RuntimeException("Item not found in cart");
        }

        cart.remove(productId);
    }

    public void clearCart(Long customerId) {
        customerCarts.remove(customerId);
    }

    private Map<String, Object> convertToResponse(Map<Long, CartItem> cart) {
        Map<String, Object> response = new HashMap<>();

        int totalPrice = cart.values().stream()
                .mapToInt(CartItem::getSubtotal)
                .sum();

        response.put("items", cart.values());
        response.put("totalPrice", totalPrice);

        return response;
    }

    // Internal cart item class
    public static class CartItem {
        private Long productId;
        private String productName;
        private String productImage;
        private Integer price;
        private Integer quantity;
        private Integer subtotal;

        // Getters and setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getProductImage() { return productImage; }
        public void setProductImage(String productImage) { this.productImage = productImage; }

        public Integer getPrice() { return price; }
        public void setPrice(Integer price) { this.price = price; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Integer getSubtotal() { return subtotal; }
        public void setSubtotal(Integer subtotal) { this.subtotal = subtotal; }
    }
}