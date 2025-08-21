package com.example.demo.controller;

import com.example.demo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.OrderService;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        return ResponseEntity.ok(orderService.getAllOrders(status, startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderDetails(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
                                               @RequestParam String status) {
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}