package com.anudeep.probeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryPageResponseDTO {
    private List<HistoryEntryDTO> data;
    private long totalCount;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
}
