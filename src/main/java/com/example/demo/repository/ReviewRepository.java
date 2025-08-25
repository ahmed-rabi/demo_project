package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Find reviews by customer ID
    List<Review> findByCustomerId(Long customerId);
    
    // Find reviews by order ID
    List<Review> findByOrderId(Long orderId);
    
    // Find reviews by rating
    List<Review> findByRating(Integer rating);
    
    // Check if customer already reviewed this order
    boolean existsByOrderIdAndCustomerId(Long orderId, Long customerId);
    
    // Get average rating for all reviews
    @Query("SELECT AVG(r.rating) FROM Review r")
    Double getAverageRating();
    
    // Get reviews ordered by creation date
    List<Review> findAllByOrderByCreatedAtDesc();
}