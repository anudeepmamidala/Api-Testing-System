package com.anudeep.probeapi.service;

import com.anudeep.probeapi.dto.ApiRequestDTO;
import com.anudeep.probeapi.dto.ApiResponseDTO;
import com.anudeep.probeapi.entity.HistoryEntry;
import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.exception.CustomException;
import com.anudeep.probeapi.repository.HistoryRepository;
import com.anudeep.probeapi.validation.RequestValidator;
import com.anudeep.probeapi.validation.SecurityValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private SecurityValidator securityValidator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponseDTO executeRequest(ApiRequestDTO request, User user) {
        try {
            // Validate request
            requestValidator.validateHttpMethod(request.getMethod());
            requestValidator.validateUrl(request.getUrl());
            securityValidator.validateSSRF(request.getUrl());
            requestValidator.validateRequestBody(request.getBody());

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            if (request.getHeaders() != null) {
                request.getHeaders().forEach(headers::set);
            }
            headers.set("Content-Type", "application/json");

            // Create HTTP request
            HttpEntity<?> httpRequest = new HttpEntity<>(request.getBody(), headers);
            
            // Execute request and measure latency
            long startTime = System.currentTimeMillis();
            ResponseEntity<String> response;
            
            try {
                response = restTemplate.exchange(
                    request.getUrl(),
                    HttpMethod.valueOf(request.getMethod().toUpperCase()),
                    httpRequest,
                    String.class
                );
            } catch (RestClientException e) {
                log.error("Error executing request to {}: {}", request.getUrl(), e.getMessage());
                throw new CustomException("External API error: " + e.getMessage(), "API_ERROR", 502);
            }

            long endTime = System.currentTimeMillis();
            long latencyMs = endTime - startTime;

            // Extract response headers
            Map<String, String> responseHeaders = new HashMap<>();
            response.getHeaders().forEach((key, values) -> {
                if (values != null && !values.isEmpty()) {
                    responseHeaders.put(key, values.get(0));
                }
            });

            // Save to history
            HistoryEntry entry = HistoryEntry.builder()
                    .user(user)
                    .method(request.getMethod())
                    .url(request.getUrl())
                    .requestBody(request.getBody())
                    .requestHeaders(serializeMap(request.getHeaders()))
                    .responseStatus(response.getStatusCodeValue())
                    .responseBody(response.getBody())
                    .responseHeaders(serializeMap(responseHeaders))
                    .latencyMs(latencyMs)
                    .build();

            entry = historyRepository.save(entry);
            log.info("Request executed and saved for user {}: {} {}", user.getUsername(), 
                request.getMethod(), request.getUrl());

            // Return response
            return ApiResponseDTO.builder()
                    .historyId(entry.getId())
                    .statusCode(response.getStatusCodeValue())
                    .body(response.getBody())
                    .headers(responseHeaders)
                    .latencyMs(latencyMs)
                    .method(request.getMethod())
                    .url(request.getUrl())
                    .build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error executing request: {}", e.getMessage(), e);
            throw new CustomException("Error executing request: " + e.getMessage(), 
                "EXECUTION_ERROR", 500);
        }
    }

    private String serializeMap(Map<String, String> map) {
        try {
            if (map == null) {
                return "{}";
            }
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            log.error("Error serializing map: {}", e.getMessage());
            return "{}";
        }
    }

}
