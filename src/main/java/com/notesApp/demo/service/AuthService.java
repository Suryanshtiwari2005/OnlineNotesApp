package com.notesApp.demo.service;



import com.notesApp.demo.model.Role;
import com.notesApp.demo.model.User;
import com.notesApp.demo.repository.UserRepository;
import com.notesApp.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // -------------------------------
    // REGISTER NEW USER
    // -------------------------------
    public String register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        return "User registered successfully!";
    }

    // -------------------------------
    // LOGIN AND GENERATE JWT TOKEN
    // -------------------------------
    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(auth->auth.getAuthority())
                    .orElse("ROLE_USER");
            return jwtUtil.generateToken(username,role);
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
