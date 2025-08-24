package com.example.demo.controller;

import com.example.demo.entity.Review;
import com.example.demo.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    private final ReviewService reviewService;
    
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    
    // TEST ENDPOINT - Simple test without database
    @PostMapping("/test")
    public ResponseEntity<String> testPost(@RequestBody String data) {
        System.out.println("=== SIMPLE TEST ENDPOINT ===");
        System.out.println("Test endpoint reached with data: " + data);
        return ResponseEntity.ok("Test successful: " + data);
    }
    
    // TEST ENDPOINT - Test Review object without saving
    @PostMapping("/test-review")
    public ResponseEntity<Review> testReviewPost(@RequestBody Review review) {
        System.out.println("=== REVIEW TEST ENDPOINT ===");
        System.out.println("Review test endpoint reached!");
        System.out.println("Review object: " + review);
        return ResponseEntity.ok(review);  // Just return what was sent
    }
    
    // MAIN CREATE ENDPOINT - With debugging
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        System.out.println("=== CREATE REVIEW DEBUG ===");
        System.out.println("Main create endpoint reached!");
        System.out.println("Review object received: " + review);
        
        if (review == null) {
            System.out.println("ERROR: Review object is null!");
            return ResponseEntity.badRequest().build();
        }
        
        System.out.println("Review details:");
        System.out.println("- Order ID: " + review.getOrderId());
        System.out.println("- Customer ID: " + review.getCustomerId());
        System.out.println("- Rating: " + review.getRating());
        System.out.println("- Comment: " + review.getComment());
        System.out.println("- Created At: " + review.getCreatedAt());
        
        try {
            System.out.println("Calling reviewService.createReview()...");
            Review createdReview = reviewService.createReview(review);
            System.out.println("SUCCESS! Review created: " + createdReview);
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            System.err.println("ERROR in createReview: " + e.getMessage());
            System.err.println("Exception type: " + e.getClass().getSimpleName());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    // ALTERNATIVE CREATE WITHOUT SECURITY - For testing
    @PostMapping("/no-auth")
    public ResponseEntity<Review> createReviewNoAuth(@RequestBody Review review) {
        System.out.println("=== CREATE REVIEW WITHOUT AUTH ===");
        System.out.println("No-auth endpoint reached!");
        System.out.println("Review object: " + review);
        
        try {
            Review createdReview = reviewService.createReview(review);
            System.out.println("Review created without auth: " + createdReview);
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            System.err.println("ERROR in no-auth create: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Get all reviews (This works - keep as is)
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        System.out.println("=== GET ALL REVIEWS ===");
        List<Review> reviews = reviewService.getAllReviews();
        System.out.println("Found " + reviews.size() + " reviews");
        return ResponseEntity.ok(reviews);
    }
    
    // Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        System.out.println("=== GET REVIEW BY ID: " + id + " ===");
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    // Get reviews by customer
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<List<Review>> getReviewsByCustomer(@PathVariable Long customerId) {
        List<Review> reviews = reviewService.getReviewsByCustomer(customerId);
        return ResponseEntity.ok(reviews);
    }
    
    // Get reviews by order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Review>> getReviewsByOrder(@PathVariable Long orderId) {
        List<Review> reviews = reviewService.getReviewsByOrder(orderId);
        return ResponseEntity.ok(reviews);
    }
    
    // Get average rating
    @GetMapping("/average-rating")
    public ResponseEntity<Double> getAverageRating() {
        Double averageRating = reviewService.getAverageRating();
        return ResponseEntity.ok(averageRating);
    }
    
    // Update review
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        try {
            Review updatedReview = reviewService.updateReview(id, reviewDetails);
            return ResponseEntity.ok(updatedReview);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Delete review
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}