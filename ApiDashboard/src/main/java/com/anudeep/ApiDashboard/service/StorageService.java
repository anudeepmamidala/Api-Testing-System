package com.anudeep.ApiDashboard.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.anudeep.ApiDashboard.dto.*;

@Service
public class StorageService {

    private static final int MAX_HISTORY = 50;

    private final List<HistoryEntryDTO> history = new ArrayList<>();
    private final Map<String, NamedRequestDTO> namedRequests = new HashMap<>();

    // ================= HISTORY =================

    public void addToHistory(ApiRequestDTO request, ApiResponseDTO response) {

        HistoryEntryDTO entry = new HistoryEntryDTO();

        entry.setId(UUID.randomUUID().toString());
        entry.setUrl(request.getUrl());
        entry.setMethod(request.getMethod());
        entry.setHeaders(request.getHeaders());
        entry.setBody(request.getBody());
        entry.setStatusCode(response.getStatus());
        entry.setTimestamp(System.currentTimeMillis());

        history.add(0, entry);

        // limit size
        if (history.size() > MAX_HISTORY) {
            history.remove(history.size() - 1);
        }
    }

    public List<HistoryEntryDTO> getHistory() {
        return new ArrayList<>(history);
    }

    public void clearHistory() {
        history.clear();
    }

    // ================= NAMED REQUESTS =================

    public NamedRequestDTO saveNamedRequest(NamedRequestDTO request) {

        if (request.getId() == null) {
            request.setId(UUID.randomUUID().toString());
        }

        request.setCreatedAt(System.currentTimeMillis());

        namedRequests.put(request.getId(), request);

        return request;
    }

    public List<NamedRequestDTO> getAllNamedRequests() {
        return new ArrayList<>(namedRequests.values());
    }
}