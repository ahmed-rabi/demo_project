package com.example.demo.repository;

import com.example.demo.entity.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    
    // Find all enquiries ordered by ID (newest first)
    List<Enquiry> findAllByOrderByIdDesc();
    
    // Find enquiries by customer
    List<Enquiry> findByCustomerIdOrderByIdDesc(Long customerId);
}