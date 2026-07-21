package com.anudeep.probeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponseDTO {
    private Long jobId;
    private String status;
    private Integer responseStatus;
    private String responseBody;
    private Map<String, String> responseHeaders;
    private Long latencyMs;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
