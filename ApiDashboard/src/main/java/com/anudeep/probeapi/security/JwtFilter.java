package com.anudeep.probeapi.security;

import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // Skip JWT filter for public endpoints and CORS preflight
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // Allow CORS preflight requests
        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Skip JWT filter for auth endpoints and health
        if (path.contains("/auth/") || path.contains("/health") || path.contains("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                if (jwtService.isTokenValid(token)) {
                    String username = jwtService.getUsernameFromToken(token);
                    Long userId = jwtService.getUserIdFromToken(token);
                    
                    if (username != null) {
                        User user = userRepository.findById(userId).orElse(null);
                        if (user == null) {
                            filterChain.doFilter(request, response);
                            return;
                        }

                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        authentication.setDetails(userId);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        request.setAttribute("userId", userId);
                        request.setAttribute("username", username);
                        request.setAttribute("user", user);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

}
