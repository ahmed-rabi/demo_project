package com.example.demo.repository;

import com.example.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByTimestampsBetween(Date startDate, Date endDate);
    List<Order> findByStatusAndTimestampsBetween(String status, Date startDate, Date endDate);
    Optional<Order> findByCustomerIdAndStatus(Long customerId, String status);
}