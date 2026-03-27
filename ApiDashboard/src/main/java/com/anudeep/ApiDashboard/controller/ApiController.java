package com.anudeep.ApiDashboard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anudeep.ApiDashboard.dto.ApiRequestDTO;
import com.anudeep.ApiDashboard.dto.ApiResponseDTO;
import com.anudeep.ApiDashboard.dto.HistoryEntryDTO;
import com.anudeep.ApiDashboard.dto.NamedRequestDTO;
import com.anudeep.ApiDashboard.service.ApiService;
import com.anudeep.ApiDashboard.service.StorageService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;
    private final StorageService storageService;

    // Constructor Injection (better than @Autowired)
    public ApiController(ApiService apiService, StorageService storageService) {
        this.apiService = apiService;
        this.storageService = storageService;
    }

    // ================= CORE API =================

    /**
     * Send API request
     * POST /api/send
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponseDTO> sendRequest(@RequestBody ApiRequestDTO request) {

        ApiResponseDTO response = apiService.sendRequest(request);

        // Store in history
        storageService.addToHistory(request, response);

        return ResponseEntity.ok(response);
    }

    // ================= HISTORY =================

    /**
     * GET /api/history
     */
    @GetMapping("/history")
    public ResponseEntity<List<HistoryEntryDTO>> getHistory() {
        return ResponseEntity.ok(storageService.getHistory());
    }

    /**
     * DELETE /api/history
     */
    @DeleteMapping("/history")
    public ResponseEntity<String> clearHistory() {
        storageService.clearHistory();
        return ResponseEntity.ok("History cleared");
    }

    // ================= NAMED REQUESTS =================

    /**
     * POST /api/named
     */
    @PostMapping("/named")
    public ResponseEntity<NamedRequestDTO> saveNamedRequest(@RequestBody NamedRequestDTO request) {
        return ResponseEntity.ok(storageService.saveNamedRequest(request));
    }

    /**
     * GET /api/named
     */
    @GetMapping("/named")
    public ResponseEntity<List<NamedRequestDTO>> getNamedRequests() {
        return ResponseEntity.ok(storageService.getAllNamedRequests());
    }
}