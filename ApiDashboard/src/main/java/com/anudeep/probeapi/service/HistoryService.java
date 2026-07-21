package com.anudeep.probeapi.service;

import com.anudeep.probeapi.dto.HistoryEntryDTO;
import com.anudeep.probeapi.dto.HistoryPageResponseDTO;
import com.anudeep.probeapi.entity.HistoryEntry;
import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.exception.CustomException;
import com.anudeep.probeapi.repository.HistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public HistoryPageResponseDTO getHistory(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        Page<HistoryEntry> historyPage = historyRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        List<HistoryEntryDTO> entries = historyPage.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        return HistoryPageResponseDTO.builder()
                .data(entries)
                .totalCount(historyPage.getTotalElements())
                .pageNumber(page)
                .pageSize(size)
                .totalPages(historyPage.getTotalPages())
                .build();
    }

    public HistoryEntryDTO getHistoryEntry(User user, Long id) {
        HistoryEntry entry = historyRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("History entry not found", "NOT_FOUND", 404));
        return mapToDTO(entry);
    }

    public void clearHistory(User user) {
        historyRepository.deleteByUser(user);
        log.info("Cleared history for user {}", user.getUsername());
    }

    public void deleteHistoryEntry(User user, Long id) {
        HistoryEntry entry = historyRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CustomException("History entry not found", "NOT_FOUND", 404));
        historyRepository.delete(entry);
    }

    private HistoryEntryDTO mapToDTO(HistoryEntry entity) {
        return HistoryEntryDTO.builder()
                .id(entity.getId())
                .method(entity.getMethod())
                .url(entity.getUrl())
                .requestBody(entity.getRequestBody())
                .requestHeaders(deserializeMap(entity.getRequestHeaders()))
                .responseStatus(entity.getResponseStatus())
                .responseBody(entity.getResponseBody())
                .responseHeaders(deserializeMap(entity.getResponseHeaders()))
                .latencyMs(entity.getLatencyMs())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private Map<String, String> deserializeMap(String json) {
        try {
            if (json == null || json.isEmpty() || json.equals("{}")) {
                return new java.util.HashMap<>();
            }
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error("Error deserializing map: {}", e.getMessage());
            return new java.util.HashMap<>();
        }
    }
}
