package com.anudeep.probeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionDTO {
    private Long id;
    private String name;
    private String description;
    private Long requestCount;
    private List<NamedRequestDTO> requests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
