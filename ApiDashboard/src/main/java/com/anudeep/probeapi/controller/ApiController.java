package com.anudeep.probeapi.controller;

import com.anudeep.probeapi.dto.ApiRequestDTO;
import com.anudeep.probeapi.dto.ApiResponseDTO;
import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
@Slf4j
public class ApiController {

    @Autowired
    private ApiService apiService;

    @PostMapping("/execute")
    public ResponseEntity<ApiResponseDTO> executeRequest(
            @RequestBody ApiRequestDTO request,
            @RequestAttribute("user") User user) {
        log.info("Executing request for user {}: {} {}", user.getUsername(), 
            request.getMethod(), request.getUrl());

        ApiResponseDTO response = apiService.executeRequest(request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Request service is healthy");
    }

}
