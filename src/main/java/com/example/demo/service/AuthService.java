package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String authenticateAndGenerateToken(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user != null) {
            return jwtUtil.generateToken(user.getEmail(), user.getRole());
        }
        return null;
    }

    public boolean isValidAdminToken(String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }
            
            String jwt = token.substring(7); // Remove "Bearer "
            String email = jwtUtil.extractEmail(jwt);
            String role = jwtUtil.extractRole(jwt);
            
            return "admin".equals(role) && jwtUtil.validateToken(jwt, email);
        } catch (Exception e) {
            return false;
        }
    }
}