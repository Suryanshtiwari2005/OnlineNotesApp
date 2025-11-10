package com.notesApp.demo.controller;

import com.notesApp.demo.model.User;
import com.notesApp.demo.service.AuthService;
import com.notesApp.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    // ----------------------------
    // Register a new user
    // ----------------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.createUser(user);
            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "username", newUser.getUsername(),
                    "role", newUser.getRole()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ----------------------------
    // Login and generate JWT token
    // ----------------------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            String token = authService.login(username, password);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "message", "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}

