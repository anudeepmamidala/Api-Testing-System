package com.anudeep.probeapi.controller;

import com.anudeep.probeapi.dto.HistoryEntryDTO;
import com.anudeep.probeapi.dto.HistoryPageResponseDTO;
import com.anudeep.probeapi.entity.User;
import com.anudeep.probeapi.service.HistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
@Slf4j
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping
    public ResponseEntity<HistoryPageResponseDTO> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestAttribute("user") User user) {
        return ResponseEntity.ok(historyService.getHistory(user, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoryEntryDTO> getHistoryEntry(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        return ResponseEntity.ok(historyService.getHistoryEntry(user, id));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearHistory(@RequestAttribute("user") User user) {
        historyService.clearHistory(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoryEntry(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        historyService.deleteHistoryEntry(user, id);
        return ResponseEntity.noContent().build();
    }

}
