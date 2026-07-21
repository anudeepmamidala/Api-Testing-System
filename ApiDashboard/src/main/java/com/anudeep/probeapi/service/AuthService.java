package com.anudeep.probeapi.service;

import com.anudeep.probeapi.dto.AuthRequestDTO;
import com.anudeep.probeapi.dto.AuthResponseDTO;
import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.exception.CustomException;
import com.anudeep.probeapi.repository.UserRepository;
import com.anudeep.probeapi.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponseDTO register(AuthRequestDTO request) {
        // Validate input
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new CustomException("Username cannot be empty", "INVALID_INPUT", 400);
        }
        
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new CustomException("Email cannot be empty", "INVALID_INPUT", 400);
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new CustomException("Password cannot be empty", "INVALID_INPUT", 400);
        }

        // Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("Username already exists", "USER_EXISTS", 409);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already registered", "EMAIL_EXISTS", 409);
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        log.info("New user registered: {}", user.getUsername());

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .message("User registered successfully")
                .build();
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        // Validate input
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new CustomException("Username cannot be empty", "INVALID_INPUT", 400);
        }
        
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new CustomException("Password cannot be empty", "INVALID_INPUT", 400);
        }

        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("Invalid username or password", 
                    "INVALID_CREDENTIALS", 401));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new CustomException("Invalid username or password", "INVALID_CREDENTIALS", 401);
        }

        log.info("User logged in: {}", user.getUsername());

        // Generate JWT token
        String token = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .message("Login successful")
                .build();
    }

}
