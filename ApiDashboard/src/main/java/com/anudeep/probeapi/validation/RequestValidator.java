package com.anudeep.probeapi.validation;

import com.anudeep.probeapi.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
@Slf4j
public class RequestValidator {

    private static final int MAX_URL_LENGTH = 2048;
    private static final int MAX_BODY_SIZE_MB = 10;
    private static final long MAX_BODY_SIZE_BYTES = MAX_BODY_SIZE_MB * 1024 * 1024L;

    public void validateUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new CustomException("URL cannot be empty", "INVALID_URL", 400);
        }

        if (url.length() > MAX_URL_LENGTH) {
            throw new CustomException("URL exceeds maximum length of " + MAX_URL_LENGTH + " characters", 
                "INVALID_URL", 400);
        }

        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            
            if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
                throw new CustomException("URL must use http or https scheme", "INVALID_URL", 400);
            }
            
            if (uri.getHost() == null) {
                throw new CustomException("URL must contain a valid host", "INVALID_URL", 400);
            }
        } catch (URISyntaxException e) {
            throw new CustomException("Invalid URL format: " + e.getMessage(), "INVALID_URL", 400);
        }
    }

    public void validateRequestBody(String body) {
        if (body == null || body.isEmpty()) {
            return; // Body is optional
        }

        if (body.length() > MAX_BODY_SIZE_BYTES) {
            throw new CustomException("Request body exceeds maximum size of " + MAX_BODY_SIZE_MB + "MB", 
                "INVALID_BODY", 400);
        }

        // Basic JSON validation
        if (!isValidJson(body)) {
            throw new CustomException("Request body must be valid JSON", "INVALID_JSON", 400);
        }
    }

    public void validateHttpMethod(String method) {
        if (method == null || method.isEmpty()) {
            throw new CustomException("HTTP method cannot be empty", "INVALID_METHOD", 400);
        }

        String methodUpper = method.toUpperCase();
        if (!methodUpper.matches("^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS)$")) {
            throw new CustomException("Invalid HTTP method: " + method, "INVALID_METHOD", 400);
        }
    }

    private boolean isValidJson(String json) {
        try {
            // Simple JSON validation - check for balance of braces and brackets
            int braceCount = 0;
            int bracketCount = 0;
            
            for (char c : json.toCharArray()) {
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                else if (c == '[') bracketCount++;
                else if (c == ']') bracketCount--;
                
                if (braceCount < 0 || bracketCount < 0) return false;
            }
            
            return braceCount == 0 && bracketCount == 0;
        } catch (Exception e) {
            return false;
        }
    }

}
