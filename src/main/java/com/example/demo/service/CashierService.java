package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CashierService {

    private final UserRepository userRepository;

    public CashierService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Create Cashier
    public User createCashier(User cashier) {
        cashier.setRole("CASHIER");
        return userRepository.save(cashier);
    }

    // ✅ Get all Cashiers
    public List<User> getAllCashiers() {
        return userRepository.findByRole("CASHIER");
    }

    // ✅ Get one Cashier
    public Optional<User> getCashierById(Long id) {
        return userRepository.findById(id)
                .filter(user -> "CASHIER".equals(user.getRole()));
    }

    // ✅ Update Cashier
    public User updateCashier(Long id, User cashierDetails) {
        return userRepository.findById(id).map(cashier -> {
            cashier.setName(cashierDetails.getName());
            cashier.setEmail(cashierDetails.getEmail());
            cashier.setPhone(cashierDetails.getPhone());
            cashier.setPassword(cashierDetails.getPassword());
            cashier.setRole("CASHIER"); // enforce role
            return userRepository.save(cashier);
        }).orElseThrow(() -> new RuntimeException("Cashier not found"));
    }

    // ✅ Delete Cashier
    public void deleteCashier(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            if ("CASHIER".equals(user.getRole())) {
                userRepository.delete(user);
            } else {
                throw new RuntimeException("User is not a cashier");
            }
        });
    }
}
