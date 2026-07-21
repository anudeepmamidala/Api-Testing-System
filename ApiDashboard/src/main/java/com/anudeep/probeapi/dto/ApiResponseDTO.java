package com.anudeep.probeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDTO {
    private Long historyId;
    private Integer statusCode;
    private String body;
    private Map<String, String> headers;
    private Long latencyMs;
    private String method;
    private String url;
}
