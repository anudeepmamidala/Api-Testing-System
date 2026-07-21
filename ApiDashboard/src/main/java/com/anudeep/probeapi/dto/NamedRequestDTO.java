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
public class NamedRequestDTO {
    private Long id;
    private String name;
    private String description;
    private String method;
    private String url;
    private String body;
    private Map<String, String> headers;
    private Long collectionId;
    private String status; // SUCCESS, FAILED, PENDING
    private Integer responseCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
