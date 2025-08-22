package com.example.demo.service;

import com.example.demo.dto.SignupRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.config.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtils.generateToken(user.getEmail(), user.getRole());
    }

    public void signup(SignupRequest req) {
        if (userRepository.existsByEmail(req.getEmail()) || userRepository.existsByPhone(req.getPhone())) {
            throw new RuntimeException("Email/Phone already exists");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPassword(passwordEncoder.encode(req.getPassword()));


        if (req.getRole() == null || req.getRole().isEmpty()) {
            user.setRole("CUSTOMER");
        } else {
            String roleUpper = req.getRole().toUpperCase();
            if (roleUpper.equals("CASHIER") || roleUpper.equals("ADMIN")) {
                user.setRole(roleUpper);
            } else {
                user.setRole("CUSTOMER");
            }
        }

        userRepository.save(user);
    }

    // Inside AuthService
    public String getUserRole(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole();
    }
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }




}
