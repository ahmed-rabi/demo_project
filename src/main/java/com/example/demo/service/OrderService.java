package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getAllOrders(String status, Date startDate, Date endDate) {
        if (status != null && startDate != null && endDate != null) {
            return orderRepository.findByStatusAndTimestampsBetween(status, startDate, endDate);
        } else if (status != null) {
            return orderRepository.findByStatus(status);
        } else if (startDate != null && endDate != null) {
            return orderRepository.findByTimestampsBetween(startDate, endDate);
        } else {
            return orderRepository.findAll();
        }
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Map<String, Object> getOrderDetails(Long id) {
        Order order = getOrderById(id);
        List<OrderItem> items = orderItemRepository.findByOrderId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("items", items);

        return response;
    }

    // New method to create an order from cart items
    public Order createOrder(Long customerId, Long tableId, Map<Long, CartService.CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setTableId(tableId);
        order.setStatus("pending");
        order.setTimestamps(new Date());

        int totalPrice = cartItems.values().stream()
                .mapToInt(CartService.CartItem::getSubtotal)
                .sum();
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartService.CartItem cartItem : cartItems.values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);
        return savedOrder;
    }

    // New method to get all orders for a customer
    public List<Order> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // New method to get order details for a specific customer
    public Map<String, Object> getCustomerOrderDetails(Long orderId, Long customerId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("items", items);

        return response;
    }
}