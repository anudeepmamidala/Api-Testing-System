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
public class HistoryEntryDTO {
    private Long id;
    private String method;
    private String url;
    private String requestBody;
    private Map<String, String> requestHeaders;
    private Integer responseStatus;
    private String responseBody;
    private Map<String, String> responseHeaders;
    private Long latencyMs;
    private LocalDateTime createdAt;
}
