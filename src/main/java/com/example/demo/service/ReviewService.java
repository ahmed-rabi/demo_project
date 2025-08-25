package com.example.demo.service;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    
    // Create a new review
    public Review createReview(Review review) {
        // Check if customer already reviewed this order
        if (reviewRepository.existsByOrderIdAndCustomerId(review.getOrderId(), review.getCustomerId())) {
            throw new RuntimeException("Customer has already reviewed this order");
        }
        
        // Validate rating is between 1-5
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        
        return reviewRepository.save(review);
    }
    
    // Get all reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // Get review by ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }
    
    // Get reviews by customer
    public List<Review> getReviewsByCustomer(Long customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }
    
    // Get reviews by order
    public List<Review> getReviewsByOrder(Long orderId) {
        return reviewRepository.findByOrderId(orderId);
    }
    
    // Get average rating
    public Double getAverageRating() {
        Double avg = reviewRepository.getAverageRating();
        return avg != null ? avg : 0.0;
    }
    
    // Update review
    public Review updateReview(Long id, Review reviewDetails) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setRating(reviewDetails.getRating());
            review.setComment(reviewDetails.getComment());
            return reviewRepository.save(review);
        } else {
            throw new RuntimeException("Review not found with id: " + id);
        }
    }
    
    // Delete review
    public void deleteReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        } else {
            throw new RuntimeException("Review not found with id: " + id);
        }
    }
}