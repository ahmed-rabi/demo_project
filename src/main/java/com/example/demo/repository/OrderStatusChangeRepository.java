package com.example.demo.repository;

import com.example.demo.entity.OrderStatusChange;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderStatusChangeRepository extends JpaRepository<OrderStatusChange, Long> {
    List<OrderStatusChange> findByOrderId(Long orderId);
}
