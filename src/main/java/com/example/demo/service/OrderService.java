package com.example.demo.service;

import com.example.demo.entity.*;
        import com.example.demo.repository.*;
        import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderStatusChangeRepository orderStatusChangeRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // ✅ Get all orders with filters
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

    // ✅ Get single order
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    // ✅ Update order status + log change + send notification
    public String updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Update order
        order.setStatus(status);
        orderRepository.save(order);

        // Log status change
        OrderStatusChange change = new OrderStatusChange();
        change.setOrder(order);
        change.setStatus(status);
        change.setCreatedAt(LocalDateTime.now());
        orderStatusChangeRepository.save(change);

        // Create customer notification
        Notification notif = new Notification();
        notif.setCustomerId(order.getCustomerId());
        notif.setMessage("Your order #" + order.getId() + " status updated to: " + status);
        notificationRepository.save(notif);

        return "Order status updated and customer notified";
    }

    // ✅ Order details with items
    public Map<String, Object> getOrderDetails(Long id) {
        Order order = getOrderById(id);
        List<OrderItem> items = orderItemRepository.findByOrderId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("items", items);

        return response;
    }

    // ✅ Create order from cart
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

        // Save items
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

    // ✅ Customer orders
    public List<Order> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // ✅ Customer order details
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
