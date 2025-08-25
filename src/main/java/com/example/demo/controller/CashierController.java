package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.CashierService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cashiers")
public class CashierController {

    private final CashierService cashierService;

    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    // ✅ Only ADMIN can create cashier
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createCashier(@RequestBody User cashier) {
        return ResponseEntity.ok(cashierService.createCashier(cashier));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllCashiers() {
        return ResponseEntity.ok(cashierService.getAllCashiers());
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateCashier(@PathVariable Long id, @RequestBody User cashier) {
        return ResponseEntity.ok(cashierService.updateCashier(id, cashier));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashier(@PathVariable Long id) {
        cashierService.deleteCashier(id);
        return ResponseEntity.noContent().build();
    }
}
