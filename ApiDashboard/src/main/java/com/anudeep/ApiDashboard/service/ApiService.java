package com.anudeep.ApiDashboard.service;

import java.net.URI;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anudeep.ApiDashboard.dto.ApiRequestDTO;
import com.anudeep.ApiDashboard.dto.ApiResponseDTO;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiResponseDTO sendRequest(ApiRequestDTO request) {

        long start = System.currentTimeMillis();

        try {
            // ✅ BASIC VALIDATION
            if (request.getUrl() == null || request.getUrl().isEmpty()) {
                throw new IllegalArgumentException("URL cannot be empty");
            }

            if (request.getMethod() == null) {
                throw new IllegalArgumentException("Method is required");
            }

            logger.info("Sending {} request to {}", request.getMethod(), request.getUrl());

            // 🔹 Build headers
            HttpHeaders headers = new HttpHeaders();
            if (request.getHeaders() != null) {
                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                    headers.add(entry.getKey(), entry.getValue()); // ✅ FIXED
                }
            }

            // 🔹 Build request entity
            HttpEntity<String> entity;

            if ("GET".equalsIgnoreCase(request.getMethod())) {
                entity = new HttpEntity<>(headers);
            } else {
                entity = new HttpEntity<>(request.getBody(), headers);
            }

            // 🔹 Call external API
            ResponseEntity<String> response = restTemplate.exchange(
                    new URI(request.getUrl()),
                    HttpMethod.valueOf(request.getMethod().toUpperCase()),
                    entity,
                    String.class
            );

            long end = System.currentTimeMillis();

            return ApiResponseDTO.builder()
                    .status(response.getStatusCodeValue())
                    .body(response.getBody())
                    .timeTaken(end - start)
                    .error(null)
                    .build();

        }
        // ✅ HANDLE REAL API ERRORS (IMPORTANT)
        catch (HttpStatusCodeException e) {

            long end = System.currentTimeMillis();

            logger.error("API returned error status: {}", e.getStatusCode());

            return ApiResponseDTO.builder()
                    .status(e.getStatusCode().value())
                    .body(e.getResponseBodyAsString())
                    .timeTaken(end - start)
                    .error(null)
                    .build();
        }
        // ✅ GENERIC ERROR
        catch (Exception e) {

            long end = System.currentTimeMillis();

            logger.error("API call failed", e);

            return ApiResponseDTO.builder()
                    .status(500)
                    .body("")
                    .timeTaken(end - start)
                    .error(e.getMessage())
                    .build();
        }
    }
}