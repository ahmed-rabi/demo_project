package com.example.demo.controller;

import com.example.demo.entity.Enquiry;
import com.example.demo.service.EnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {
    
    @Autowired
    private EnquiryService enquiryService;
    
    // Customer: Submit enquiry (FR-6)
    @PostMapping("/submit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> submitEnquiry(@RequestBody Map<String, Object> request) {
        try {
            String content = (String) request.get("content");
            Long customerId = Long.valueOf(request.get("customerId").toString());
            
            Enquiry enquiry = enquiryService.submitEnquiry(content, customerId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Enquiry submitted successfully",
                "enquiry", enquiry
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error submitting enquiry: " + e.getMessage()
            ));
        }
    }
    
    // Admin: View all enquiries (FR-32)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEnquiries() {
        try {
            List<Enquiry> enquiries = enquiryService.getAllEnquiries();
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "enquiries", enquiries,
                "total", enquiries.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error fetching enquiries: " + e.getMessage()
            ));
        }
    }
    
    // Admin: Delete inappropriate enquiry (moderation)
    @DeleteMapping("/admin/{enquiryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEnquiry(@PathVariable Long enquiryId) {
        try {
            boolean success = enquiryService.deleteEnquiry(enquiryId);
            if (success) {
                return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "Enquiry deleted successfully"
                ));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error deleting enquiry: " + e.getMessage()
            ));
        }
    }
    
    // Admin: Get single enquiry details
    @GetMapping("/admin/{enquiryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEnquiryById(@PathVariable Long enquiryId) {
        try {
            Optional<Enquiry> enquiry = enquiryService.getEnquiryById(enquiryId);
            if (enquiry.isPresent()) {
                return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "enquiry", enquiry.get()
                ));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error fetching enquiry: " + e.getMessage()
            ));
        }
    }
    
    // Customer: Get their own enquiries
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCustomerEnquiries(@PathVariable Long customerId) {
        try {
            List<Enquiry> enquiries = enquiryService.getEnquiriesByCustomer(customerId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "enquiries", enquiries,
                "total", enquiries.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error fetching enquiries: " + e.getMessage()
            ));
        }
    }
    
    // Admin: Get enquiries statistics
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEnquiriesStats() {
        try {
            long totalCount = enquiryService.getTotalEnquiriesCount();
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "totalEnquiries", totalCount
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Error fetching statistics: " + e.getMessage()
            ));
        }
    }
}