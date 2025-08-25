package com.example.demo.service;

import com.example.demo.entity.Enquiry;
import com.example.demo.repository.EnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EnquiryService {
    
    @Autowired
    private EnquiryRepository enquiryRepository;
    
    // Customer: Submit enquiry
    public Enquiry submitEnquiry(String content, Long customerId) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Enquiry content cannot be empty");
        }
        
        Enquiry enquiry = new Enquiry(content.trim(), customerId);
        return enquiryRepository.save(enquiry);
    }
    
    // Admin: Get all enquiries
    public List<Enquiry> getAllEnquiries() {
        return enquiryRepository.findAllByOrderByIdDesc();
    }
    
    // Admin: Delete inappropriate enquiry (since we can't hide)
    public boolean deleteEnquiry(Long enquiryId) {
        if (enquiryRepository.existsById(enquiryId)) {
            enquiryRepository.deleteById(enquiryId);
            return true;
        }
        return false;
    }
    
    // Get enquiry by ID
    public Optional<Enquiry> getEnquiryById(Long id) {
        return enquiryRepository.findById(id);
    }
    
    // Get enquiries by customer
    public List<Enquiry> getEnquiriesByCustomer(Long customerId) {
        return enquiryRepository.findByCustomerIdOrderByIdDesc(customerId);
    }
    
    // Get total count of enquiries
    public long getTotalEnquiriesCount() {
        return enquiryRepository.count();
    }
}