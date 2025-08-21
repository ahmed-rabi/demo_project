package com.example.demo.Controller;



import com.example.demo.Service.AuthService;
import com.example.demo.Dto.LoginRequest;
import com.example.demo.Dto.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());
            String role = authService.getUserRole(request.getEmail());
            return ResponseEntity.ok(new AuthResponse(token, role, "Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            authService.signup(request);
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    public static class AuthResponse {
        private String token;
        private String role;
        private String message;

        public AuthResponse(String token, String role, String message) {
            this.token = token;
            this.role = role;
            this.message = message;
        }

        public String getToken() { return token; }
        public String getRole() { return role; }
        public String getMessage() { return message; }
    }
}
