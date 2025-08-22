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
}